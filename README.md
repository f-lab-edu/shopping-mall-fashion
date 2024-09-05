# 👚👕  MallFashion
`MallFashion`은 **패션상품의 판매와 구매가 가능한 이커머스 플랫폼으로 사용자가 사용하기 편한 User Experience를 제공함을 목표로 합니다.**

<br>

# 📗 프로젝트 아키텍쳐

<img width="769" alt="image" src="https://github.com/user-attachments/assets/d52f5edc-2c0f-4411-a137-cca3fa4791f9">

<br>

# 🎯 프로젝트 목표

**1. 사용자가 이용하기 편한, 사용자의 정보를 안전하게 취급하는 서비스 제공**
- 단순한 CRUD 기능만 개발하는 것이 아닌 실제의 Use case를 고민하고 어떻게 하면 고객이 편리하게 이용할 수 있을지 고민하면서 개발합니다.
- 고객의 정보를 안전하게 유지, 전달합니다.
  - (기존) 비밀번호 찾기시 유저 전화번호를 쿼리 파라미터로 넘기는 구조
  - (변경 후) 아이디 찾기, 비밀번호 찾기시 해당 권한만 갖는 일회용 토큰 발급
  - password 인코딩
  - 권한에 따라 접근할 수 있는 리소스를 제한
- 서버의 정보를 안전하게 관리합니다.
  - 노출되면 안되는 키 값들을 도커 실행시점에 전달
//TODO


<br>

# 🧩 사용 기술

- Java 17
- SpringBoot 3.2.5
- Spring Security 6.2.4
- Spring Data JPA
- MySql
- Redis
- JUnit5
- Mockito
- AWS EC2
- AWS RDS
- Docker
- Github Actions
- NGrinder
- Pinpoint
- Prometheus
- Grafana

<br>

# 💡 MallFashion의 대표적인 Use Case

## 상점 등록 프로세스
<img width="500" alt="image" src="https://github.com/user-attachments/assets/c8576100-f641-4b42-a7a6-56df8b55d3d0">

단순히 상점 엔티티를 생성하여 DB에 저장하는 것이 아닌 실제 케이스와 같이 
1. 스토어 신청
2. 관리자 검토 후 승인
3. 신청자에게 임시 비밀번호 알림
4. 신청자는 발급 받은 임시 비밀번호로 상점 초기 정보 세팅

의 절차를 통과하도록 구현합니다.

<br>

# 🛠️ 프로젝트 개선사항

- [재사용 가능한 공통 응답 API 구현](https://doohyunhwang97.github.io/develop/mall-fashion/mall1/)
- [전역 예외처리기 도입](https://doohyunhwang97.github.io/develop/mall-fashion/mall2/)
- [CI를 통한 지속적인 통합 환경 구축](https://doohyunhwang97.github.io/develop/mall-fashion/mall3/)
- [Continuous Delivery를 통한 자동화된 QA환경 구축](https://doohyunhwang97.github.io/develop/mall-fashion/mall4/)


# 📚 데이터 모델

## ERD
<img width="1431" alt="image" src="https://github.com/user-attachments/assets/e3b790a0-b392-4b62-9eac-dafc0cd7fd10">


//TODO

# 🥁 Git 브랜치 전략

프로젝트의 버전 관리 및 협업을 위해 Git-Flow 전략을 채택하였습니다.

<img width="600" alt="image" src="https://github.com/f-lab-edu/joy-mall/assets/59166263/06ab0ce6-e13d-493d-88b5-bda6da3a1bc5">

- **main**: 제품으로 출시될 수 있는 브랜치입니다. 릴리스 이력을 관리하기 위해 사용됩니다.
- **develop**: 다음 출시 버전을 개발하는 브랜치입니다. 기능 개발 및 버그 수정이 이루어지며, 모든 변경사항은 이 브랜치에 병합됩니다.
- **feature**: 새로운 기능 개발이나 실험적인 작업을 위한 브랜치입니다. 각 기능 개발이 완료되면 `develop` 브랜치로 병합됩니다.
- **release**: 이 브랜치는 준비 중인 릴리스의 최종 버그 수정 및 문서 작업 등을 위해 사용됩니다. 준비가 완료되면 `main`과 `develop` 브랜치에 병합됩니다.
- **hotfix**: 이미 출시된 버전에서 발생한 긴급한 버그를 수정하기 위한 브랜치입니다. 수정이 완료되면 `main`과 `develop` 브랜치에 병합됩니다.

## MallFashion이 Git-Flow을 사용하는 방법
- develop 브랜치에서 feature을 생성해 신규 기능을 개발합니다.
- 기능 개발 완료시 develop 브랜치에 병합합니다. 이때 CI를 이용해 기존 코드와 충돌을 예방합니다.
- 사용자에게 서비스하기 전에 release 브랜치를 생성하여 부하테스트 및 성능테스트를 거칩니다. 이때 release 브랜치는 생성과 동시에 QA 서버에 자동 배포됩니다.
- 모든 테스트가 완료된 release 브랜치는 main 브랜치로 병합됩니다. 이때 변경 사항은 운영서버에 자동으로 배포됩니다.

<br>

# 👬 MallFashion이 협업하는 방식
## Github Project를 이용한 스프린트 단위 계획 수립과 공유
- Jira처럼 Github Project를 이용하여 백로그에 issue 들을 등록하고 시작 날짜, 종료 날짜, 예상 시간 등을 입력해서 스프린트로 옮겨서 업무를 진행하고 팀원과 공유하고 있습니다.
- 스프린트 단위는 일주일로 정해서 스프린트 단위로 계획 수립, 프로젝트를 진행하고 있습니다.

<img width="1723" alt="image" src="https://github.com/user-attachments/assets/cae9ef2d-cb27-4acd-9af2-ef1cee3036de">

![image](https://github.com/user-attachments/assets/726f2f6b-0206-4ee6-adfe-cf004078bde6)

## 다이어그램을 이용한 적극적인 소통
- MallFashion 팀은 한명은 개발, 다른 한명은 코드 리뷰를 담당하고 있습니다.
- 개발자가 기획까지 담당하기 때문에 적극적으로 소통하지 않으면 서로가 생각하는 기획의 방향성이 다를 수 있습니다.
- 이러한 상황이 발생할 때마다 다이어그램을 통한 적극적인 의사소통을 해결합니다.

<img width="800" alt="image" src="https://github.com/user-attachments/assets/e3ba4340-7c75-4d03-97b2-1fffa6b5f991">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/ea40e834-ba1a-4718-96c6-99ae74ec8f77">
