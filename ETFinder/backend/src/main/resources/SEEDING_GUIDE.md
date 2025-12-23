# ETFinder 더미 데이터 시딩 가이드

## 📋 개요

ETFinder 프로젝트의 **한줄평 기능**과 **모의투자 랭킹 기능** 데모를 위한 더미 데이터 생성 가이드입니다.

---

## 🎯 생성되는 더미 데이터

### 1. 한줄평 기능
- **더미 유저**: 50명 (`dummy+001@etfinder.dev` ~ `dummy+050@etfinder.dev`)
- **한줄평**: 약 190~250개 (ETF당 5~20개, 감성 분포 다양화)
  - 긍정 리뷰: 약 40~50%
  - 부정 리뷰: 약 30~35%
  - 중립/미분석: 약 20~25%
- **타임스탬프**: 최근 30일 분산

### 2. 모의투자 랭킹 기능
- **지갑 (wallet)**: 50개 (유저당 1개)
  - Top 10 (20%): 1,100만원 ~ 1,300만원 (10~30% 수익)
  - Middle 30 (60%): 950만원 ~ 1,100만원 (-5% ~ 10%)
  - Bottom 10 (20%): 700만원 ~ 950만원 (-30% ~ -5%)
- **보유 종목 (holdings)**: 약 150개 (유저당 0~5개 ETF)
- **거래 내역 (trade_history)**: 약 250건 (유저당 3~20건)
- **자산 스냅샷 (wallet_daily_snapshot)**: 약 350건 (유저당 최근 7일)

---

## 🚀 실행 방법

### 1️⃣ 더미 데이터 시딩

```bash
# MySQL/MariaDB 접속 후 실행
mysql -u root -p etfinder < backend/src/main/resources/seed_dummy_data.sql
```

**또는 MySQL Workbench / DBeaver 등 GUI 도구 사용:**
1. `seed_dummy_data.sql` 파일 열기
2. 전체 선택 후 실행 (Ctrl/Cmd + Enter)

**예상 실행 시간**: 약 3~5초

---

### 2️⃣ 검증 (선택사항)

```bash
# 시딩 결과 확인
mysql -u root -p etfinder < backend/src/main/resources/verify_dummy_data.sql
```

**검증 내용:**
- ✅ 테이블별 더미 데이터 개수
- ✅ ETF별 한줄평 개수 및 감성 분포
- ✅ 모의투자 랭킹 Top 10 / Bottom 5
- ✅ 유저별 보유 종목 현황
- ✅ 거래 내역 샘플
- ✅ 데이터 무결성 검증 (FK, UNIQUE 제약 확인)

---

### 3️⃣ 롤백 (더미 데이터 삭제)

```bash
# 더미 데이터만 안전하게 삭제
mysql -u root -p etfinder < backend/src/main/resources/rollback_dummy_data.sql
```

**안전성 보장:**
- ✅ 실제 유저 데이터는 절대 삭제되지 않음
- ✅ `email LIKE 'dummy+%@etfinder.dev'` 패턴으로만 식별
- ✅ 트랜잭션으로 전체 롤백 가능

---

## 📊 더미 데이터 특징

### A. 한줄평 (Comments)

#### 감성 분포 전략
- **ETF별로 다른 감성 비율** (랜덤하지만 일관성 유지)
  - 일부 ETF: 긍정 70~90% (인기 종목)
  - 일부 ETF: 부정 60~80% (부진 종목)
  - 일부 ETF: 균형 40~60% (중립 종목)

#### 리뷰 예시
**긍정 (POSITIVE):**
- "하루 수익이 미쳤네. 나이스 ㅋㅋ 선택 지렸다"
- "이거 진짜 대박이네요 ㅎㅎ"
- "수익률 보고 깜짝 놀랐습니다"

**부정 (NEGATIVE):**
- "하루에 글 한개도 제대로 안올라오는 망한 종목ㅋㅋ"
- "이거 도대체 왜 들고있냐ㅋㅋ"
- "초상집이 따로없네ㅋㅋ"

**중립 (NULL - AI 분석 전):**
- "마지막으로 물어봅니다 내일부터 딱지 없이 진행합니까?"
- "이거 장기로 가져가도 될까요?"
- "관망 중입니다"

---

### B. 모의투자 랭킹 (Mock Investment)

#### 랭킹 계산 로직 (MockRankingMapper.xml)
```sql
SELECT
    u.user_id,
    u.nickname,
    w.total_asset,
    ((w.total_asset - 10000000) * 100.0 / 10000000) AS return_rate,
    RANK() OVER (ORDER BY w.total_asset DESC) AS rank
FROM wallet w
INNER JOIN users u ON w.user_id = u.user_id
ORDER BY w.total_asset DESC
```

#### 수익률 분포
| 구간 | 비율 | 자산 범위 | 수익률 |
|------|------|-----------|--------|
| Top 10 | 20% | 1,100만 ~ 1,300만 | +10% ~ +30% |
| Middle 30 | 60% | 950만 ~ 1,100만 | -5% ~ +10% |
| Bottom 10 | 20% | 700만 ~ 950만 | -30% ~ -5% |

---

## 🔍 데이터 무결성 검증

### 자동 검증 항목
1. ✅ **지갑 없는 더미 유저**: 0명 (모든 유저는 지갑 보유)
2. ✅ **존재하지 않는 ETF 보유**: 0건 (FK 제약 준수)
3. ✅ **존재하지 않는 ETF 댓글**: 0건 (FK 제약 준수)
4. ✅ **중복 보유 종목**: 0건 (UNIQUE 제약 준수)

---

## 🛡️ 안전성 보장

### 1. 실제 유저 데이터 보호
- 더미 유저는 `provider = 'DEMO'`로 식별
- 실제 Kakao OAuth 유저는 `provider = 'KAKAO'`
- 롤백 시 `email LIKE 'dummy+%@etfinder.dev'` 패턴만 삭제

### 2. 트랜잭션 보장
```sql
START TRANSACTION;
-- 모든 INSERT/DELETE 작업
COMMIT;
```
- 오류 발생 시 자동 롤백
- 데이터 일관성 보장

### 3. FK CASCADE 설정
```sql
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
```
- 더미 유저 삭제 시 연관 데이터 자동 삭제
- 고아 레코드 방지

---

## 📝 사용 시나리오

### 시나리오 1: 데모 환경 구축
```bash
# 1. 더미 데이터 시딩
mysql -u root -p etfinder < seed_dummy_data.sql

# 2. 검증
mysql -u root -p etfinder < verify_dummy_data.sql

# 3. 프론트엔드에서 확인
# - ETF 상세 페이지 → 한줄평 확인
# - 모의투자 → 랭킹 확인
```

### 시나리오 2: 데모 후 정리
```bash
# 더미 데이터 삭제
mysql -u root -p etfinder < rollback_dummy_data.sql
```

### 시나리오 3: 반복 테스트
```bash
# 1. 롤백
mysql -u root -p etfinder < rollback_dummy_data.sql

# 2. 재시딩 (다른 랜덤 데이터)
mysql -u root -p etfinder < seed_dummy_data.sql
```

---

## ⚠️ 주의사항

### 1. 인코딩 설정
```sql
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
```
- 한글 데이터 깨짐 방지
- MySQL 클라이언트 인코딩 확인 필요

### 2. 기존 ETF 데이터 필요
- `etf_product` 테이블에 실제 ETF 데이터가 있어야 함
- ETF 데이터 없으면 한줄평/보유종목 생성 안됨

### 3. 실행 순서
```
1. seed_dummy_data.sql (시딩)
2. verify_dummy_data.sql (검증)
3. rollback_dummy_data.sql (롤백)
```

### 4. 중복 실행 시
- `users` 테이블의 UNIQUE 제약으로 인해 오류 발생
- 먼저 `rollback_dummy_data.sql` 실행 후 재시딩

---

## 🔧 커스터마이징

### 더미 유저 수 변경
```sql
-- seed_dummy_data.sql 수정
-- 50명 → 100명으로 변경 시:
INSERT INTO users (...) VALUES
('dummy+001@etfinder.dev', ...),
...
('dummy+100@etfinder.dev', ...);
```

### 한줄평 개수 조절
```sql
-- LIMIT 값 조정
LIMIT 80;  -- 긍정 리뷰 개수
LIMIT 60;  -- 부정 리뷰 개수
LIMIT 50;  -- 중립 리뷰 개수
```

### 수익률 분포 조정
```sql
-- wallet INSERT 쿼리의 CASE 문 수정
WHEN u.user_id % 50 <= 10 THEN 11000000 + ...  -- Top 20%
WHEN u.user_id % 50 <= 40 THEN 9500000 + ...   -- Mid 60%
ELSE 7000000 + ...                              -- Low 20%
```

---

## 📞 문제 해결

### Q1. "Duplicate entry" 오류
**원인**: 이미 더미 데이터가 존재함  
**해결**: 
```bash
mysql -u root -p etfinder < rollback_dummy_data.sql
mysql -u root -p etfinder < seed_dummy_data.sql
```

### Q2. 한글 깨짐
**원인**: 인코딩 설정 문제  
**해결**:
```bash
# MySQL 클라이언트 실행 시 인코딩 지정
mysql -u root -p --default-character-set=utf8mb4 etfinder < seed_dummy_data.sql
```

### Q3. FK 제약 오류
**원인**: `etf_product` 테이블에 데이터 없음  
**해결**: ETF 데이터 먼저 import 후 시딩

### Q4. 랭킹이 제대로 안 나옴
**원인**: `wallet.total_asset` 계산 오류  
**해결**: `verify_dummy_data.sql` 실행하여 데이터 확인

---

## ✅ 체크리스트

시딩 전:
- [ ] MySQL/MariaDB 실행 중
- [ ] `etfinder` 데이터베이스 존재
- [ ] `etf_product` 테이블에 ETF 데이터 존재
- [ ] 기존 더미 데이터 없음 (또는 롤백 완료)

시딩 후:
- [ ] `seed_dummy_data.sql` 실행 성공
- [ ] `verify_dummy_data.sql`로 검증 완료
- [ ] 프론트엔드에서 한줄평 확인
- [ ] 프론트엔드에서 랭킹 확인

---

## 📚 관련 파일

| 파일 | 용도 |
|------|------|
| `seed_dummy_data.sql` | 더미 데이터 생성 |
| `rollback_dummy_data.sql` | 더미 데이터 삭제 |
| `verify_dummy_data.sql` | 시딩 결과 검증 |
| `schema.sql` | 테이블 스키마 정의 |
| `MockRankingMapper.xml` | 랭킹 쿼리 로직 |
| `CommentMapper.xml` | 한줄평 쿼리 로직 |

---

## 🎉 완료!

이제 ETFinder 프로젝트의 한줄평 및 모의투자 랭킹 기능을 실제 데이터처럼 데모할 수 있습니다!

**문의사항이 있으시면 언제든지 연락 주세요.** 🚀
