# 지호락(Jihorak, 스터디 관리 서비스)

<p align="center" >
  <img height="100" src="https://user-images.githubusercontent.com/53418946/180585110-3a3ca556-2be1-4251-8788-dda41a221ae5.png">
</p>

> *子曰: 知之者不如好之者 好之者不如樂之者* (자왈: 지지자불여호지자 호지자불여락지자)
>
> 아는 사람은 그것을 좋아하는 사람만 못하고, 좋아하는 사람은 즐기는 사람만 못하다.
>
>_논어 옹야편(雍也篇)

[http://jihorak.space](http://jihorak.space)



<br/>

## 지호락의 특징

- 관심분야, 위치 지역 태그를 기반으로 원하는 스터디를 모집하고 지원할 수 있습니다. 
- 모임 기능을 통해 스터디원들과 모임을 진행할 수 있습니다.
- 웹에디터를 통해 조금 더 편리한 글 작성이 가능합니다.

<br/>

## 기술 스택
### Backend
![Generic badge](https://img.shields.io/badge/17-OpenJDK-537E99.svg)
![Generic badge](https://img.shields.io/badge/2.7.1-SpringBoot-6DB33F.svg)
![Generic badge](https://img.shields.io/badge/8.0-MySQL-01578B.svg)

### Thymeleaf
![Generic badge](https://img.shields.io/badge/4.6.0-Bootstrap-523C76.svg)
![Generic badge](https://img.shields.io/badge/3.1.1-Jdenticon-EC9AD2.svg)
![Generic badge](https://img.shields.io/badge/3.5.1-Tagify-444444.svg)
![Generic badge](https://img.shields.io/badge/2.25.1-Trumbowyg-EC9148.svg)
![Generic badge](https://img.shields.io/badge/4.1.0-CropperJS-3979FC.svg)
![Generic badge](https://img.shields.io/badge/8.11.1-MarkJS-F2DF3B.svg)
![Generic badge](https://img.shields.io/badge/2.25.1-MomentJS-040404.svg)

<br/>

## 기능
### 스크린샷
- WEB

### 기능 목록
- 회원가입/로그인 화면: 이메일 기반의 회원가입, 로그인
- 첫 화면: 스터디 관련 기능 수행
  - 스터디 리스트 조회
  - 스터디 검색(키워드 표기)
- 스터디 관리 화면: 소속 스터디를 관리
  - 스터디: 개설/수정/조회, 프로필, url
  - 모임:
  - 유저: 알림

<br/>

## 아키텍처
![db-erd](https://user-images.githubusercontent.com/53418946/185012498-4cc132f7-5787-4244-acdb-b0589a2d5d72.png)

<br/>

## 디렉토리 구조
```
├── docs                            (Web Front 스크린샷, DB 스키마 이미지 등)
/src/main
├── java/me/jincrates/studymanager
│   ├── infra                       ()
│   │   ├── config                  ()
│   │   └── mail                    ()
│   └── modules                     ()
│       ├── account                 ()
│       ├── event                   ()
│       ├── main                    ()
│       ├── notification            ()
│       ├── study                   ()
│       ├── tag                     ()
│       └── zone                    ()
└── resources
    ├── modules                     ()
    │   ├── static                  ()
    │   └── templates               ()
    ├── application.yml             (프로젝트 관련 설정 파일)
    ├── application-dev.yml         (테스트코드 관련 설정 파일)
    └── zones_kr.csv                (지역정보 csv 파일)

** 모듈 하위 form, validator 구조는 생략
```
<br/>

## 참고 자료
* 뼈대: 백기선, 스프링과 JPA 기반 웹 애플리케이션 개발(https://inf.run/vsDz)
