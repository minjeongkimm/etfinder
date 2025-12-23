-- ==========================================
-- ETFinder 더미 데이터 검증 쿼리
-- ==========================================
-- 목적: 시딩된 더미 데이터 확인 및 검증
-- 
-- 실행 방법:
--   mysql -u root -p etfinder < verify_dummy_data.sql
-- 
-- 검증 항목:
--   1. 테이블별 더미 데이터 개수
--   2. ETF별 한줄평 샘플 및 감성 분포
--   3. 모의투자 랭킹 Top 10
--   4. 데이터 무결성 검증
-- ==========================================

USE etfinder;

-- ==========================================
-- 1. 테이블별 더미 데이터 개수
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📊 1. 테이블별 더미 데이터 개수' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '더미 유저 (users)' AS 테이블,
    COUNT(*) AS 개수
FROM users 
WHERE email LIKE 'dummy+%@etfinder.dev'

UNION ALL

SELECT 
    '한줄평 (comments)' AS 테이블,
    COUNT(*) AS 개수
FROM comments 
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')

UNION ALL

SELECT 
    '지갑 (wallet)' AS 테이블,
    COUNT(*) AS 개수
FROM wallet 
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')

UNION ALL

SELECT 
    '보유 종목 (holdings)' AS 테이블,
    COUNT(*) AS 개수
FROM holdings 
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')

UNION ALL

SELECT 
    '거래 내역 (trade_history)' AS 테이블,
    COUNT(*) AS 개수
FROM trade_history 
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')

UNION ALL

SELECT 
    '자산 스냅샷 (wallet_daily_snapshot)' AS 테이블,
    COUNT(*) AS 개수
FROM wallet_daily_snapshot 
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev');

-- ==========================================
-- 2. ETF별 한줄평 개수 및 감성 분포
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '💬 2. ETF별 한줄평 개수 및 감성 분포 (상위 10개)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    e.etf_code AS 'ETF 코드',
    e.etf_name AS 'ETF 이름',
    COUNT(*) AS '총 댓글수',
    SUM(CASE WHEN c.sentiment = 'POSITIVE' THEN 1 ELSE 0 END) AS '긍정',
    SUM(CASE WHEN c.sentiment = 'NEGATIVE' THEN 1 ELSE 0 END) AS '부정',
    SUM(CASE WHEN c.sentiment IS NULL THEN 1 ELSE 0 END) AS '중립/미분석',
    CONCAT(
        ROUND(SUM(CASE WHEN c.sentiment = 'POSITIVE' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 1), 
        '%'
    ) AS '긍정 비율'
FROM comments c
INNER JOIN etf_product e ON c.etf_id = e.etf_id
WHERE c.user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
GROUP BY e.etf_id, e.etf_code, e.etf_name
ORDER BY COUNT(*) DESC
LIMIT 10;

-- ==========================================
-- 3. ETF별 한줄평 샘플 (최신 3개씩)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📝 3. 한줄평 샘플 (첫 번째 ETF 기준 최신 5개)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    u.nickname AS '작성자',
    c.content AS '내용',
    c.sentiment AS '감성',
    DATE_FORMAT(c.created_at, '%Y-%m-%d %H:%i') AS '작성일시'
FROM comments c
INNER JOIN users u ON c.user_id = u.user_id
WHERE c.user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
  AND c.etf_id = (
      SELECT etf_id FROM comments 
      WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
      GROUP BY etf_id 
      ORDER BY COUNT(*) DESC 
      LIMIT 1
  )
ORDER BY c.created_at DESC
LIMIT 5;

-- ==========================================
-- 4. 모의투자 랭킹 Top 10 (실제 쿼리 사용)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '🏆 4. 모의투자 랭킹 Top 10 (MockRankingMapper 쿼리 사용)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT
    RANK() OVER (ORDER BY w.total_asset DESC) AS '순위',
    u.nickname AS '닉네임',
    FORMAT(w.total_asset, 0) AS '총 자산 (원)',
    CONCAT(
        IF(w.total_asset >= 10000000, '+', ''),
        ROUND((w.total_asset - 10000000) * 100.0 / 10000000, 2), 
        '%'
    ) AS '수익률',
    FORMAT(w.total_asset - 10000000, 0) AS '손익 (원)'
FROM wallet w
INNER JOIN users u ON w.user_id = u.user_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
ORDER BY w.total_asset DESC
LIMIT 10;

-- ==========================================
-- 5. 모의투자 랭킹 Bottom 5
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📉 5. 모의투자 랭킹 Bottom 5' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT
    RANK() OVER (ORDER BY w.total_asset DESC) AS '순위',
    u.nickname AS '닉네임',
    FORMAT(w.total_asset, 0) AS '총 자산 (원)',
    CONCAT(
        IF(w.total_asset >= 10000000, '+', ''),
        ROUND((w.total_asset - 10000000) * 100.0 / 10000000, 2), 
        '%'
    ) AS '수익률',
    FORMAT(w.total_asset - 10000000, 0) AS '손익 (원)'
FROM wallet w
INNER JOIN users u ON w.user_id = u.user_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
ORDER BY w.total_asset ASC
LIMIT 5;

-- ==========================================
-- 6. 유저별 보유 종목 현황 (샘플 3명)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📈 6. 유저별 보유 종목 현황 (상위 3명 샘플)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    u.nickname AS '유저',
    e.etf_code AS 'ETF 코드',
    e.etf_name AS 'ETF 이름',
    h.quantity AS '수량',
    FORMAT(h.average_price, 0) AS '평단가',
    FORMAT(e.current_price, 0) AS '현재가',
    FORMAT(e.current_price * h.quantity, 0) AS '평가금액',
    CONCAT(
        IF((e.current_price - h.average_price) >= 0, '+', ''),
        ROUND((e.current_price - h.average_price) * 100.0 / h.average_price, 2),
        '%'
    ) AS '수익률'
FROM holdings h
INNER JOIN users u ON h.user_id = u.user_id
INNER JOIN etf_product e ON h.etf_id = e.etf_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND u.user_id IN (
      SELECT user_id FROM wallet 
      WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
      ORDER BY total_asset DESC 
      LIMIT 3
  )
ORDER BY u.user_id, h.created_at DESC;

-- ==========================================
-- 7. 거래 내역 샘플 (최신 10건)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '💰 7. 거래 내역 샘플 (최신 10건)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    u.nickname AS '유저',
    e.etf_code AS 'ETF',
    t.trade_type AS '거래유형',
    t.quantity AS '수량',
    FORMAT(t.price, 0) AS '단가',
    FORMAT(t.amount, 0) AS '거래금액',
    DATE_FORMAT(t.created_at, '%Y-%m-%d %H:%i') AS '거래일시'
FROM trade_history t
INNER JOIN users u ON t.user_id = u.user_id
INNER JOIN etf_product e ON t.etf_id = e.etf_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
ORDER BY t.created_at DESC
LIMIT 10;

-- ==========================================
-- 8. 데이터 무결성 검증
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '✅ 8. 데이터 무결성 검증' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

-- 8-1. 지갑 없는 더미 유저 확인 (0이어야 정상)
SELECT 
    '지갑 없는 더미 유저' AS '검증 항목',
    COUNT(*) AS '개수',
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류') AS '상태'
FROM users u
LEFT JOIN wallet w ON u.user_id = w.user_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND w.user_id IS NULL

UNION ALL

-- 8-2. 보유 종목의 ETF가 실제 존재하는지 확인 (0이어야 정상)
SELECT 
    '존재하지 않는 ETF 보유' AS '검증 항목',
    COUNT(*) AS '개수',
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류') AS '상태'
FROM holdings h
LEFT JOIN etf_product e ON h.etf_id = e.etf_id
WHERE h.user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
  AND e.etf_id IS NULL

UNION ALL

-- 8-3. 한줄평의 ETF가 실제 존재하는지 확인 (0이어야 정상)
SELECT 
    '존재하지 않는 ETF 댓글' AS '검증 항목',
    COUNT(*) AS '개수',
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류') AS '상태'
FROM comments c
LEFT JOIN etf_product e ON c.etf_id = e.etf_id
WHERE c.user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
  AND e.etf_id IS NULL

UNION ALL

-- 8-4. 중복 보유 종목 확인 (0이어야 정상)
SELECT 
    '중복 보유 종목 (UNIQUE 위반)' AS '검증 항목',
    COUNT(*) - COUNT(DISTINCT user_id, etf_id) AS '개수',
    IF(COUNT(*) - COUNT(DISTINCT user_id, etf_id) = 0, '✅ 정상', '❌ 오류') AS '상태'
FROM holdings
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev');

-- ==========================================
-- 9. 감성 분포 통계
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '😊 9. 전체 한줄평 감성 분포' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    COALESCE(sentiment, '미분석') AS '감성',
    COUNT(*) AS '개수',
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM comments WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')), 1), '%') AS '비율'
FROM comments
WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
GROUP BY sentiment
ORDER BY COUNT(*) DESC;

-- ==========================================
-- 10. 랭킹 분포 통계
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📊 10. 수익률 분포 통계' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    CASE 
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 10 THEN '10% 이상 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 0 THEN '0~10% 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= -10 THEN '0~-10% 손실'
        ELSE '-10% 이상 손실'
    END AS '수익률 구간',
    COUNT(*) AS '유저 수',
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM wallet WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')), 1), '%') AS '비율'
FROM wallet w
WHERE w.user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev')
GROUP BY 
    CASE 
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 10 THEN '10% 이상 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 0 THEN '0~10% 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= -10 THEN '0~-10% 손실'
        ELSE '-10% 이상 손실'
    END
ORDER BY 
    CASE 
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 10 THEN 1
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 0 THEN 2
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= -10 THEN 3
        ELSE 4
    END;

SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '✅ 검증 완료!' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
