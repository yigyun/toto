# Toto Market


## 🖥️ 프로젝트 소개 및 개요
 - 온라인 경매 사이트 제작, 기존 게시판 기능을 활용
 - 복권 방식의 경매 기능 추가: 로또 번호 추천 기능 탑재 예정
 - 개인 프로젝트를 통해 웹 개발 실력 향상 및 새로운 경매 방식 만들어보기

#### 개인 프로젝트 목표
 - 웹 개발 실력 검증 및 아이디어 구현

## 📌 주요 기능

#### Member [(상세정보)](./info/member.md)
- 북마크: 관심 있는 경매를 모아서 확인
- 로그인: 스프링 시큐리티, OAuth2.0 기반의 인증, 인가 (jwt 혹은 keycloak 기반 인증 서버 방식으로 전환 예정)
#### Auction [(상세정보)](./info/auction.md)
- 일반 경매: 입찰 금액을 적어서 경매에 참여하는 방식
- 상품 기능(등록, 조회, 수정, 삭제): 다양한 카테고리의 상품을 경매
- 복권 경매: 복권 방식으로 경매에 참여하는 방식(로또 번호 추천 기능 탑재)
- 자동 관리: Spring Batch를 통한 경매 시간을 기반으로 하는 관리
#### etc
- 관리자 페이지: Member, Bid, Board(Item) 관리


## 개발 기간
- 23.11 - 24.03(진행중)

## ⚙️ 개발 환경
- Language : Java 11
- Framework : Spring Boot 2.x (with Spring Security, Spring Data JPA, Spring WebSocket)
- Query Language : QueryDSL
- Database : MySQL (Production), H2 (Test)
- ORM : JPA
- Authentication : OAuth2
- Image Processing : Thumbnailator
- API Documentation : Swagger
- Build Tool : Gradle
- IDE : IntelliJ IDEA
- Version Control : Git, GitHub
- Containerization : Docker
- View : thymeleaf, axios, javascript, tailwind 등

## 내역

* 0.0.1 (2023-11-05)
    * 로그인 화면, 기능 완성
* 0.0.2 (2023-12-05)
    * 기본 게시판(CRUD) 완성
* 0.0.3 (2024-01-04)
    * V1 완성(경매, 게시판, 북마크, 로그인, 회원가입)
* 0.0.4 (2024-01-10)
    * 패키지 구조 변경
* 0.0.5 (2024-01-17)
    * View 수정 및 보완
* 0.0.6 (2024-01-10)
    * Exception Handling 추가
* 0.0.7 (2024-02-02)
    * 테스트 코드 작성 완료
* 0.0.8 (2024-02-07)
    * DB (H2 -> MySql)
* 0.0.9 (2024-02-18)
    * Docker 적용
      - DB, 서버 각각 컨테이너로 분리 후 동작
* 0.0.10 (2024-01-10)
    * yml 파일 분리, .gitignore 관리 및 GitHub Secret으로 정보 관리
* 0.0.11 (예정)
    *  
