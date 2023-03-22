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
- TestContainer: 1.17.6
- spring-data-redis: 3.0.4


