# ETFinder 더미 데이터 시딩 가이드

## 📋 개요

ETFinder 프로젝트의 **한줄평 기능**과 **모의투자 랭킹 기능** 데모를 위한 대규모 더미 데이터 생성 가이드입니다.

---

## 🎯 생성되는 더미 데이터

### Group A: 랭킹용 유저 (1000명)
- **닉네임**: `demo_0001` ~ `demo_1000`
- **Email**: `dummy+0001@etfinder.dev` ~ `dummy+1000@etfinder.dev`
- **용도**: 모의투자 랭킹 대규모 데이터

### Group B: 리뷰 작성자 (20명)
- **닉네임**: 실제 커뮤니티 스타일 (예: 우직한종목시세보스, 물리면버틴다, 장마감에운다 등)
- **Email**: `reviewer+001@etfinder.dev` ~ `reviewer+020@etfinder.dev`
- **용도**: ETF 360750 한줄평 작성

### 한줄평 (20개)
- **대상 ETF**: 360750 (TIGER 미국S&P500)
- **감성 분포**: 긍정 70%, 중립 15%, 부정 15%
- **특징**: 짧은/긴 리뷰 혼재, 풍자적 톤

### 모의투자 데이터
- **지갑**: 1000개
- **자산 스냅샷**: 8000건 (최근 7일)
- **랭킹 분포**: Top 20% (+10~30%), Mid 60% (-5~+10%), Bottom 20% (-30~-5%)

---

## 🚀 실행 방법 (3단계)

### Step 1: 대규모 시딩 실행

```bash
# 프로젝트 루트 디렉토리로 이동
cd /path/to/ETFinder

# 시딩 실행 (약 10~30초 소요)
mysql -u ssafy -pssafy etfinder < backend/src/main/resources/seed_large_scale_dummy_data.sql
```

**예상 출력:**
```
✅ Group A 유저 1000명 생성 완료
✅ Group B 유저 20명 생성 완료
✅ ETF 360750 한줄평 20개 생성 완료
✅ 지갑 1000개 생성 완료
✅ 자산 스냅샷 7000건 생성 완료
```

---

### Step 2: 검증 (선택사항)

```bash
mysql -u ssafy -pssafy etfinder < backend/src/main/resources/verify_large_scale_data.sql
```

**검증 내용:**
- ✅ 전체 유저 수: 1020명
- ✅ ETF 360750 한줄평: 20개
- ✅ 감성 분포: 긍정 70%, 중립 15%, 부정 15%
- ✅ 모의투자 랭킹 Top 10
- ✅ 데이터 무결성

---

### Step 3: 프론트엔드 확인

1. **ETF 360750 상세 페이지**:
   - 한줄평 섹션 확인
   - 작성자 닉네임 확인 (실제 커뮤니티 스타일)

2. **모의투자 랭킹 페이지**:
   - 랭킹 Top 10 확인
   - 수익률 분포 확인

---

## 🔄 재시딩 (데이터 초기화 후 재생성)

기존 더미 데이터를 삭제하고 새로 생성하려면:

```bash
# 1. 기존 더미 데이터 삭제 (하드 리셋)
# 주의: 이 스크립트는 현재 프로젝트에 없으므로 수동으로 삭제하거나
# seed_large_scale_dummy_data.sql을 재실행하면 됩니다.

# 2. 재시딩
mysql -u ssafy -pssafy etfinder < backend/src/main/resources/seed_large_scale_dummy_data.sql
```

---

## ⚠️ 주의사항

### 1. MySQL 설정 확인
```bash
# application.properties 확인
spring.datasource.username=ssafy
spring.datasource.password=ssafy
spring.datasource.url=jdbc:mysql://localhost:3306/etfinder
```

### 2. 인코딩 설정
한글 데이터 깨짐 방지를 위해 UTF-8 인코딩 사용:
```bash
mysql -u ssafy -pssafy --default-character-set=utf8mb4 etfinder < backend/src/main/resources/seed_large_scale_dummy_data.sql
```

### 3. 보유 종목 및 거래 내역
현재 ETF 데이터 부족으로 보유 종목과 거래 내역이 0건입니다.
- **영향**: 랭킹과 한줄평 기능은 정상 작동
- **해결**: ETF 데이터 import 후 재시딩

---

## 📊 예상 결과

### 전체 유저 수
- Group A: 1000명
- Group B: 20명
- **합계**: 1020명

### 랭킹 Top 5 예시
```
1. demo_0047 - 12,986,778원 (+29.87%)
2. demo_0986 - 12,983,228원 (+29.83%)
3. demo_0933 - 12,975,005원 (+29.75%)
4. demo_0880 - 12,966,782원 (+29.67%)
5. demo_0046 - 12,948,887원 (+29.49%)
```

### Group B 닉네임 예시
```
우직한종목시세보스, 물리면버틴다, 장마감에운다, 수익좀주라,
존버는승리한다, 국장탈출기원, 개미는운다, 배당만먹고간다,
손절은없다, 분할매수신봉자, 파란불만본다, 물타기장인,
반등노리는개미, 손익률마이너스, 장투만믿는다, 평단가내려라,
매수타이밍놓침, 빚투는위험해, 월급쟁이투자왕, 계좌빨개요
```

---

## 🛡️ 안전성 보장

### 실제 유저 데이터 보호
- 더미 유저는 `provider = 'DEMO'`로 식별
- 실제 Kakao OAuth 유저는 `provider = 'KAKAO'`
- 삭제 시 `provider = 'DEMO'` 조건으로만 삭제

### 트랜잭션 보장
```sql
START TRANSACTION;
-- 모든 INSERT 작업
COMMIT;
```
- 오류 발생 시 자동 롤백
- 데이터 일관성 보장

---

## 📁 관련 파일

| 파일 | 용도 |
|------|------|
| `seed_large_scale_dummy_data.sql` | 대규모 시딩 스크립트 |
| `verify_large_scale_data.sql` | 검증 쿼리 |
| `verify_dummy_data.sql` | 기본 검증 쿼리 (레거시) |
| `schema.sql` | 테이블 스키마 정의 |
| `README_SEEDING.md` | 이 파일 |

---

## 🔧 문제 해결

### Q1. "command not found: mysql"
**해결**: MySQL 클라이언트 PATH 추가
```bash
export PATH=$PATH:/usr/local/mysql/bin
```

### Q2. "Access denied for user 'ssafy'"
**해결**: 비밀번호 확인 또는 권한 부여
```bash
mysql -u root -p
GRANT ALL PRIVILEGES ON etfinder.* TO 'ssafy'@'localhost';
FLUSH PRIVILEGES;
```

### Q3. "Unknown database 'etfinder'"
**해결**: 데이터베이스 생성
```bash
mysql -u root -p
CREATE DATABASE etfinder DEFAULT CHARACTER SET utf8mb4;
```

### Q4. 한글 깨짐
**해결**: UTF-8 인코딩 지정
```bash
mysql -u ssafy -pssafy --default-character-set=utf8mb4 etfinder < backend/src/main/resources/seed_large_scale_dummy_data.sql
```

---

## 🎉 완료!

이제 ETFinder 프로젝트의 한줄평 및 모의투자 랭킹 기능을 대규모 데이터로 데모할 수 있습니다!

**문의사항이 있으시면 팀 채널에 공유해주세요.** 🚀
