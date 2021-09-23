# README

# <프로젝트 소개>

## [비대면 화상 회의 & 스터디 플랫폼 "PPAKGOM"]

### :bulb: 관심사 검색을 통해 원하는 스터디를 빠르게 찾고 스터디원 평가를 통해 열정적인 스터디원과 함께 하는 화상회의 스터디 플랫폼

##### 진행 기간 : 2021.07.12 ~ 2021.08.20

##### 목표

- 코로나로 인해 오프라인 스터디가 힘든 지금, 온라인으로 스터디를 지원합니다.
- 관심분야를 통해 스터디, 스터디원을 찾을 수 있고 열정도를 보면서 좋은 팀원과 함께 할 수 있습니다.
- 공부나 관심 분야 공유를 하고 싶은 사람들 누구나 이용할 수 있고 특히, 취업 준비생의 취뽀를 응원합니다🙂

<br>

# :running:팀원 소개

| 이름               | 역할             | 역할 상세                                             |
| ------------------ | ---------------- | ----------------------------------------------------- |
| 이광교(kwanggyo)   | 팀장 & Front-end | 스터디 검색, 추천, 찜하기, 카드, 일정관리             |
| 이진영(wlsdud837)  | Front-end & 배포 | 소셜로그인, 점수/출석 차트, 화상회의, 알림, 채팅      |
| 송지연(6loutlside) | Back-end         | 회원 CRUD, 스터디 가입, 출석, 점수 결과               |
| 신준희(yallyyally) | Back-end         | 스터디 CRUD, 스터디 추천, 찜하기, 문제집 만들기, 초대 |
| 천주용(juyongc)    | Front-end        | 프로필, 스터디 관리, 스터디원 초대/가입, 문제집       |

<br>

# :tv: 시연 영상

[![Video Label](https://img.youtube.com/vi/OkFVDs6xKVY/0.jpg)](https://youtu.be/OkFVDs6xKVY?t=0s)



# :heavy_check_mark: 주요 기능

**스터디 검색**

![스터디검색](README.assets/스터디검색.gif)

**스터디 생성**

![스터디생성](README.assets/스터디생성.gif)

**스터디 가입**

![스터디가입](README.assets/스터디가입.gif)

**스터디 일정&출석**

![스터디일정&출석](README.assets/스터디일정&출석.gif)

**스터디 찜하기**

![스터디찜하기](README.assets/스터디찜하기.gif)

**스터디 초대**

![스터디초대](README.assets/스터디초대.gif)

**스터디원 평가**

![스터디원 평가](README.assets/스터디원 평가.gif)

**문제집 생성**

![문제집 생성](README.assets/문제집 생성.gif)

**문제풀기&결과**

![문제풀기&결과](README.assets/문제풀기&결과.gif)

<br>

# 🎈 기획

##### API 명세서

- https://confusion-hovercraft-efe.notion.site/29466b85a3c34d4cbf8a2c8a5c8bb4b9?v=14ace7cd5f1549bf924ba899610ac7ef

##### 와이어 프레임

- https://confusion-hovercraft-efe.notion.site/c905a398dbb043faab420e725292e31b

**데이터베이스**

![image-20210820012701972](README.assets/image-20210820012701972.png)

<br>

# 🎨 아키텍쳐

![아키텍쳐](README.assets/아키텍쳐.png)

<br>

# :gear: 개발 환경

#### Frontend

- node v14.17.2
  - https://nodejs.org/es/blog/release/v14.17.2/
- npm 6.14.13
  - https://www.npmjs.com/package/npm/v/6.14.13
- @vue/cli 4.5.13
  - https://www.npmjs.com/package/@vue/cli/v/4.5.13
- VScode Extensions
  - Vetur - Pine Wu
  - EditorConfig for VS Code
  - Vue
  - Vue VSCode Snippets - sarah.drasner

**Project setup**

```plaintext
npm install
```

**Compiles and hot-reloads for development**

```plaintext
npm run serve
```

**Compiles and minifies for production**

```plaintext
npm run build
```

#### Backend

- Java jdk 1.8

  - https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2018-09/R/eclipse-jee-2018-09-win32-x86_64.zip

- Gradle

  - https://services.gradle.org/distributions/ -> gradle-6.7-bin.zip

- MySQL 8.0.23
- full distribution on Eclipse 4.15
  - https://github.com/spring-projects/toolsuite-distribution/wiki/Spring-Tool-Suite-3
  - STS 3.9.14.RELEASE
  - 디펜던시 : Spring Boot DevTools, Lombok,MySQL Driver, QueryDSL, Stomp-WebSocket, Spring Security, JPA, Mail

