# BoardService
JPA와 QueryDSL을 활용하여 구현한 CRUD 게시판

# Feature
- 사용자
  - ID, PW 회원가입
  - 일반 로그인, 소셜 로그인(카카오)
- 게시판
  - 게시글 및 댓글 CRUD
  - 키워드 검색 (제목, 내용, 작성자)
  - 게시판 목록 페이징
  - 게시판 목록 댓글 개수 보여주기
    
## 개발 기능

- 로그인 : ID, PW 일치 여부 확인
- 자동 로그인 : 자동 로그인 체크박스 선택 후 로그인 시 rememberme 토큰을 사용하여 사용자 로그인 유지 
- 키워드 검색 : QueryDSL을 사용하여 키워드 조회 동적 쿼리 구현
- 게시글 CRUD
- 댓글 CRUD

 # Tech 
 - Java 11/ Spring Boot v2.7.14
 - Gradle
 - MySQL (DB)
   
# Dependency

- SPring Web
- Spring Data JPA
- QueryDsl
- Validation
- Spring Security
- OAuth2 Client
- Thymeleaf
- Lombok
- MySQL Driver

# EER Diagram

![image](https://github.com/1hyunjin/BoardService/assets/38430900/bb4614d4-903c-46cf-9813-a3ab56106aba)

# API Spec

![image](https://github.com/1hyunjin/BoardService/assets/38430900/ae36ae29-3cfd-4218-ba39-33303b24fdcc)

![image](https://github.com/1hyunjin/BoardService/assets/38430900/d426e002-e8ac-4966-ad86-77acf36512c4)

![image](https://github.com/1hyunjin/BoardService/assets/38430900/f1974041-8fe5-41aa-bb5b-9f936602e734)

# Details
### 게시글 목록

![image](https://github.com/1hyunjin/BoardService/assets/38430900/46024adc-92d9-44b6-9fa8-bd66ba39d407)

### 키워드 검색

![image](https://github.com/1hyunjin/BoardService/assets/38430900/7573c993-00e5-4797-b125-ea35a0f2c532)


### 게시글 상세 

![image](https://github.com/1hyunjin/BoardService/assets/38430900/33870d8b-54df-4b7a-a6b4-7f63073426f0)

### 글 작성 

![image](https://github.com/1hyunjin/BoardService/assets/38430900/22ba496c-a0c5-401d-ae7e-5e7c61ddde66)

## 후기

지금까지 차근차근 Spring boot에 대해 공부를 하였고 이를 토대로 JPA 와 QueryDSL을 이해하면서 게시판 CRUD를 구현하였다. 
생각보다 코드를 짜는데 어려움이 많았고 특히 QueryDSL로 동적쿼리를 짜는 부분이 재미있었다. 
QueryDSL은 자바 코드로 쿼리를 작성하므로 코드 가독성이 JPQL보다 쓰기 편했다. 

## 개선할 점 

기본적인 로그인 구현은 하였지만 일반 로그인과 oAuth2를 이용한 로그인 시 JWT를 사용하여 access token과 refresh token을 구분하여 token을 생성 및 유효성을 검사 그리고 DB에 저장할 수 있는 기능을 구현하고자 한다. 

구현 목적 : 보안 강화 및 Stateless 서비스를 가능하게 하여 유연한 로그인 시스템을 만들기 위함. 

-> 구현 완료 : https://github.com/1hyunjin/OAuth2-JWT-Login

