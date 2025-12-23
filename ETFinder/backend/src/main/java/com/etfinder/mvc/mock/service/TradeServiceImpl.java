package com.etfinder.mvc.mock.service;

import java.util.List;

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

@Service
@Transactional
public class TradeServiceImpl implements TradeService {

	private final WalletMapper walletMapper;
	private final MockHoldingMapper holdingMapper;
	private final TradeHistoryMapper tradeHistoryMapper;
	private final EtfMapper etfMapper;
	private final WalletService walletService;

	public TradeServiceImpl(
			WalletMapper walletMapper,
			MockHoldingMapper holdingMapper,
			TradeHistoryMapper tradeHistoryMapper,
			EtfMapper etfMapper,
			WalletService walletService) {
		this.walletMapper = walletMapper;
		this.holdingMapper = holdingMapper;
		this.tradeHistoryMapper = tradeHistoryMapper;
		this.etfMapper = etfMapper;
		this.walletService = walletService;
	}

	@Override
	public int executeTrade(Long userId, TradeRequest request) {

		// 1. 유효성 검사
		if (request.getQuantity() == null || request.getQuantity() <= 0) {
			throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
		}

		// 2. ETF 현재가 조회
		EtfProduct etf = etfMapper.selectOneEtf(request.getEtfId());
		if (etf == null) {
			throw new IllegalArgumentException("존재하지 않는 ETF입니다.");
		}

		Integer currentPrice = etf.getCurrentPrice();
		if (currentPrice == null || currentPrice <= 0) {
			throw new IllegalStateException("ETF 가격 정보가 없습니다.");
		}

		// 3. 거래액 계산
		long amount = (long) currentPrice * request.getQuantity();

		// 4. 거래 타입에 따라 처리
		String tradeType = request.getTradeType().toUpperCase();

		if ("BUY".equals(tradeType)) {
			processBuy(userId, request.getEtfId(), currentPrice, request.getQuantity(), amount);
		} else if ("SELL".equals(tradeType)) {
			processSell(userId, request.getEtfId(), currentPrice, request.getQuantity(), amount);
		} else {
			throw new IllegalArgumentException("거래 타입은 BUY 또는 SELL이어야 합니다.");
		}

		// 5. 거래 내역 저장
		TradeHistory trade = new TradeHistory();
		trade.setUserId(userId);
		trade.setEtfId(request.getEtfId());
		trade.setTradeType(tradeType);
		trade.setPrice(currentPrice);
		trade.setQuantity(request.getQuantity());
		trade.setAmount(amount);

		tradeHistoryMapper.insert(trade);

		// 6. 총 자산 갱신
		walletService.refreshTotalAsset(userId);

		// 7. 당시간 스냅샷 저장 (신규)
		walletService.saveCurrentHourSnapshot(userId);

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
	 */
	private void processSell(Long userId, Long etfId, Integer price, Integer quantity, Long amount) {

		// 1. 보유 종목 조회 (Row Lock)
		MockHolding holding = holdingMapper.selectForUpdate(userId, etfId);

		if (holding == null) {
			throw new IllegalStateException("보유하지 않은 종목입니다.");
		}

		// 2. 보유 수량 확인
		if (holding.getQuantity() < quantity) {
			throw new IllegalStateException("보유 수량이 부족합니다.");
		}

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
	}

	@Override
	@Transactional(readOnly = true)
	public List<TradeHistoryResponse> getRecentTrades(Long userId) {
		return tradeHistoryMapper.selectRecentTrades(userId);
	}
}
