# BoardService
개인 프로젝트로 Spring에 대해 학습 후 학습한 내용을 바탕으로 구현한 게시판 서비스입니다.
JPA와 QueryDSL을 활용하여 구현한 웹의 기본 CRUD 게시판입니다. 

# 주요 기능
- 사용자
  - Security 회원가입 및 로그인, oAuth2.0 로그인(카카오)
  - 회원 정보 중복 검사
- 게시판
  - 게시글 CRUD 기능
  - 키워드 검색 (제목, 내용, 작성자)
  - 게시판 목록 페이징
  - 게시판 목록에 댓글 개수 보여주기
- 댓글
  - CURD 기능
    
## 개발 기능
1. 회원 가입
  - 중복 확인
    - 중복 ID 입력 시 "이미 사용중인 ID 입니다" 메시지
2. 로그인
  - 인증 확인
    - ID, PW 일치 여부 확인
  - 자동 로그인
    - 자동 로그인 체크박스 선택 후 로그인 시 rememberme 토큰을 사용하여 사용자 로그인 유지
3. 게시판
  - 키워드 검색 : QueryDSL을 사용하여 키워드 조회 동적 쿼리 구현

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

# Refactoring 

## 1. Service? ServiceImpl?

지금까지 공부해왔던 내용과는 다르게 BoardService와 BoardServiceImpl 로 인터페이스와 구현 클래스가 나눠져 있다. 사용자 정의 리포지토리에서는 사용하는 것을 알고 있었지만 Service 도 나눠서 구현하는지 오늘 알게 되었다. 

하지만 서비스 인터페이스와 구현 클래스를 나누어 구현하는 것이 의문이 들어 찾아보게 되었다.

**BoardService와 BoardServiceImpl만 해도 1:1 구성인데 굳이 분리하는 것이 필요할까?**

### Service 인터페이스와 ServiceImpl 구현 클래스를 나누는 이유

**다형성과 OCP(Open Closed Principle)**

인터페이스와 구현체가 나눠져있으면 구현체는 외부로부터 독립된다. 이로써 구현체의 수정이나 확장이 자유로워지고, 클라이언트 코드에는 영향을 주지 않는다. 

**AOP Proxy**

과거 Spring에서 AOP를 구현할 때 JDK Dynamic Proxy를 사용했다. 이 JDK Dynamic Proxy는 프록시 객체를 생성하려면 인터페이스가 필수였다. 

하지만 Spring3.2부터는 CGLIB를 기본적으로 포함하여 클래스 기반으로 프록시 객체를 생성할 수 있게 되었다. 

### 결론

무조건 Service 인터페이스와 ServiceImpl를 나누어 구현하기 보단, OCP를 준수하며 구현하는 것이 좋다고 생각한다.

## 2. BookSearchImpl
**QuerydslRepositorySupport → JPAQueryFactory 변경** 

- Querydsl 3.x 버전을 대상으로 만듦
- Querydsl 4.x에 나온 JPAQueryFactory로 시작할 수 없음 (select로 시작 불가)
- QueryFactory를 제공하지 않음
- 스프링 데이터 Sort 기능이 정상 동작하지 않음

## 후기

지금까지 차근차근 Spring boot에 대해 공부를 하였고 이를 토대로 JPA 와 QueryDSL을 이해하면서 게시판 CRUD를 구현하였다. 
생각보다 코드를 짜는데 어려움이 많았고 특히 QueryDSL로 동적쿼리를 짜는 부분이 재미있었다. 
QueryDSL은 자바 코드로 쿼리를 작성하므로 코드 가독성이 JPQL보다 쓰기 편했다. 

## 개선할 점 

기본적인 로그인 구현은 하였지만 일반 로그인과 oAuth2를 이용한 로그인 시 JWT를 사용하여 access token과 refresh token을 구분하여 token을 생성 및 유효성을 검사 그리고 DB에 저장할 수 있는 기능을 구현하고자 한다. 

구현 목적 : 보안 강화 및 Stateless 서비스를 가능하게 하여 유연한 로그인 시스템을 만들기 위함. 

-> 구현 완료 : https://github.com/1hyunjin/OAuth2-JWT-Login

