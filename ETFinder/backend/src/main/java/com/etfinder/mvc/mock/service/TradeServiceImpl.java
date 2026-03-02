package com.etfinder.mvc.mock.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.mock.dto.MockHolding;
import com.etfinder.mvc.mock.dto.TradeHistory;
import com.etfinder.mvc.mock.dto.TradeHistoryResponse;
import com.etfinder.mvc.mock.dto.TradeRequest;
import com.etfinder.mvc.mock.dto.Wallet;
import com.etfinder.mvc.mock.mapper.MockHoldingMapper;
import com.etfinder.mvc.mock.mapper.TradeHistoryMapper;
import com.etfinder.mvc.mock.mapper.WalletMapper;
import com.etfinder.mvc.realtime.cache.RealTimePriceCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TradeServiceImpl implements TradeService {

	private final WalletMapper walletMapper;
	private final MockHoldingMapper holdingMapper;
	private final TradeHistoryMapper tradeHistoryMapper;
	private final EtfMapper etfMapper;
	private final WalletService walletService;
	private final RealTimePriceCache priceCache;

	public TradeServiceImpl(
			WalletMapper walletMapper,
			MockHoldingMapper holdingMapper,
			TradeHistoryMapper tradeHistoryMapper,
			EtfMapper etfMapper,
			WalletService walletService,
			RealTimePriceCache priceCache) {
		this.walletMapper = walletMapper;
		this.holdingMapper = holdingMapper;
		this.tradeHistoryMapper = tradeHistoryMapper;
		this.etfMapper = etfMapper;
		this.walletService = walletService;
		this.priceCache = priceCache;
	}

	@Override
	public int executeTrade(Long userId, TradeRequest request) {

		// 1. 유효성 검사
		if (request.getQuantity() == null || request.getQuantity() <= 0) {
			throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
		}

		// 2. ETF 정보 조회 (종목코드 필요)
		EtfProduct etf = etfMapper.selectOneEtf(request.getEtfId());
		if (etf == null) {
			throw new IllegalArgumentException("존재하지 않는 ETF입니다.");
		}

		// 3. 실행 가격 결정: 실시간 캐시 우선, DB fallback
		Integer executionPrice = null;
		String priceSource = null;

		// 3-1. 실시간 캐시에서 가격 조회 시도
		String etfCode = etf.getEtfCode();
		Optional<Integer> cachedPrice = priceCache.getPrice(etfCode);

		if (cachedPrice.isPresent()) {
			executionPrice = cachedPrice.get();
			priceSource = "CACHE";
			log.info("💸 [거래 실행] 종목: {}, 가격 소스: 실시간 캐시, 가격: {}원", etfCode, executionPrice);
		} else {
			// 3-2. 캐시 미스: DB current_price로 fallback
			executionPrice = etf.getCurrentPrice();
			priceSource = "DB_FALLBACK";
			log.warn("⚠️ [거래 실행] 종목: {}, 가격 소스: DB (캐시 미스), 가격: {}원", etfCode, executionPrice);
		}

		// 3-3. 가격 유효성 검증
		if (executionPrice == null || executionPrice <= 0) {
			log.error("❌ [거래 실행 실패] 종목: {}, 가격 없음 (캐시: {}, DB: {})",
					etfCode, cachedPrice.orElse(null), etf.getCurrentPrice());
			throw new IllegalStateException("ETF 가격 정보가 없습니다. 종목코드: " + etfCode);
		}

		// 4. 거래액 계산
		long amount = (long) executionPrice * request.getQuantity();

		// 5. 거래 타입에 따라 처리
		String tradeType = request.getTradeType().toUpperCase();
		long realizedPnL = 0L; // 매도 시 실현손익 저장

		if ("BUY".equals(tradeType)) {
			processBuy(userId, request.getEtfId(), executionPrice, request.getQuantity(), amount);
			log.info("✅ [매수 완료] 사용자: {}, 종목: {}, 수량: {}, 단가: {}원 ({})",
					userId, etfCode, request.getQuantity(), executionPrice, priceSource);
		} else if ("SELL".equals(tradeType)) {
			realizedPnL = processSell(userId, request.getEtfId(), executionPrice, request.getQuantity(), amount);
			log.info("✅ [매도 완료] 사용자: {}, 종목: {}, 수량: {}, 단가: {}원 ({}), 실현손익: {}원",
					userId, etfCode, request.getQuantity(), executionPrice, priceSource, realizedPnL);
		} else {
			throw new IllegalArgumentException("거래 타입은 BUY 또는 SELL이어야 합니다.");
		}

		// 6. 거래 내역 저장
		TradeHistory trade = new TradeHistory();
		trade.setUserId(userId);
		trade.setEtfId(request.getEtfId());
		trade.setTradeType(tradeType);
		trade.setPrice(executionPrice);
		trade.setQuantity(request.getQuantity());
		trade.setAmount(amount);

		tradeHistoryMapper.insert(trade);

		// 7. 총 자산 갱신
		walletService.refreshTotalAsset(userId);

		// 8. 당시간 스냅샷 저장 (매도 시 실현손익 포함)
		if ("SELL".equals(tradeType)) {
			walletService.saveCurrentHourSnapshotWithRealizedPnL(userId, realizedPnL);
		} else {
			walletService.saveCurrentHourSnapshot(userId);
		}

		return 1;
	}

	/**
	 * 매수 처리
	 */
	private void processBuy(Long userId, Long etfId, Integer price, Integer quantity, Long amount) {

		// 1. 지갑 조회 (없으면 생성)
		Wallet wallet = walletMapper.selectByUserId(userId);
		if (wallet == null) {
			walletMapper.insertInitialWallet(userId);
			wallet = walletMapper.selectByUserId(userId);
		}

		// 2. 잔액 확인
		if (wallet.getBalance() < amount) {
			throw new IllegalStateException("잔액이 부족합니다.");
		}

		// 3. 잔액 차감
		long newBalance = wallet.getBalance() - amount;
		walletMapper.updateBalance(userId, newBalance);

		// 4. 보유 종목 업데이트 (Row Lock)
		MockHolding holding = holdingMapper.selectForUpdate(userId, etfId);

		if (holding == null) {
			// 신규 보유
			MockHolding newHolding = new MockHolding();
			newHolding.setUserId(userId);
			newHolding.setEtfId(etfId);
			newHolding.setQuantity(quantity);
			newHolding.setAveragePrice(price);

			holdingMapper.insertHolding(newHolding);
		} else {
			// 기존 보유 수량 추가 및 평단가 재계산
			int oldQuantity = holding.getQuantity();
			int oldAvgPrice = holding.getAveragePrice();

			int newQuantity = oldQuantity + quantity;
			// 평단가 = (기존 매입금액 + 신규 매입금액) / 신규 총 수량
			long totalCost = (long) oldQuantity * oldAvgPrice + (long) quantity * price;
			int newAvgPrice = (int) (totalCost / newQuantity);

			holding.setQuantity(newQuantity);
			holding.setAveragePrice(newAvgPrice);

			holdingMapper.updateHolding(holding);
		}
	}

	/**
	 * 매도 처리
	 * 
	 * @return 실현손익 (realized P&L)
	 */
	private long processSell(Long userId, Long etfId, Integer price, Integer quantity, Long amount) {

		// 1. 보유 종목 조회 (Row Lock)
		MockHolding holding = holdingMapper.selectForUpdate(userId, etfId);

		if (holding == null) {
			throw new IllegalStateException("보유하지 않은 종목입니다.");
		}

		// 2. 보유 수량 확인
		if (holding.getQuantity() < quantity) {
			throw new IllegalStateException("보유 수량이 부족합니다.");
		}

		// ===== 실현손익 계산 로직 =====
		int avgCost = holding.getAveragePrice();
		int sellPrice = price;
		int soldQty = quantity;

		// 실현손익 = (매도가 - 평단가) * 수량
		long realizedPnL = (long) (sellPrice - avgCost) * soldQty;

		// 3. 보유 수량 감소 또는 삭제
		int newQuantity = holding.getQuantity() - quantity;

		if (newQuantity == 0) {
			// 전량 매도 - 보유 종목 삭제
			holdingMapper.deleteHolding(userId, etfId);
		} else {
			// 일부 매도 - 수량만 감소 (평단가는 유지)
			holding.setQuantity(newQuantity);
			holdingMapper.updateHolding(holding);
		}

		// 4. 잔액 증가
		Wallet wallet = walletMapper.selectByUserId(userId);
		long newBalance = wallet.getBalance() + amount;
		walletMapper.updateBalance(userId, newBalance);

		// 실현손익 반환 (스냅샷 저장 시 사용)
		return realizedPnL;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TradeHistoryResponse> getRecentTrades(Long userId) {
		return tradeHistoryMapper.selectRecentTrades(userId);
	}
}
