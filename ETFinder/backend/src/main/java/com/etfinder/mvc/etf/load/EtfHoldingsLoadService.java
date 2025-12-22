package com.etfinder.mvc.etf.load;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfHolding;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtfHoldingsLoadService {

    private final EtfMapper etfMapper;

    @Transactional
    public void loadHoldings() throws Exception {
        
    	// 1. [핵심] 전체 ETF 목록을 딱 한 번 조회
        List<EtfProduct> allEtfs = etfMapper.selectAllEtf();
        
        // 2. 조회한 리스트를 검색하기 쉽게 Map으로 변환 (Key: 종목코드, Value: ID)
        Map<String, Long> etfMap = new HashMap<>();
        for (EtfProduct etf : allEtfs) {
            etfMap.put(etf.getEtfCode(), etf.getEtfId());
        }

        // 3. 파일 읽기 시작
        String folderPath = "src/main/resources/holdings/";
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));

        if (files == null || files.length == 0) return;

        for (File file : files) {
        	// 파일명에서 코드 추출 ("360750.xlsx" -> "360750")
            String etfCode = file.getName().replace(".xlsx", "");
            // 4. Map에서 ID 찾기 (DB 조회 아님, 메모리 조회라 엄청 빠름)
            Long etfId = etfMap.get(etfCode);
            
            if (etfId == null) {
                log.warn("DB에 없는 ETF입니다. 패스: {}", etfCode);
                continue;
            }

            // 기존 구성종목 삭제
            etfMapper.deleteHoldingsByEtfId(etfId);
            // 파일 파싱 및 저장
            parseAndSave(file, etfId);
        }
    }

    private void parseAndSave(File file, Long etfId) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            
            // 1. 데이터를 임시로 담을 리스트와 합계 변수 준비
            List<EtfHolding> tempList = new ArrayList<>();
            BigDecimal totalWeight = BigDecimal.ZERO;
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String rawCode = getString(row.getCell(0));
                    String rawName = getString(row.getCell(1));
                    
                    EtfHolding holding = new EtfHolding();
                    holding.setEtfId(etfId);
                    
                    // 종목코드 처리 (" US Equity" 제거)
                    if (rawCode != null && rawCode.toUpperCase().contains("EQUITY")) {
                         holding.setStockCode(rawCode.split(" ")[0]);
                    } else {
                         holding.setStockCode(rawCode);
                    }
                    
                    holding.setStockName(rawName);
                    
                    // 일단 있는 그대로 값을 읽어옴 (x100 하지 않음)
                    BigDecimal weight = getBigDecimal(row.getCell(2));
                    
                    if (weight != null) {
                        holding.setWeight(weight);
                        tempList.add(holding);
                        totalWeight = totalWeight.add(weight); // 합계 누적
                    }

                } catch (Exception e) {
                    log.error("Parsing Error at row {}: {}", i, e.getMessage());
                }
            }

            // 2. 합계를 보고 "비율(Ratio)"인지 "퍼센트(%)"인지 판단
            // 합계가 10보다 작으면 비율(0.xx)로 작성된 문서라고 판단 (보통 합이 1.0 근처임)
            // 합계가 10보다 크면 퍼센트(xx.xx)로 작성된 문서라고 판단 (보통 합이 100.0 근처임)
            BigDecimal multiplier = BigDecimal.ONE;
            if (totalWeight.compareTo(BigDecimal.TEN) < 0) {
                log.info("[{}] 문서 타입: 비율(Ratio) -> x100 보정 적용", etfId);
                multiplier = new BigDecimal("100");
            } else {
                log.info("[{}] 문서 타입: 퍼센트(%) -> 보정 없음", etfId);
            }

            // 3. 최종 저장 (보정치 적용)
            for (EtfHolding h : tempList) {
                if (h.getWeight() != null) {
                    // 비율이면 100 곱해서 저장, 퍼센트면 그대로 저장
                    h.setWeight(h.getWeight().multiply(multiplier));
                }
                etfMapper.insertHolding(h);
            }
            
            log.info("Saved {} items for ETF ID {}", tempList.size(), etfId);

        } catch (Exception e) {
            log.error("File Error: {}", file.getName());
        }
    }
    
    // BigDecimal 변환 유틸 메서드
    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return null;
        try {
            // 1. 숫자형 셀일 경우 (가장 흔함)
            if (cell.getCellType() == CellType.NUMERIC) {
                // double 값을 BigDecimal로 안전하게 변환
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } 
            // 2. 문자형 셀일 경우 ("6.89" 처럼 텍스트로 저장된 경우)
            else if (cell.getCellType() == CellType.STRING) {
                String text = cell.getStringCellValue().trim();
                
                // [핵심] % 기호가 있으면 제거
                if (text.contains("%")) 
                    text = text.replace("%", "").trim();
                
                if (text.isEmpty()) return null;
                
                return new BigDecimal(text);
            }
        } catch (Exception e) {
            // 파싱 실패 시 null 리턴 (로그 찍어도 됨)
            return null;
        }
        return null;
    }

    // 유틸 메서드
    private String getString(Cell cell) {
        if (cell == null) return null;
        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? String.valueOf((long) cell.getNumericCellValue())
                    : cell.getStringCellValue().trim();
        } catch (Exception e) { return null; }
    }
}