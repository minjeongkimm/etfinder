-- ==========================================
-- ETFinder 대규모 더미 데이터 검증 쿼리
-- ==========================================
-- 목적: 시딩된 대규모 더미 데이터 확인 및 검증
-- 
-- 실행 방법:
--   mysql -u ssafy -pssafy etfinder < backend/src/main/resources/verify_large_scale_data.sql
-- 
-- 검증 항목:
--   1. 전체 유저 수 (1020명 예상)
--   2. Group A/B 유저 수
--   3. ETF 360750 한줄평
--   4. 모의투자 랭킹 Top 10
--   5. 데이터 무결성
-- ==========================================

USE etfinder;

-- ==========================================
-- 1. 전체 유저 수 확인
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📊 1. 전체 유저 수 확인' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '항목' AS 항목,
    '개수' AS 개수,
    '예상' AS 예상
UNION ALL
SELECT 
    'Group A 유저 (dummy+XXX)',
    CAST(COUNT(*) AS CHAR),
    '1000'
FROM users WHERE email LIKE 'dummy+%@etfinder.dev'
UNION ALL
SELECT 
    'Group B 유저 (reviewer+XXX)',
    CAST(COUNT(*) AS CHAR),
    '20'
FROM users WHERE email LIKE 'reviewer+%@etfinder.dev'
UNION ALL
SELECT 
    '전체 더미 유저',
    CAST(COUNT(*) AS CHAR),
    '1020'
FROM users WHERE provider = 'DEMO';

-- ==========================================
-- 2. Group B 유저 닉네임 확인
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '💬 2. Group B 유저 닉네임 확인 (리뷰 작성자)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    nickname AS '닉네임',
    email AS '이메일'
FROM users 
WHERE email LIKE 'reviewer+%@etfinder.dev'
ORDER BY email;

-- ==========================================
-- 3. ETF 360750 한줄평 확인
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📝 3. ETF 360750 한줄평 확인' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '감성' AS 감성,
    COUNT(*) AS 개수,
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM comments WHERE etf_id = 622), 1), '%') AS 비율
FROM comments
WHERE etf_id = 622
GROUP BY sentiment
ORDER BY 
    CASE sentiment
        WHEN 'POSITIVE' THEN 1
        WHEN NULL THEN 2
        WHEN 'NEGATIVE' THEN 3
    END;

SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📝 3-1. ETF 360750 한줄평 샘플 (최신 10개)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    u.nickname AS '작성자',
    c.content AS '내용',
    c.sentiment AS '감성',
    DATE_FORMAT(c.created_at, '%Y-%m-%d') AS '작성일'
FROM comments c
JOIN users u ON c.user_id = u.user_id
WHERE c.etf_id = 622
ORDER BY c.created_at DESC
LIMIT 10;

-- ==========================================
-- 4. 모의투자 데이터 규모 확인
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '💰 4. 모의투자 데이터 규모 확인' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '항목' AS 항목,
    '개수' AS 개수
UNION ALL
SELECT 
    '지갑',
    CAST(COUNT(*) AS CHAR)
FROM wallet WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
UNION ALL
SELECT 
    '보유 종목',
    CAST(COUNT(*) AS CHAR)
FROM holdings WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
UNION ALL
SELECT 
    '거래 내역',
    CAST(COUNT(*) AS CHAR)
FROM trade_history WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
UNION ALL
SELECT 
    '자산 스냅샷',
    CAST(COUNT(*) AS CHAR)
FROM wallet_daily_snapshot WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 5. 모의투자 랭킹 Top 10 (MockRankingMapper 쿼리 사용)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '🏆 5. 모의투자 랭킹 Top 10' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT
    RANK() OVER (ORDER BY w.total_asset DESC) AS '순위',
    u.nickname AS '닉네임',
    FORMAT(w.total_asset, 0) AS '총자산',
    CONCAT(
        IF(w.total_asset >= 10000000, '+', ''),
        ROUND((w.total_asset - 10000000) * 100.0 / 10000000, 2), 
        '%'
    ) AS '수익률',
    FORMAT(w.total_asset - 10000000, 0) AS '손익'
FROM wallet w
INNER JOIN users u ON w.user_id = u.user_id
WHERE u.provider = 'DEMO'
ORDER BY w.total_asset DESC
LIMIT 10;

-- ==========================================
-- 6. 모의투자 랭킹 Bottom 5
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📉 6. 모의투자 랭킹 Bottom 5' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT
    RANK() OVER (ORDER BY w.total_asset DESC) AS '순위',
    u.nickname AS '닉네임',
    FORMAT(w.total_asset, 0) AS '총자산',
    CONCAT(
        IF(w.total_asset >= 10000000, '+', ''),
        ROUND((w.total_asset - 10000000) * 100.0 / 10000000, 2), 
        '%'
    ) AS '수익률',
    FORMAT(w.total_asset - 10000000, 0) AS '손익'
FROM wallet w
INNER JOIN users u ON w.user_id = u.user_id
WHERE u.provider = 'DEMO'
ORDER BY w.total_asset ASC
LIMIT 5;

-- ==========================================
-- 7. 수익률 분포 통계
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📊 7. 수익률 분포 통계' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    CASE 
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 10 THEN '10% 이상 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= 0 THEN '0~10% 수익'
        WHEN ((w.total_asset - 10000000) * 100.0 / 10000000) >= -10 THEN '0~-10% 손실'
        ELSE '-10% 이상 손실'
    END AS '수익률 구간',
    COUNT(*) AS '유저 수',
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM wallet WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')), 1), '%') AS '비율'
FROM wallet w
WHERE w.user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
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

-- ==========================================
-- 8. 데이터 무결성 검증
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '✅ 8. 데이터 무결성 검증' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '검증 항목' AS 검증항목,
    '개수' AS 개수,
    '상태' AS 상태
UNION ALL
SELECT 
    '지갑 없는 더미 유저',
    CAST(COUNT(*) AS CHAR),
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류')
FROM users u
LEFT JOIN wallet w ON u.user_id = w.user_id
WHERE u.provider = 'DEMO'
  AND u.email LIKE 'dummy+%@etfinder.dev'
  AND w.user_id IS NULL
UNION ALL
SELECT 
    '존재하지 않는 ETF 보유',
    CAST(COUNT(*) AS CHAR),
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류')
FROM holdings h
LEFT JOIN etf_product e ON h.etf_id = e.etf_id
WHERE h.user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
  AND e.etf_id IS NULL
UNION ALL
SELECT 
    '존재하지 않는 ETF 댓글',
    CAST(COUNT(*) AS CHAR),
    IF(COUNT(*) = 0, '✅ 정상', '❌ 오류')
FROM comments c
LEFT JOIN etf_product e ON c.etf_id = e.etf_id
WHERE c.user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
  AND e.etf_id IS NULL
UNION ALL
SELECT 
    '중복 보유 종목 (UNIQUE 위반)',
    CAST(COUNT(*) - COUNT(DISTINCT user_id, etf_id) AS CHAR),
    IF(COUNT(*) - COUNT(DISTINCT user_id, etf_id) = 0, '✅ 정상', '❌ 오류')
FROM holdings
WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 9. 보유 종목 샘플 (상위 3명)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '📈 9. 보유 종목 샘플 (랭킹 상위 3명)' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    u.nickname AS '유저',
    e.etf_code AS 'ETF코드',
    e.etf_name AS 'ETF이름',
    h.quantity AS '수량',
    FORMAT(h.average_price, 0) AS '평단가',
    FORMAT(e.current_price, 0) AS '현재가',
    CONCAT(
        IF((e.current_price - h.average_price) >= 0, '+', ''),
        ROUND((e.current_price - h.average_price) * 100.0 / h.average_price, 2),
        '%'
    ) AS '수익률'
FROM holdings h
INNER JOIN users u ON h.user_id = u.user_id
INNER JOIN etf_product e ON h.etf_id = e.etf_id
WHERE u.provider = 'DEMO'
  AND u.user_id IN (
      SELECT user_id FROM wallet 
      WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO')
      ORDER BY total_asset DESC 
      LIMIT 3
  )
ORDER BY u.user_id, h.created_at DESC
LIMIT 15;

-- ==========================================
-- 10. 거래 내역 샘플 (최신 10건)
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '💸 10. 거래 내역 샘플 (최신 10건)' AS '';
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
WHERE u.provider = 'DEMO'
ORDER BY t.created_at DESC
LIMIT 10;

SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '✅ 검증 완료!' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
