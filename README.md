# team-16-project


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
    
    
    ●카멜 표기법(Camel Case) - 일반적인 변수명들에 사용
    
    첫 글자를 대문자로 적되, 맨 앞에 오는 글자는 소문자로 표기하는 것이다.
    
    표기한 모습이 낙타의 등과 같다고 하여 카멜 표기법이라고 부른다.
    
    ```html
    int totalNumber;
    ```
    
    ●스네이크 표기법(Snake Case) - entity의 변수명들에 사용
    
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
    - 기능+ Request/Response + Dto
