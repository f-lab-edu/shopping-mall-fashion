# 👚👕  MallFashion
`MallFashion`은 **패션상품의 판매와 구매가 가능한 이커머스 플랫폼으로 사용자가 사용하기 편한 User Experience를 제공함을 목표로 합니다.**

<br>

# 📗 프로젝트 아키텍쳐

![image](https://github.com/user-attachments/assets/83220af8-2d35-42b7-90f0-02e57ce01921)

<br>

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
- Prometheus
- Grafana
- ELK

<br>

# 💻 인프라

- 메인 스프링부트 서버: t3.medium 1대(2코어 4GB), t2.micro 1대(1코어 1GB)
- MySQL 서버: db.t3.micro 1대
- Redis 서버: t2.micro 1대
- Prometheus + Grafana 서버: t2.micro 1대
- ELK 서버: t3.medium 1대

<br>

# 💻 프로젝트 중점사항

## 500 QPS 조회 요청과 50 QPS의 주문 요청 성능 최적화
### 요구사항
- 전체 상품 리스트 조회시 75%의 사용자는 1번 페이지에 머무르며 20%의 사용자는 2번 페이지로 이동, 5%의 사용자는 3번 페이지까지 조회
- 페이지 건수는 50건, 상품 리스트 하단에서 조회 가능
- 사용자는 평균적으로 조회한 상품의 같은 카테고리의 상품 중 인기 상품 2개, 같은 상점의 상품 중 인기 상품 2개를 각각 추가로 조회(총 5회)
- 상품 상세 페이지는 상품 정보, 상품 설명, 같은 상점 인기 상품 리스트, 같은 카테고리 인기 상품 리스트 순으로 시용자가 스크롤하여 확인 가능
- 상품 리스트 정렬 순위 최신화 주기는 1시간
- 상품 총 개수 10만개, 상품별 옵션 개수 5개(총 50만개)
- 조회 피크 트래픽 500/s
- 목표 조회 응답속도 평균 150ms

### 개선과정
#### 1. 쿼리 튜닝
- 상품 리스트 조회를 위해 선택도가 높은 순으로 멀티 칼럼 인덱스를 정의
- MySQL 로그와 EXPLAIN 구문을 활용해 인덱스 활용 여부 및 비효율을 분석
- 프로파일링을 통해 쿼리 성능을 확인

![image](https://github.com/user-attachments/assets/e2a2f011-a462-4473-ad57-da4d6b8a6aa1)

쿼리 튜닝 후 `explain`을 이용해 확인하는 과정

![image](https://github.com/user-attachments/assets/ec8f448f-310e-4867-a9b9-4859ef86e962)
프로파일링을 통해 쿼리 수행속도 개선 확인(45:개선전, 43: 개선후)

![image](https://github.com/user-attachments/assets/25077a1b-8b69-4622-b929-6ac5979d1327)
쿼리 로그 확인을 통해 속도를 느리게 하던 범인 색출(?)에 성공

<img width="663" alt="image" src="https://github.com/user-attachments/assets/3f62b971-d991-4bff-8c04-1a4da43402a8">

대부분의 수행시간을 차지하고 있었다

#### 2. 페이징 쿼리 개선
조회 요청 시 발생한 count 쿼리가 전체 응답 시간의 대부분을 차지하여 성능 저하의 원인임을 확인했습니다. JPA 페이지네이션 구현에서 발생하는 추가적인 count 쿼리가 10만 건의 상품 레코드에 대해 매번 수행되면서 문제가 발생했습니다. 
이를 해결하기 위해 페이지 건수를 고정 값으로 반환하고, 추후 연산하여 반환하는 방식을 도입했습니다. 
또한, 프론트엔드에서 페이지 번호 요청을 미리 보내는 방식도 제안했습니다. 
마지막으로, 커버링 인덱스를 정의해 성능이 3배 개선된 것을 프로파일링을 통해 확인했습니다.
#### 3. 캐시 도입 배경
Ngrinder를 통해 요구사항에 맞는 트래픽을 발생시킨 결과, 주문과 상품 조회 모두 평균 300ms로 요구사항에 못 미치는 성능을 Grafana 대시보드에서 확인했습니다. 대시보드에서 DB 커넥션 획득 과정에서 병목이 발생했으며, db.t3.micro의 부족한 코어 개수로 커넥션풀 튜닝이 불가능했습니다. 이에 DB 스케일 아웃 대신 **캐시 서버를 통한 DB 부하 분산**을 선택했으며, t2.micro 인스턴스를 이용한 캐시 운영이 **DB 스케일 아웃보다 2배 경제적**임을 확인했습니다. 또한, 캐싱을 통한 조회 성능 향상과 레디스 클러스터의 수평적 확장성으로 추후 트래픽 증가에 대비할 수 있다는 점을 고려했습니다.

<img width="500" alt="image" src="https://github.com/user-attachments/assets/7bc9182f-1bf0-4771-8e0f-9f065cd10693">

커넥션풀에서 발생한 병목이 전체 성능에 영향을 주고 있었다

#### 4. 캐시 도입 전 고려했던 내용들
가장 먼저 고려한 사항은 가용 가능한 메모리였습니다. t2.micro 인스턴스 기준으로 캐시 서버에서 사용할 수 있는 메모리는 약 500MB였습니다. **ELK의 Elastic Search에서 Cardinality Aggregation을 활용**해 30만 건의 조회 요청 중 **고유한 요청이 약 2천 건**임을 확인했습니다. 이를 통해 많은 조회 요청에서 2천 건의 데이터가 중복 응답되고 있다는 분석이 가능했습니다.
응답 데이터의 평균 크기가 약 1.5KB였기 때문에, 필요한 캐시 메모리는 약 3MB로 매우 적음을 추정할 수 있었습니다. 또한, **추후 캐시해야 할 데이터가 10배 이상 증가하더라도 t2.micro 인스턴스**가 충분히 캐시 서버로 활용 가능하다는 결론을 내렸습니다.
#### 5. 캐싱으로 인한 정합성 문제
저는 성능 요구사항을 만족하는 선에서 **실험적으로 가장 적은 TTL을 선택**하여 DB와 캐시 사이 정합성을 최대한 지키고 사용자들이 최신화된 데이터를 확인할 수 있게 했습니다. 그리고 재고나 판매 상태와 같이 최신화가 중요한 데이터에 대해서는 주문과 같이 상태 변화가 일어났을 때 주문과 같은 트랜잭션에 캐시를 같이 지워주어 캐싱으로 인해 최대한 정합성 문제가 발생하지 않게 설계했습니다.
또한 현재 캐시 서버에서 판매 상태와 재고를 각각 다른 키로 관리하고 있었기 때문에 상품이 품절되지 않았음에도 재고가 0개가 되어 주문이 되지 않는 등 데이터 무결성과 정합성이 유지되지 않는 문제가 발생할 수 있었습니다.
이를 해결하기 위해 **Redis Transaction을 이용**하여 상품의 판매 상태와 재고 **변경이 아토믹하게 적용**되도록 설계했습니다.

#### 6. 결과
위의 요구사항을 기준으로 **그라파나를 통해 확인**한 결과 조회 응답속도 평균은 300ms → 130ms로 **각각 2.5배 정도의 성능 개선**을 확인할 수 있었습니다. 

또한 DB 쿼리의 감소로 기존에 발생하던 커넥션 풀 병목도 ****평균적으로 유휴 커넥션이 50% 정도 존재하는 것을 확인하여 해결되었음을 확인했습니다. 결과적으로 커넥션 대기 시간으로 인한 응답 지연이 개선되어 주문 응답속도 또한 400ms → 185ms 로 2.3배의 성능 개선 효과를 얻은 것을 확인했습니다.

<img width="1404" alt="image" src="https://github.com/user-attachments/assets/278d1897-fe8e-447e-9679-816ace5c85be">
<img width="526" alt="image" src="https://github.com/user-attachments/assets/db2e40b9-64e2-4daf-b128-4cc4b46839b6">

## 대량의 상품 데이터 삽입

### 1. 대량의 상품 및 옵션 데이터 배치
십만 건의 상품 데이터와 오십만 건의 상품 옵션 데이터를 대량으로 배치하는 기능을 구현했습니다. (상품별 옵션 5개씩 배치)

### 2. 인메모리 해시맵 사용
상품 옵션 삽입 시, 상품 레코드의 PK 값이 필요해 추가적인 쓰기 및 조회 쿼리가 필요했습니다. 이를 해결하기 위해, 상품과 옵션 간의 관계 정보를 인메모리 해시맵에 임시로 저장하는 방식을 선택했습니다.

싱글 인스턴스와 적은 키 개수(약 1만 개)를 고려해 Redis 등 캐시 서버 대신 인메모리 캐시를 사용했습니다. 해시맵을 사용한 이유는 키로 사용되는 상품명이 고유하며, 이를 기반으로 상수 시간에 조회가 가능하기 때문입니다.

```java
Map<String, List<ItemOptionDto>> itemNameOptionsMap = new HashMap<>(testItemDtos.size() * 2);
```

### 3. JdbcTemplate 사용
전체 데이터 중 상품 및 옵션 데이터가 90% 이상을 차지하므로, JdbcTemplate을 이용한 벌크 연산으로 삽입을 구현했습니다.
JPA가 제공하는 bulk insert는 키 자동 생성 전략으로 인해 사용할 수 없었습니다. 이를 통해 삽입, 삭제에서는 90% 이상의 성능 개선을 얻었습니다.

### 4. 멀티 쓰레드 사용
```java
List<TestItemDto> itemDtos = ConcurrentUtil.collect(IntStream.range(0, itemCount)
                .mapToObj(i -> {
                    return executorService.submit(() ->
                            ItemDtoGenerator.generateItemTestDtos(userCreatedDataInfo,
                                    storeCreatedDataInfo, categoryCreatedDataInfo));
                })
                .toList());

```
### 5. 병렬 스트림 사용
병렬 스트림을 이용해 약 300%의 성능 개선을 얻었습니다. 기존에는 모든 데이터를 한꺼번에 삽입했으나, 개선된 방식에서는 500개 단위로 그룹화해 병렬 처리를 적용했습니다. 그룹 사이즈는 실험적으로 결정되었으며, 대량의 데이터를 나누어 비동기적으로 처리함으로써 성능 이점을 얻을 수 있었습니다.

<기존 코드>
```java
List<Object[]> itemBatchArgs = new ArrayList<>();
        Map<String, List<ItemOptionDto>> itemNameOptionsMap =
                new HashMap<>(testItemDtos.size() * 2);
        for (TestItemDto testItemDto : testItemDtos) {
            itemBatchArgs.add(new Object[]{
                    testItemDto.getName(),
                    testItemDto.getOriginalPrice(),
                    testItemDto.getSalePrice(),
                    testItemDto.getDescription(),
                    testItemDto.getSex().name(),
                    testItemDto.getSaleState().name(),
                    testItemDto.getStoreId(),
                    testItemDto.getCategoryId(),
                    testItemDto.getIsModifiedBy(),
                    testItemDto.getOrderCount()
            });
            itemNameOptionsMap.put(testItemDto.getName(), testItemDto.getItemOptions());
        }
        jdbcTemplate.batchUpdate(itemSql, itemBatchArgs);
```
<변경된 코드>
```java
IntStream.range(0, numberOfItemGroups)
                .mapToObj(i -> testItemDtos.subList(i * groupSize, Math.min((i + 1) * groupSize, testItemDtos.size())))
                .parallel()
                .forEach(itemGroup -> {
                    List<Object[]> batchArgs = new ArrayList<>();
                    for (TestItemDto item : itemGroup) {
                        batchArgs.add(new Object[]{
                                item.getName(),
                                item.getOriginalPrice(),
                                item.getSalePrice(),
                                item.getDescription(),
                                item.getSex().name(),
                                item.getSaleState().name(),
                                item.getStoreId(),
                                item.getCategoryId(),
                                item.getIsModifiedBy(),
                                item.getOrderCount()
                        });
                        itemNameOptionsMap.put(item.getName(), item.getItemOptions());
                    }
                    jdbcTemplate.batchUpdate(itemSql, batchArgs);
                });
```

jdbcTemplate.batchUpdate(itemSql, batchArgs); 부분을 비동기 처리하려 했지만, 성능이 오히려 저하되었습니다.

기존 HashMap<>을 사용해 상수 시간 조회를 보장했고, 테스트 데이터의 정합성이 중요해진다면 ConcurrentHashMap<>으로 전환할 계획입니다.
<br>

# 💻 모니터링 환경

## Grafana 대시보드
### 메인 서버
CPU, 메모리, 커넥션 풀, HTTP 통계, 활성 쓰레드 개수 등을 모니터링합니다.

<img width="1725" alt="image" src="https://github.com/user-attachments/assets/47090ee5-2ef9-4525-93a2-ea2ec1e82a03">

### Redis 서버
![image](https://github.com/user-attachments/assets/6f5ef2bd-578e-49f5-81eb-633041032522)


## ELK 로그 모니터링
* 사용자 행동에 대한 로그와 톰캣 로그를 수집합니다.
* 데이터 분석을 위해 Elastic Search가 제공하는 기능들을 이용해 원하는 데이터를 집계합니다.(ex. Cardinality Aggregation)
<img width="1726" alt="image" src="https://github.com/user-attachments/assets/2271268e-8943-487e-8138-1574a0f164dc">


<br>

# 🛠️ 프로젝트 개선사항

- [재사용 가능한 공통 응답 API 구현](https://doohyunhwang97.github.io/develop/mall-fashion/mall1/)
- [전역 예외처리기 도입](https://doohyunhwang97.github.io/develop/mall-fashion/mall2/)
- [CI를 통한 지속적인 통합 환경 구축](https://doohyunhwang97.github.io/develop/mall-fashion/mall3/)
- [Continuous Delivery를 통한 자동화된 QA환경 구축](https://doohyunhwang97.github.io/develop/mall-fashion/mall4/)


# 📚 데이터 모델

## ERD
<img width="1431" alt="image" src="https://github.com/user-attachments/assets/e3b790a0-b392-4b62-9eac-dafc0cd7fd10">

# 🥁 Git 브랜치 전략

프로젝트의 버전 관리 및 협업을 위해 Git-Flow 전략을 채택하였습니다.

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
