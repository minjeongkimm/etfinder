-- ==========================================
-- ETFinder 대규모 더미 데이터 시딩 스크립트
-- ==========================================
-- 목적: 
--   1) Group A: 1000명 랭킹용 유저 생성
--   2) Group B: 20명 리뷰 작성자 생성 (실제 커뮤니티 닉네임)
--   3) ETF 360750 전용 한줄평 20개 생성
--   4) 대규모 모의투자 데이터 생성
-- 
-- 실행 방법:
--   mysql -u ssafy -pssafy etfinder < backend/src/main/resources/seed_large_scale_dummy_data.sql
-- 
-- 예상 실행 시간: 10~30초
-- 예상 데이터 규모: ~15,000건
-- ==========================================

USE etfinder;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 트랜잭션 시작
START TRANSACTION;

SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '🚀 대규모 더미 데이터 시딩 시작' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

-- ==========================================
-- 1. Group A: 랭킹용 유저 생성 (1000명)
-- ==========================================
SELECT '📊 Group A: 랭킹용 유저 1000명 생성 중...' AS status;

-- 1-1. 첫 500명
INSERT INTO users (email, nickname, age, provider, provider_id, propensity, created_at)
SELECT 
    CONCAT('dummy+', LPAD(n, 4, '0'), '@etfinder.dev') AS email,
    CONCAT('demo_', LPAD(n, 4, '0')) AS nickname,
    20 + (n % 40) AS age,
    'DEMO' AS provider,
    CONCAT('DEMO_', LPAD(n, 4, '0')) AS provider_id,
    ELT((n % 3) + 1, 'AGGRESSIVE', 'STABLE', 'MODERATE') AS propensity,
    DATE_SUB(NOW(), INTERVAL (n % 15) DAY) AS created_at
FROM (
    SELECT @row := @row + 1 AS n
    FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
         (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
         (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) t3,
         (SELECT @row := 0) r
    WHERE @row < 500
) numbers;

-- 1-2. 다음 500명
INSERT INTO users (email, nickname, age, provider, provider_id, propensity, created_at)
SELECT 
    CONCAT('dummy+', LPAD(n + 500, 4, '0'), '@etfinder.dev') AS email,
    CONCAT('demo_', LPAD(n + 500, 4, '0')) AS nickname,
    20 + ((n + 500) % 40) AS age,
    'DEMO' AS provider,
    CONCAT('DEMO_', LPAD(n + 500, 4, '0')) AS provider_id,
    ELT(((n + 500) % 3) + 1, 'AGGRESSIVE', 'STABLE', 'MODERATE') AS propensity,
    DATE_SUB(NOW(), INTERVAL ((n + 500) % 15) DAY) AS created_at
FROM (
    SELECT @row2 := @row2 + 1 AS n
    FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
         (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
         (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) t3,
         (SELECT @row2 := 0) r
    WHERE @row2 < 500
) numbers;

SELECT CONCAT('✅ Group A 유저 ', COUNT(*), '명 생성 완료') AS result
FROM users WHERE provider = 'DEMO' AND email LIKE 'dummy+%@etfinder.dev';

-- ==========================================
-- 2. Group B: 리뷰 작성자 생성 (20명)
-- ==========================================
SELECT '💬 Group B: 리뷰 작성자 20명 생성 중...' AS status;

INSERT INTO users (email, nickname, age, provider, provider_id, propensity, created_at) VALUES
('reviewer+001@etfinder.dev', '우직한종목시세보스', 28, 'DEMO', 'REVIEWER_001', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 15 DAY)),
('reviewer+002@etfinder.dev', '물리면버틴다', 35, 'DEMO', 'REVIEWER_002', 'STABLE', DATE_SUB(NOW(), INTERVAL 14 DAY)),
('reviewer+003@etfinder.dev', '장마감에운다', 42, 'DEMO', 'REVIEWER_003', 'MODERATE', DATE_SUB(NOW(), INTERVAL 13 DAY)),
('reviewer+004@etfinder.dev', '수익좀주라', 31, 'DEMO', 'REVIEWER_004', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 12 DAY)),
('reviewer+005@etfinder.dev', '존버는승리한다', 26, 'DEMO', 'REVIEWER_005', 'STABLE', DATE_SUB(NOW(), INTERVAL 11 DAY)),
('reviewer+006@etfinder.dev', '국장탈출기원', 39, 'DEMO', 'REVIEWER_006', 'MODERATE', DATE_SUB(NOW(), INTERVAL 10 DAY)),
('reviewer+007@etfinder.dev', '개미는운다', 33, 'DEMO', 'REVIEWER_007', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 9 DAY)),
('reviewer+008@etfinder.dev', '배당만먹고간다', 29, 'DEMO', 'REVIEWER_008', 'STABLE', DATE_SUB(NOW(), INTERVAL 8 DAY)),
('reviewer+009@etfinder.dev', '손절은없다', 45, 'DEMO', 'REVIEWER_009', 'MODERATE', DATE_SUB(NOW(), INTERVAL 7 DAY)),
('reviewer+010@etfinder.dev', '분할매수신봉자', 37, 'DEMO', 'REVIEWER_010', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 6 DAY)),
('reviewer+011@etfinder.dev', '파란불만본다', 27, 'DEMO', 'REVIEWER_011', 'STABLE', DATE_SUB(NOW(), INTERVAL 15 DAY)),
('reviewer+012@etfinder.dev', '물타기장인', 34, 'DEMO', 'REVIEWER_012', 'MODERATE', DATE_SUB(NOW(), INTERVAL 14 DAY)),
('reviewer+013@etfinder.dev', '반등노리는개미', 41, 'DEMO', 'REVIEWER_013', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 13 DAY)),
('reviewer+014@etfinder.dev', '손익률마이너스', 30, 'DEMO', 'REVIEWER_014', 'STABLE', DATE_SUB(NOW(), INTERVAL 12 DAY)),
('reviewer+015@etfinder.dev', '장투만믿는다', 25, 'DEMO', 'REVIEWER_015', 'MODERATE', DATE_SUB(NOW(), INTERVAL 11 DAY)),
('reviewer+016@etfinder.dev', '평단가내려라', 38, 'DEMO', 'REVIEWER_016', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 10 DAY)),
('reviewer+017@etfinder.dev', '매수타이밍놓침', 32, 'DEMO', 'REVIEWER_017', 'STABLE', DATE_SUB(NOW(), INTERVAL 9 DAY)),
('reviewer+018@etfinder.dev', '빚투는위험해', 28, 'DEMO', 'REVIEWER_018', 'MODERATE', DATE_SUB(NOW(), INTERVAL 8 DAY)),
('reviewer+019@etfinder.dev', '월급쟁이투자왕', 44, 'DEMO', 'REVIEWER_019', 'AGGRESSIVE', DATE_SUB(NOW(), INTERVAL 7 DAY)),
('reviewer+020@etfinder.dev', '계좌빨개요', 36, 'DEMO', 'REVIEWER_020', 'STABLE', DATE_SUB(NOW(), INTERVAL 6 DAY));

SELECT CONCAT('✅ Group B 유저 ', COUNT(*), '명 생성 완료') AS result
FROM users WHERE provider = 'DEMO' AND email LIKE 'reviewer+%@etfinder.dev';

-- ==========================================
-- 3. ETF 360750 전용 한줄평 생성 (20개)
-- ==========================================
SELECT '📝 ETF 360750 한줄평 20개 생성 중...' AS status;

-- 3-1. 긍정 리뷰 (14개, 70%)
INSERT INTO comments (user_id, etf_id, content, sentiment, created_at, updated_at)
SELECT 
    (SELECT user_id FROM users WHERE email LIKE 'reviewer+%@etfinder.dev' ORDER BY RAND() LIMIT 1),
    622, -- ETF 360750의 etf_id
    content,
    'POSITIVE',
    DATE_SUB(NOW(), INTERVAL days DAY),
    DATE_SUB(NOW(), INTERVAL days DAY)
FROM (
    SELECT '미국 S&P500 따라가니까 안정적이고 좋음' AS content, 1 AS days
    UNION ALL SELECT '장기 투자 각이다 이거', 3
    UNION ALL SELECT '배당도 나오고 수익률도 괜찮아서 만족', 5
    UNION ALL SELECT '미국 시장 믿고 간다
이거 하나면 충분함', 7
    UNION ALL SELECT '변동성 적고 꾸준히 우상향 중', 9
    UNION ALL SELECT '초보자한테 딱 좋은 ETF
추천함', 11
    UNION ALL SELECT '수수료 대비 가성비 최고', 13
    UNION ALL SELECT '분산투자 핵심 종목으로 보유 중', 15
    UNION ALL SELECT '하루 수익이 미쳤네 ㅋㅋ', 17
    UNION ALL SELECT '이거 선택한 나 칭찬해', 19
    UNION ALL SELECT '역시 S&P500은 믿고 투자', 21
    UNION ALL SELECT '장투 최고의 선택이었음', 23
    UNION ALL SELECT '안정적으로 수익 나와서 좋다', 25
    UNION ALL SELECT '테마 자체가 유망해서 계속 보유할 예정', 27
) AS positive_reviews;

-- 3-2. 중립 리뷰 (3개, 15%)
INSERT INTO comments (user_id, etf_id, content, sentiment, created_at, updated_at)
SELECT 
    (SELECT user_id FROM users WHERE email LIKE 'reviewer+%@etfinder.dev' ORDER BY RAND() LIMIT 1),
    622,
    content,
    NULL,
    DATE_SUB(NOW(), INTERVAL days DAY),
    DATE_SUB(NOW(), INTERVAL days DAY)
FROM (
    SELECT '흐름은 나쁘지 않은데 아직 모르겠다' AS content, 2 AS days
    UNION ALL SELECT '환율 영향 받아서 좀 애매함
좀 더 지켜봐야 할 듯', 10
    UNION ALL SELECT '단기보단 중장기 관점으로 봐야 할 것 같음', 18
) AS neutral_reviews;

-- 3-3. 부정 리뷰 (3개, 15%)
INSERT INTO comments (user_id, etf_id, content, sentiment, created_at, updated_at)
SELECT 
    (SELECT user_id FROM users WHERE email LIKE 'reviewer+%@etfinder.dev' ORDER BY RAND() LIMIT 1),
    622,
    content,
    'NEGATIVE',
    DATE_SUB(NOW(), INTERVAL days DAY),
    DATE_SUB(NOW(), INTERVAL days DAY)
FROM (
    SELECT '환율 때문에 수익률이 생각보다 안나옴' AS content, 4 AS days
    UNION ALL SELECT '미국 시장 하락하면 같이 떨어져서 리스크 있음', 12
    UNION ALL SELECT '차라리 국내 ETF가 나을 듯
환차손 무시 못함', 20
) AS negative_reviews;

SELECT CONCAT('✅ ETF 360750 한줄평 ', COUNT(*), '개 생성 완료') AS result
FROM comments WHERE etf_id = 622;

-- ==========================================
-- 4. 모의투자 지갑 생성 (1000개)
-- ==========================================
SELECT '💰 모의투자 지갑 1000개 생성 중...' AS status;

-- 4-1. 첫 500개
INSERT INTO wallet (user_id, balance, total_asset, updated_at)
SELECT 
    u.user_id,
    CASE 
        WHEN u.user_id % 1000 <= 200 THEN FLOOR(11000000 + (u.user_id * 37891 % 2000000)) * 0.3
        WHEN u.user_id % 1000 <= 800 THEN FLOOR(9500000 + (u.user_id * 27183 % 1500000)) * 0.4
        ELSE FLOOR(7000000 + (u.user_id * 31415 % 2500000)) * 0.5
    END AS balance,
    CASE 
        WHEN u.user_id % 1000 <= 200 THEN 11000000 + (u.user_id * 37891 % 2000000)
        WHEN u.user_id % 1000 <= 800 THEN 9500000 + (u.user_id * 27183 % 1500000)
        ELSE 7000000 + (u.user_id * 31415 % 2500000)
    END AS total_asset,
    DATE_SUB(NOW(), INTERVAL (u.user_id % 15) DAY) AS updated_at
FROM users u
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND u.user_id <= (SELECT MIN(user_id) + 499 FROM users WHERE email LIKE 'dummy+%@etfinder.dev');

-- 4-2. 다음 500개
INSERT INTO wallet (user_id, balance, total_asset, updated_at)
SELECT 
    u.user_id,
    CASE 
        WHEN u.user_id % 1000 <= 200 THEN FLOOR(11000000 + (u.user_id * 37891 % 2000000)) * 0.3
        WHEN u.user_id % 1000 <= 800 THEN FLOOR(9500000 + (u.user_id * 27183 % 1500000)) * 0.4
        ELSE FLOOR(7000000 + (u.user_id * 31415 % 2500000)) * 0.5
    END AS balance,
    CASE 
        WHEN u.user_id % 1000 <= 200 THEN 11000000 + (u.user_id * 37891 % 2000000)
        WHEN u.user_id % 1000 <= 800 THEN 9500000 + (u.user_id * 27183 % 1500000)
        ELSE 7000000 + (u.user_id * 31415 % 2500000)
    END AS total_asset,
    DATE_SUB(NOW(), INTERVAL (u.user_id % 15) DAY) AS updated_at
FROM users u
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND u.user_id > (SELECT MIN(user_id) + 499 FROM users WHERE email LIKE 'dummy+%@etfinder.dev');

SELECT CONCAT('✅ 지갑 ', COUNT(*), '개 생성 완료') AS result
FROM wallet WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 5. 모의투자 보유 종목 생성 (~3000개)
-- ==========================================
SELECT '📈 보유 종목 생성 중...' AS status;

INSERT INTO holdings (user_id, etf_id, quantity, average_price, created_at, updated_at)
SELECT 
    u.user_id,
    e.etf_id,
    FLOOR(1 + ((u.user_id * e.etf_id * 17) % 50)) AS quantity,
    FLOOR(e.current_price * (0.8 + ((u.user_id * e.etf_id * 23) % 30) / 100.0)) AS average_price,
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + ((u.user_id * e.etf_id) % 14)) DAY) AS created_at,
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + ((u.user_id * e.etf_id) % 14)) DAY) AS updated_at
FROM users u
CROSS JOIN etf_product e
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND e.current_price IS NOT NULL
  AND e.current_price > 0
  AND ((u.user_id * e.etf_id * 13) % 100) < 30
LIMIT 3000;

SELECT CONCAT('✅ 보유 종목 ', COUNT(*), '개 생성 완료') AS result
FROM holdings WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 6. 거래 내역 생성 (~5000건)
-- ==========================================
SELECT '💸 거래 내역 생성 중...' AS status;

-- 6-1. 매수 거래 (보유 종목 기반)
INSERT INTO trade_history (user_id, etf_id, trade_type, price, quantity, amount, created_at)
SELECT 
    h.user_id,
    h.etf_id,
    'BUY',
    h.average_price,
    h.quantity,
    (h.average_price * h.quantity),
    h.created_at
FROM holdings h
WHERE h.user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- 6-2. 추가 거래 (활동성 높은 유저)
INSERT INTO trade_history (user_id, etf_id, trade_type, price, quantity, amount, created_at)
SELECT 
    u.user_id,
    e.etf_id,
    IF((u.user_id * e.etf_id * 19) % 2 = 0, 'BUY', 'SELL'),
    e.current_price,
    FLOOR(1 + ((u.user_id * e.etf_id * 29) % 20)),
    e.current_price * FLOOR(1 + ((u.user_id * e.etf_id * 29) % 20)),
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + ((u.user_id * e.etf_id * 31) % 14)) DAY)
FROM users u
CROSS JOIN etf_product e
WHERE u.email LIKE 'dummy+%@etfinder.dev'
  AND e.current_price IS NOT NULL
  AND e.current_price > 0
  AND (u.user_id % 10) < 4
  AND ((u.user_id * e.etf_id * 37) % 100) < 15
LIMIT 2000;

SELECT CONCAT('✅ 거래 내역 ', COUNT(*), '건 생성 완료') AS result
FROM trade_history WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 7. 자산 스냅샷 생성 (~7000건)
-- ==========================================
SELECT '📊 자산 스냅샷 생성 중...' AS status;

INSERT INTO wallet_daily_snapshot (user_id, base_datetime, total_asset, realized_profit, created_at)
SELECT 
    w.user_id,
    DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-%d 23:00:00'), INTERVAL day_offset DAY),
    FLOOR(w.total_asset * (0.95 + ((w.user_id * day_offset * 17) % 10) / 100.0)),
    0,
    DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-%d 23:00:00'), INTERVAL day_offset DAY)
FROM wallet w
CROSS JOIN (
    SELECT 0 AS day_offset UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL 
    SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6
) days
WHERE w.user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

SELECT CONCAT('✅ 자산 스냅샷 ', COUNT(*), '건 생성 완료') AS result
FROM wallet_daily_snapshot WHERE user_id IN (SELECT user_id FROM users WHERE provider = 'DEMO');

-- ==========================================
-- 트랜잭션 커밋
-- ==========================================
COMMIT;

-- ==========================================
-- 시딩 완료 메시지
-- ==========================================
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
SELECT '✅ 대규모 더미 데이터 시딩 완료!' AS '';
SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';

SELECT 
    '항목' AS 항목,
    '개수' AS 개수
UNION ALL
SELECT 
    'Group A 유저',
    CAST(COUNT(*) AS CHAR)
FROM users WHERE email LIKE 'dummy+%@etfinder.dev'
UNION ALL
SELECT 
    'Group B 유저',
    CAST(COUNT(*) AS CHAR)
FROM users WHERE email LIKE 'reviewer+%@etfinder.dev'
UNION ALL
SELECT 
    'ETF 360750 한줄평',
    CAST(COUNT(*) AS CHAR)
FROM comments WHERE etf_id = 622
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

SELECT '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━' AS '';
