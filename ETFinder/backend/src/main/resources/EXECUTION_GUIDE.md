# 🚀 더미 데이터 시딩 실행 가이드 (단계별)

## 📋 사전 확인사항

### 1. MySQL 설정 확인
현재 프로젝트 설정:
- **데이터베이스**: `etfinder`
- **호스트**: `localhost:3306`
- **사용자**: `ssafy`
- **비밀번호**: `ssafy`

---

## 🎯 실행 방법 (3가지 옵션)

### ✅ **방법 1: 터미널에서 직접 실행 (추천)**

#### Step 1: 터미널 열기
- **macOS**: `Cmd + Space` → "터미널" 입력 → Enter
- 또는 VS Code 내장 터미널: `Ctrl + ` (백틱)

#### Step 2: 프로젝트 디렉토리로 이동
```bash
cd /Users/minjeong/Desktop/etfinder/ETFinder
```

#### Step 3: MySQL 접속 및 시딩 실행
```bash
mysql -u ssafy -p etfinder < backend/src/main/resources/seed_dummy_data.sql
```

#### Step 4: 비밀번호 입력
```
Enter password: ssafy
```
(입력 시 화면에 표시되지 않음 - 정상입니다)

#### Step 5: 완료 확인
성공 시 다음과 같은 메시지가 출력됩니다:
```
status
✅ 더미 데이터 시딩 완료!

result
생성된 더미 유저: 50명

result
생성된 한줄평: 190개

result
생성된 지갑: 50개

result
생성된 보유 종목: 150개

result
생성된 거래 내역: 250건
```

---

### 방법 2: MySQL Workbench 사용

#### Step 1: MySQL Workbench 실행

#### Step 2: 연결 설정
- **Connection Name**: ETFinder
- **Hostname**: localhost
- **Port**: 3306
- **Username**: ssafy
- **Password**: ssafy (Store in Keychain 클릭)

#### Step 3: 연결 후 스키마 선택
```sql
USE etfinder;
```

#### Step 4: SQL 파일 열기
- 메뉴: `File` → `Open SQL Script...`
- 파일 선택: `/Users/minjeong/Desktop/etfinder/ETFinder/backend/src/main/resources/seed_dummy_data.sql`

#### Step 5: 실행
- `Ctrl + Shift + Enter` (전체 실행)
- 또는 ⚡ 번개 아이콘 클릭

---

### 방법 3: DBeaver 사용

#### Step 1: DBeaver 실행

#### Step 2: 새 연결 생성
- Database → New Database Connection
- MySQL 선택

#### Step 3: 연결 정보 입력
- **Host**: localhost
- **Port**: 3306
- **Database**: etfinder
- **Username**: ssafy
- **Password**: ssafy

#### Step 4: SQL 파일 열기
- 좌측 Navigator에서 `etfinder` 우클릭
- `SQL Editor` → `Open SQL Script...`
- 파일 선택: `seed_dummy_data.sql`

#### Step 5: 실행
- `Ctrl + Alt + X` (전체 실행)
- 또는 상단 실행 버튼 클릭

---

## 🔍 검증 (선택사항)

### 터미널에서 검증
```bash
mysql -u ssafy -p etfinder < backend/src/main/resources/verify_dummy_data.sql
```

### 또는 간단 확인 쿼리
```bash
# MySQL 접속
mysql -u ssafy -p etfinder

# 접속 후 실행
SELECT COUNT(*) AS 더미유저수 FROM users WHERE email LIKE 'dummy+%@etfinder.dev';
SELECT COUNT(*) AS 한줄평수 FROM comments WHERE user_id IN (SELECT user_id FROM users WHERE email LIKE 'dummy+%@etfinder.dev');
```

---

## 🔄 롤백 (더미 데이터 삭제)

### 터미널에서 롤백
```bash
mysql -u ssafy -p etfinder < backend/src/main/resources/rollback_dummy_data.sql
```

---

## ⚠️ 문제 해결

### 문제 1: "command not found: mysql"
**원인**: MySQL 클라이언트가 PATH에 없음

**해결 방법 A - PATH 추가 (임시):**
```bash
export PATH=$PATH:/usr/local/mysql/bin
```

**해결 방법 B - 전체 경로 사용:**
```bash
/usr/local/mysql/bin/mysql -u ssafy -p etfinder < backend/src/main/resources/seed_dummy_data.sql
```

**해결 방법 C - MySQL Workbench 사용** (위 방법 2 참고)

---

### 문제 2: "Access denied for user 'ssafy'@'localhost'"
**원인**: 비밀번호 오류 또는 사용자 권한 없음

**해결 방법:**
```bash
# root로 접속
mysql -u root -p

# ssafy 사용자 권한 확인 및 부여
GRANT ALL PRIVILEGES ON etfinder.* TO 'ssafy'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

---

### 문제 3: "Unknown database 'etfinder'"
**원인**: 데이터베이스가 생성되지 않음

**해결 방법:**
```bash
# root로 접속
mysql -u root -p

# 데이터베이스 생성
CREATE DATABASE etfinder DEFAULT CHARACTER SET utf8mb4;
EXIT;

# schema.sql 먼저 실행
mysql -u ssafy -p etfinder < backend/src/main/resources/schema.sql

# 그 다음 시딩 실행
mysql -u ssafy -p etfinder < backend/src/main/resources/seed_dummy_data.sql
```

---

### 문제 4: "Duplicate entry" 오류
**원인**: 이미 더미 데이터가 존재함

**해결 방법:**
```bash
# 먼저 롤백
mysql -u ssafy -p etfinder < backend/src/main/resources/rollback_dummy_data.sql

# 그 다음 재시딩
mysql -u ssafy -p etfinder < backend/src/main/resources/seed_dummy_data.sql
```

---

### 문제 5: 한글 깨짐
**원인**: 인코딩 설정 문제

**해결 방법:**
```bash
mysql -u ssafy -p --default-character-set=utf8mb4 etfinder < backend/src/main/resources/seed_dummy_data.sql
```

---

## 📊 실행 후 확인

### 프론트엔드에서 확인
1. **한줄평 확인**:
   - ETF 상세 페이지 접속
   - 한줄평 섹션에서 더미 리뷰 확인
   - 작성자: "데모유저01", "데모유저02" 등

2. **모의투자 랭킹 확인**:
   - 모의투자 페이지 접속
   - 랭킹 탭 클릭
   - Top 10 유저 확인 (데모유저XX)

### MySQL에서 직접 확인
```sql
-- 더미 유저 확인
SELECT * FROM users WHERE email LIKE 'dummy+%@etfinder.dev' LIMIT 5;

-- 한줄평 확인
SELECT u.nickname, c.content, c.sentiment, c.created_at 
FROM comments c
JOIN users u ON c.user_id = u.user_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
ORDER BY c.created_at DESC
LIMIT 10;

-- 랭킹 확인
SELECT 
    RANK() OVER (ORDER BY w.total_asset DESC) AS 순위,
    u.nickname AS 닉네임,
    FORMAT(w.total_asset, 0) AS 총자산,
    CONCAT(ROUND((w.total_asset - 10000000) * 100.0 / 10000000, 2), '%') AS 수익률
FROM wallet w
JOIN users u ON w.user_id = u.user_id
WHERE u.email LIKE 'dummy+%@etfinder.dev'
ORDER BY w.total_asset DESC
LIMIT 10;
```

---

## ✅ 체크리스트

시딩 전:
- [ ] MySQL 서버 실행 중
- [ ] `etfinder` 데이터베이스 존재
- [ ] `etf_product` 테이블에 ETF 데이터 존재
- [ ] 기존 더미 데이터 없음 (또는 롤백 완료)

시딩 실행:
- [ ] 터미널에서 명령어 실행
- [ ] 비밀번호 입력 (`ssafy`)
- [ ] 성공 메시지 확인

시딩 후:
- [ ] `verify_dummy_data.sql`로 검증
- [ ] 프론트엔드에서 한줄평 확인
- [ ] 프론트엔드에서 랭킹 확인

---

## 🎉 완료!

이제 ETFinder 프로젝트의 한줄평 및 모의투자 랭킹 기능을 실제 데이터처럼 데모할 수 있습니다!

**문의사항이 있으시면 언제든지 연락 주세요.** 🚀
