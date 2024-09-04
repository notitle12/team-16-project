# team-16-project

# 개발 기간 및 작업 관리


## 개발 기간

- 전체 개발 기간 : 2024-08-22 ~ 2024-09-02
  - 프로젝트 설계 : 2024-08-22 ~ 2024-08-23
  - 백엔드 개발: 2024-08-24 ~ 2024-09-01
  - 서버배포 : 2024-08-31 ~ 2024-09-02

## **작업 관리**

- GitHub Projects와 Issues를 사용하여 진행 상황을 공유했습니다.
- 주간회의를 진행하며 작업 순서와 방향성에 대한 고민을 나누고 노션에 데일리스크럼 내용을 기록했습니다.

# 팀원 역할분담

- 봉대현 (팀장) :
  - 주문, 결제, 배송지, 메뉴도메인 기능개발
  - 서버배포
- 김휘수 :
  - 회원가입, 로그인, 시큐리티 기능 개발
- 박지안 :
  - 카테고리, 지역, 가게 도메인 기능 개발

# 서비스 구성 및 실행방법

<aside>

### 1. **서비스 구성**

- **기술 스택**: 프로젝트에서 사용하는 주요 기술과 프레임워크를 명시합니다.
  - **백엔드**: Java, Spring Boot, JPA, PosterSQL
  - **AI 연동**: Google Cloud AI
  - **배포**: Docker, AWS EC2
- **시스템 아키텍처**: 서비스의 전반적인 아키텍처를 간략하게 설명합니다.
  - **모놀리식 아키텍처**: 모든 기능이 단일 애플리케이션으로 통합된 구조.
  - **데이터베이스**: PostgresSQL을 사용하여 데이터 관리.
  - **AI API 연동**: 외부 AI API와 통신하여 상품 설명을 생성.
- **주요 구성 요소**:
  - **사용자 관리**: 고객, 가게 주인, 관리자 별로 권한을 관리하는 사용자 관리 시스템.
  - **주문 관리**: 온라인 및 대면 주문을 처리하고 관리하는 시스템.
  - **결제 관리**: 카드 결제 정보를 처리하고 저장하는 시스템.
  - **상품 관리**: 상품 정보 등록 및 AI 기반 상품 설명 생성.
  - **로그 관리**: 시스템의 모든 주요 활동을 기록하는 감사 로그 시스템.
</aside>

## 인프라 구성도

https://drive.google.com/file/d/1hTWDe41GN8kNfux5PVuqYhA7AcCfgvKW/view?usp=sharing
![image](https://github.com/user-attachments/assets/12444879-bc0a-41e8-aad0-33ea17824e9c)  


## 실행방법

```bash

#로컬에서 실행하는 경우

# postgres 설정 및 실행
url: postgresql://localhost:5432/delivery
username: postgres
password: 11223344

#프로젝트 클론
git clone https://github.com/notitle12/team-16-project.git

```

# 프로젝트 목적/상세

- **음식 주문 관리 플랫폼을 개발**
  - 목적 : **실생활의 행동을 대체하거나, 반복적이고 인력이 많이 소요되는 작업을 서비스로 자동화**하는 것
  - 목표 : 광화문 근처에서 운영될 음식점들의 배달 및 포장 주문 관리, 결제 그리고 주문 내역 관리 기능을 제공
  - 차별점 :
    - 가게 사장님이 AI기능으로 상품 설명을 쉽게 작성할 수 있도록 지원

- 기대효과 :
  - 업무 효율성 향상
  - 사용자 편의성 증대

# ERD

- [https://dbdiagram.io/d/주문관리-플랫폼ERD-66c6a529a346f9518cbd14e9](https://dbdiagram.io/d/%EC%A3%BC%EB%AC%B8%EA%B4%80%EB%A6%AC-%ED%94%8C%EB%9E%AB%ED%8F%BCERD-66c6a529a346f9518cbd14e9)
![image](https://github.com/user-attachments/assets/46d731cb-93de-4c32-b576-a5c877e7a2d4)


# 기술 스택


- Programming language
  - Java
- Framework
  - Spring Boot
  - Spring Security
  - Spring Data JPA
- Collaboration
  - Slack
  - Git
  - ZEP
- Data
  - PostgreSQL
  - Querydsl
- Infrastructure
  - AWS
  - Docker
  - Ubuntu
  - Google Cloud AI
- Testing
  - POSTMAN
  - JUnit5
- Build Tool
  - Gradle
# 개발 규칙

## Branch (git flow)

- main: 배포본
- develop : 배포본 이전본, 총 통합 브랜치 역할
- feature/기능명 : 기능ex) feature/login, feature/main

## commit 컨벤션

- feat : 새로운 기능 추가
- fix : 기능 수정
- refactor : 코드 리팩토링
- docs : 문서 수정
- test : 테스트 코드

## 네이밍 규칙

- 변수명
    
    
    ●카멜 표기법(Camel Case) - 일반적인 변수들에 사용
    
    첫 글자를 대문자로 적되, 맨 앞에 오는 글자는 소문자로 표기하는 것이다.
    
    표기한 모습이 낙타의 등과 같다고 하여 카멜 표기법이라고 부른다.
    
    ```html
    int totalNumber;
    ```
    
    ●스네이크 표기법(Snake Case) - DB 테이블의 컬럼에 사용
    
    단어 사이에 언더바를 넣어서 표기하는 것이다.
    
    ```html
    int total_number;
    ```
    

- 함수선언방식
    - 조회 : get
    - 수정 : update
    - 삭제 : delete
    - 등록 : create
    - 서치 : search
- 파일명
    - 도메인 + 기능들
    - ex) [UserService.java](http://UserService.java) , UserRepository
- dto 생성시
    - 도메인 + 기능+ Request/Response + Dto
