CREATE DATABASE etfinder DEFAULT CHARACTER SET utf8mb4;

USE etfinder;

-- ==========================================
-- 1. 회원 (Users)
-- ==========================================
CREATE TABLE users (
    user_id      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 고유 ID',
    email        VARCHAR(100) UNIQUE COMMENT '이메일',
    nickname     VARCHAR(50) NOT NULL COMMENT '닉네임',
    age          INT COMMENT '나이 (추천 가중치용)',
    provider     VARCHAR(20) COMMENT '가입 경로 (KAKAO, GOOGLE)',
    provider_id  VARCHAR(255) COMMENT '소셜 식별값 (영어 ID)',
    propensity   VARCHAR(20) COMMENT '투자성향 (AGGRESSIVE, STABLE 등)',
    created_at   DATETIME DEFAULT NOW() COMMENT '가입일',
    
    -- provider + provider_id는 유니크해야 함 (중복 가입 방지)
    UNIQUE KEY uk_provider (provider, provider_id)
);

-- ==========================================
-- 2. ETF 상품 (Etf Product) - *view_count 제거됨*
-- ==========================================
CREATE TABLE etf_product (
    etf_id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ETF 고유 ID',
    etf_code      VARCHAR(20) UNIQUE NOT NULL COMMENT '종목코드 (검색용)',
    etf_name      VARCHAR(100) NOT NULL COMMENT '종목명',
    market        VARCHAR(10) COMMENT '시장 (KOR, USA)',
    theme         VARCHAR(50) COMMENT '테마 (반도체, 배당 등)',
    fee           DOUBLE COMMENT '총보수',
    risk_rating   INT COMMENT '위험등급 (1~5)',
    aum           BIGINT COMMENT '시가총액',
    current_price INT COMMENT '현재가 (매일 갱신)',
    
    -- 수익률 데이터
    return_1mo    DOUBLE,
    return_3mo    DOUBLE,
    return_6mo    DOUBLE,
    return_1yr    DOUBLE COMMENT '1년 수익률 (추천 핵심)',
    return_3yr    DOUBLE,
    
    description   TEXT COMMENT 'AI 요약 설명',
    created_at    DATETIME DEFAULT NOW(),
    
    -- 좋아요 수 
    like_count INT NOT NULL DEFAULT 0
);

-- ==========================================
-- 3. 찜 목록 / 포트폴리오 (Bookmark)
-- ==========================================
CREATE TABLE bookmark (
    bookmark_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    etf_id       BIGINT NOT NULL,
    created_at   DATETIME DEFAULT NOW(),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    -- 한 유저가 같은 종목을 중복 찜하는 것 방지
    UNIQUE KEY uk_bookmark (user_id, etf_id)
);

-- ==========================================
-- 4. 한줄평 댓글 (Comments)
-- ==========================================
CREATE TABLE comments (
    comment_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT,
    etf_id       BIGINT NOT NULL,
    content      VARCHAR(200) NOT NULL COMMENT '내용 200자 제한',
    sentiment    VARCHAR(10) COMMENT 'AI 감성분석 결과 (POSITIVE/NEGATIVE)',
    
    created_at   DATETIME DEFAULT NOW() COMMENT '최초 작성 시간',
    updated_at   DATETIME DEFAULT NOW() COMMENT '사용자 수정 시간',
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE
);


-- ==========================================
-- 5. 좋아요 (Likes)
-- ==========================================
CREATE TABLE likes (
    like_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    etf_id       BIGINT NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    -- 중복 좋아요 방지
    UNIQUE KEY uk_likes (user_id, etf_id)
);

-- ==========================================
-- 6. 검색 로그 (Search Log)
-- ==========================================
CREATE TABLE search_log (
    search_log_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT COMMENT '비회원이면 NULL 가능',
    keyword          VARCHAR(100) COMMENT '검색어',
    created_at       DATETIME DEFAULT NOW()
);

-- ==========================================
-- 7. [모의투자] 가상 계좌 (Wallet)
-- ==========================================
CREATE TABLE wallet (
    user_id      BIGINT PRIMARY KEY COMMENT 'User ID와 1:1 매핑',
    balance      BIGINT DEFAULT 10000000 COMMENT '가용 잔액 (기본 1,000만)',
    total_asset  BIGINT DEFAULT 10000000 COMMENT '총자산 (잔액+평가금)',
    updated_at   DATETIME DEFAULT NOW() ON UPDATE NOW(),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==========================================
-- 8. [모의투자] 보유 종목 (Holdings)
-- ==========================================
CREATE TABLE holdings (
    holding_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    etf_id        BIGINT NOT NULL,
    quantity      INT NOT NULL DEFAULT 0 COMMENT '보유 수량',
    average_price INT NOT NULL DEFAULT 0 COMMENT '평단가',
    created_at    DATETIME DEFAULT NOW(),
    updated_at    DATETIME DEFAULT NOW() ON UPDATE NOW(),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    -- 한 유저가 같은 종목을 여러 행 가지지 않도록 유니크
    UNIQUE KEY uk_holdings (user_id, etf_id)
);

-- ==========================================
-- 9. [모의투자] 거래 내역 (Trade History)
-- ==========================================
CREATE TABLE trade_history (
    trade_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    etf_id       BIGINT NOT NULL,
    trade_type   ENUM('BUY', 'SELL') NOT NULL COMMENT '매수/매도',
    price        INT NOT NULL COMMENT '체결 단가',
    quantity     INT NOT NULL COMMENT '체결 수량',
    amount       BIGINT NOT NULL COMMENT '총 거래액 (price * quantity)',
    created_at   DATETIME DEFAULT NOW(),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE
);

-- ==========================================
-- 10. ETF 통계 정보 (Statistics) - *Hot Data 분리*
-- ==========================================
CREATE TABLE etf_statistics (
    stat_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    etf_id         BIGINT NOT NULL,
    view_count     BIGINT DEFAULT 0 COMMENT '조회수',
--    comment_count  BIGINT DEFAULT 0 COMMENT '댓글 수 (캐싱)',
    updated_at     DATETIME DEFAULT NOW() ON UPDATE NOW(),
    
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    -- ETF 하나당 통계 row는 하나만!
    UNIQUE KEY uk_etf_stats (etf_id)
);

-- ==========================================
-- 11. ETF 구성종목 (Holdings / PDF)
-- ==========================================
CREATE TABLE etf_holdings (
    holding_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    etf_id         BIGINT NOT NULL COMMENT 'etf_product 테이블 참조',
    stock_code     VARCHAR(20) NOT NULL COMMENT '종목코드 (예: 005930)',
    stock_name     VARCHAR(100) NOT NULL COMMENT '종목명 (예: 삼성전자)',
    weight         DECIMAL(5, 2) NOT NULL COMMENT '구성비중 % (예: 25.45)',
    stock_price    INT COMMENT '기준가/전일종가 (단순 참고용)',
    updated_at     DATETIME DEFAULT NOW() COMMENT '배치 돌 때마다 갱신됨',

    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    
    -- 한 ETF 안에서 같은 종목이 두 번 들어갈 순 없음
    UNIQUE KEY uk_etf_stock (etf_id, stock_code)
);

-- ==========================================
-- 12. ETF 과거 시세 (Daily Price History)
-- 차트 그리기에 필요한 일별 데이터 저장소
-- ==========================================
CREATE TABLE etf_daily_history (
    history_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    etf_id       BIGINT NOT NULL COMMENT 'etf_product 테이블 참조',
    base_date    DATE NOT NULL COMMENT '기준 날짜 (YYYY-MM-DD)',
    
    -- 차트 데이터 (OHLCV)
    close_price  BIGINT NOT NULL COMMENT '종가 (라인 차트의 기준)',
    open_price   BIGINT COMMENT '시가 (캔들 차트용)',
    high_price   BIGINT COMMENT '고가 (캔들 차트용)',
    low_price    BIGINT COMMENT '저가 (캔들 차트용)',
    volume       BIGINT COMMENT '거래량 (보조 지표용)',
    
    created_at   DATETIME DEFAULT NOW() COMMENT '데이터 수집 시점',
    
    FOREIGN KEY (etf_id) REFERENCES etf_product(etf_id) ON DELETE CASCADE,
    
    -- [중요] 한 ETF에 같은 날짜 데이터가 중복으로 쌓이는 것 방지
    -- 조회 성능 향상 (Index 역할 겸용)
    UNIQUE KEY uk_etf_history (etf_id, base_date)
);
