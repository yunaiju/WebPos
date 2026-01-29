# 일회성 POS 사이트 "WebPos"

### 프로젝트 소개
#### 개요 
- 팝업 스토어, 플리마켓, 단기 행사 등 일시적 판매를 목표로 하는 환경에서 굳이 무거운 POS 앱을 구매하여 사용해야 할까 ?
#### 목적
- 로그인과 설치 없이 간편하게 상품 관리, 운영, 판매 내역 관리만 바로 처리할 수 있는 일회성 POS 사이트 제작
#### 기대효과
- 구매 X, 설치 X, 설정 X, 최소한의 기능으로 간단하고 빠르게 POS 시스템 이용 가능

- 상품 데이터와 결제 내역 등을 Session 단위로 분리 -> 사용자 로그인 없이도 각 Session의 독립적인 POS 사용 가능

- 판매 내역 관리 및 마감 정산 용이

- 인터넷만 있으면 어디서든 POS 사용 가능, 별도의 HW, SW 필요 X

### 기술스택
- Language : JAVA
- Framework : SpringBoot3, Spring Web, Spring Data JPA, Spring Session, Spring Validation
- Template Engine : Thymeleaf, ChatGPT
- Database : MariaDB, JPA, JPQL
- Deployment / DevOps : AWS LightSail (Amazon Linux 2023), Docker,  Docker Compose, Nginx
- HTTPS / SSL : Certbot (Let’s Encrypt)
- Code Test : Junit5
- Version Control : Git / GitHub
- CI/CD : GitHub, GitHub Actions

#### Project Architecture
<img width="811" height="561" alt="webpos architecture" src="https://github.com/user-attachments/assets/a42ec559-e639-494b-b915-7d90f3c6bd92" />

### 주요기능
#### Session 생성 & 관리
- 사이트 접속 시 자동으로 고유 sessionId 발급 - 로그인 불필요

- 세션 별 데이터 저장 – 모든 데이터가 sessionId 참조

#### Session 종료
- 일회성 POS 시스템의 특성 고려 -> 12시간 timeout 설정 

- Session 만료 시, 해당 Session의 모든 데이터 자동 삭제

- Session 만료 후 사이트 접속 시 새 Session 자동 발급

#### 카테고리/상품 CRUD
- 카테고리/상품 조회, 상세조회, 등록, 수정, 삭제

#### 운영
- 카테고리 별 상품 선택 가능

- 상품 선택 시 장바구니에 추가

- 수량 및 결제 금액 자동 계산

#### 가상 결제 처리
- 결제 수단 선택 : 현금 / 카드

- 현금 결제 시 거스름돈 자동 계산

- 결제 수단, 금액, 결제 시간 데이터 저장

#### 결제 내역 조회
- 특정 Session의 결제 내역 조회

- 결제 수단, 금액, 결제 시간 데이터 조회

- 결제 취소 가능

- 총 매출, 결제 수단 별 매출 표시

- 영업 종료 시 특정 Session의 모든 데이터 삭제
