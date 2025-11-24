package com.etfinder.mvc.service;

import com.etfinder.mvc.model.dto.EtfProduct;
import com.etfinder.mvc.model.mapper.EtfMapper;
import com.etfinder.util.EtfClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtfLoadService {

    private final EtfMapper etfMapper;
    
    public EtfLoadService(EtfMapper etfMapper) {
		this.etfMapper = etfMapper;
	}
    /**
     * ETF 데이터를 엑셀 파일에서 읽어와 DB에 적재하는 메서드
     */
    @Transactional
    public void load() throws Exception {

        // ⚠️ 실제 파일 경로로 변경 필요
        // 추천: 프로젝트 루트에 /data 폴더 두고 파일 넣기
        String path = "src/main/resources/etfinder_dataset.xlsx";
        log.info("Loading ETF dataset from: {}", path);

        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int success = 0;
        int failed = 0;

        // 0번 row = 헤더 → 1번 row부터 시작
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                EtfProduct etf = new EtfProduct();

                // ================
                // 1) 기본 ETF 정보
                // ================
                etf.setEtfCode(getString(row.getCell(0)));   // 종목코드
                String name = getString(row.getCell(1));     // 종목명
                etf.setEtfName(name);

                // ================
                // 2) 시장 / 테마 자동 분류
                // ================
                etf.setMarket(EtfClassifier.extractMarket(name));
                etf.setTheme(EtfClassifier.extractTheme(name));

                // ================
                // 3) 위험등급 (1~5)
                // ================
                String volatility = getString(row.getCell(8)); // 변동성 텍스트
                etf.setRiskRating(EtfClassifier.convertRiskToInt(volatility));

                // ================
                // 4) 수치형 데이터 매핑
                // ================
                etf.setAum(getLong(row.getCell(7)));                     // 순자산총액
                etf.setFee(getDouble(row.getCell(9)));                  // 총보수
                etf.setReturn1mo(getDouble(row.getCell(10)));           // 수익률 1M
                etf.setReturn3mo(getDouble(row.getCell(11)));           // 수익률 3M
                etf.setReturn6mo(getDouble(row.getCell(12)));           // 수익률 6M
                etf.setReturn1yr(getDouble(row.getCell(13)));           // 수익률 1Y
                etf.setReturn3yr(getDouble(row.getCell(14)));           // 수익률 3Y

                // ================
                // 5) DB insert
                // ================
                etfMapper.insertEtf(etf);
                success++;

            } catch (Exception e) {
                failed++;
                log.error("Row {} insert failed: {}", i, e.getMessage());
            }
        }

        workbook.close();
        fis.close();

        log.info("ETF Load Finished — Success: {}, Failed: {}", success, failed);
    }

    // ===============================
    // 안전한 Cell 처리 유틸 메서드들
    // ===============================

    private String getString(Cell cell) {
        if (cell == null) return null;
        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? String.valueOf((long) cell.getNumericCellValue())
                    : cell.getStringCellValue().trim();
        } catch (Exception e) {
            return null;
        }
    }

    private Double getDouble(Cell cell) {
        if (cell == null) return null;
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private Long getLong(Cell cell) {
        if (cell == null) return null;
        try {
            return (long) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }
}
