#Blog Search Project
> Blog 검색용 api 서버 프로젝트
## 개발환경
- Java: 17
- Spring Boot: 3.0.4
***
## 실행 방법
### 1. Redis 실행
- docker run -d -p 6379:6379 --name blog-test-redis redis:7.0.10-alpine

### 2. Application jar file 다운로드
- https://drive.google.com/file/d/1OUXrkZw-UC-8zKOGrJG7awBsBVpEJ5Xt/view?usp=sharing

### 3. Application 실행
- java -jar blog-search-api-0.0.1-SNAPSHOT.jar
***
## API 명세

http://localhost:8080/api/blog/search?query=test - 블로그 검색

http://localhost:8080/api/blog/search/keyword/rank - 랭킹 조회

### 1. Blog 검색
- URL: /api/blog/search
- Method: GET
- Request Parameter
    - |query parameter|description|비고|
      |------|---|---|
      |query|검색 키워드|**필수**|
      |sort|정렬 방식|accuracy: 정확도순 (기본), recency: 최신순|
      |page|페이지 번호|기본값: 1|
      |size|페이지 사이즈|기본값: 10|

### 2. Ranking 조회
- URL: /api/blog/search/keyword/rank
- Method: GET
- Request Parameter
  - |query parameter|description|비고|
    |------|---|---|
    |size|조회할 ranking 수|10 ~ 1000 (기본: 10)|

***
## 추가 의존성
- Lombok: 1.18.26
  - 반복적인 보일러 플레이트 코드 작업 제거
- TestContainer: 1.17.6
  - 통합 테스트 시, Redis 컨테이너를 테스트 환경으로 실행
- spring-data-redis: 3.0.4
  - 검색어 저장 및 검색어 count 증가는 I/O Bound 작업으로 판단
  - 따라서, 관계형 데이터베이스보다 Redis 를 사용하는 것이 더 적합하다고 판단
- openfeign: 12.1
  - 카카오 블로그 검색 api 호출을 위한 client


