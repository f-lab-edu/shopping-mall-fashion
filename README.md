# 프로젝트 배경
재학중이던 학교의 [웹 프로그래밍 동아리](https://github.com/wafflestudio)에서 자바 스프링 기반으로 [개인 프로젝트](https://github.com/DoohyunHwang97/spring-seminar-assignment)와 [팀 프로젝트](https://github.com/DoohyunHwang97/naver-cafe-clone)를 진행했습니다.
프로젝트를 진행하면서 스프링 웹 어플리케이션에 대한 전반적 이해도를 높일 수 있었지만 아무래도 기본적인 CRUD 만 구현을 했다면 이제 개인 프로젝트로 대용량 트래픽, 대용량 데이터를 다루기 위해서는 어떤 기술적인 고민을 해야하는지에 대해서 학습하고 싶어서 시작한 개인 프로젝트입니다.
그래서 차근차근 기술을 하나씩 더해가보면서 성능을 확인하고 응답 속도와 데이터 사이즈에 대한 감을 익히고자 했습니다.

* 2024/09 프로젝트 마무리 및 리뷰
* 2025/05 회고 및 리뷰 추가 : 8개월 후 어떤 부분들이 부족했고 보완 가능성이 있는지에 대한 스스로 평가한 리뷰 추가

<br>
</br>

# 프로젝트 목적

1. **DB 인덱싱부터 원격 캐시 서버까지 점진적으로 조회 성능을 개선**해나가며 **대용량 트래픽 처리에 대해 이해**도를 높이고자 했습니다.
2. **대량의 데이터를 조회 및 삽입하는 상황**에서 병렬 처리 등을 고려하며 **대용량 데이터 처리에 대한 이해**도를 높이고자 했습니다.
3. **선착순 쿠폰과 같은 기능을 개발**하여 **분산락과 트랜잭션 개념 등 동시성 처리에 대한 이해**도를 높이고자 했습니다.
4. **단위 테스트, 슬라이스 테스트, 통합 테스트 작성**을 통해 **테스트 관련 이해**도를 높이고자 했습니다.
5. **모니터링 시스템 및 자동화된 CI/CD 구축**을 통해 **AWS, Docker 등 플랫폼 시스템에 대한 이해**도를 높이고자 했습니다.

<br></br>

# 💻 트러블 슈팅

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
프로파일링을 통해 쿼리 수행속도 개선 확인(45:개선전, 42: 개선후)

![image](https://github.com/user-attachments/assets/25077a1b-8b69-4622-b929-6ac5979d1327)
쿼리 로그 확인을 통해 속도를 느리게 하던 범인 색출(?)에 성공

<img width="663" alt="image" src="https://github.com/user-attachments/assets/3f62b971-d991-4bff-8c04-1a4da43402a8">

대부분의 수행시간을 차지하고 있었다

#### 2. 페이징 쿼리 개선
조회 요청 시 발생한 count 쿼리가 전체 응답 시간의 대부분을 차지하여 성능 저하의 원인임을 확인했습니다. JPA 페이지네이션 구현에서 발생하는 추가적인 count 쿼리가 10만 건의 상품 레코드에 대해 매번 수행되면서 문제가 발생했습니다. 
이를 해결하기 위해 페이지 건수를 고정 값으로 반환하고, 추후 연산하여 반환하는 방식을 도입했습니다. 
또한, 프론트엔드에서 페이지 번호 요청을 미리 보내는 방식도 제안했습니다. 
마지막으로, 커버링 인덱스를 정의해 성능이 3배 개선된 것을 프로파일링을 통해 확인했습니다.

> 2025/05 리뷰
> 1. 해당 count 쿼리는 모든 상품 대상으로 인덱스 풀 스캔이 일어나기 때문에 200ms가 소요됐습니다. 이는 단일 조회 요청 기준으로 봤을 때 오래 걸리는 요청입니다. 상품 리스트 같은 경우 자주 변하는 정보가 아니라서 그 count 수 또한 잘 변하지 않을 것입니다. 따라서 1시간 배치 등을 이용해서 백그라운드에서 캐시 히트율이 높은 아이템 count 를 대상으로 캐싱 및 주기적 갱신을 해둔다면 count 요청시 낮은 레이턴시로 응답이 가능할 것입니다.
> 2. 클라이언트에서 별도의 count api를 호출하는 방식을 사용했는데 그럴 필요 없이 `Page<Item>` 대신 `List<Item>`으로 조회 후 캐시된 count 수를 `PageImpl`로 래핑해주는 방식을 사용할 수 있습니다.

#### 3. 캐시 도입 배경
Ngrinder를 통해 요구사항에 맞는 트래픽을 발생시킨 결과, 주문과 상품 조회 모두 평균 300ms로 요구사항에 못 미치는 성능을 Grafana 대시보드에서 확인했습니다. 대시보드에서 DB 커넥션 획득 과정에서 병목이 발생했으며, db.t3.micro의 부족한 코어 개수로 커넥션풀 튜닝이 불가능했습니다. 이에 DB 스케일 아웃 대신 **캐시 서버를 통한 DB 부하 분산**을 선택했으며, t2.micro 인스턴스를 이용한 캐시 운영이 **DB 스케일 아웃보다 2배 경제적**임을 확인했습니다. 또한, 캐싱을 통한 조회 성능 향상과 레디스 클러스터의 수평적 확장성으로 추후 트래픽 증가에 대비할 수 있다는 점을 고려했습니다.

<img width="500" alt="image" src="https://github.com/user-attachments/assets/7bc9182f-1bf0-4771-8e0f-9f065cd10693">

커넥션풀에서 발생한 병목이 전체 성능에 영향을 주고 있었다.

> 2025/05 리뷰
> 1. 단순한 경제성 외에도 DB에 직접 부하가 집중되면 전체 DB 응답 속도가 느려질 수 있다는 구조적 한계가 존재합니다.
> 특히 DB는 어플리케이션 서버에 비해 동시 처리 가능한 요청 수가 제한적이며 스케일업이나 Read Replica를 적용하더라도 DB 장애 가능성 자체를 근본적으로 줄이지는 못합니다.
> 실제로 DB가 병목 지점이 될 경우 전체 서비스가 영향을 받을 수 있으며 이는 서비스 전체의 안정성을 저하시킵니다.
> 따라서 캐시 서버 도입은 단순히 비용 절감 차원을 넘어서 DB 부하 분산, 응답 속도 개선, 장애 전파 차단 등 여러 측면에서 유리한 선택입니다.

#### 4. 캐시 도입 전 고려했던 내용들
가장 먼저 고려한 사항은 가용 가능한 메모리였습니다. t2.micro 인스턴스 기준으로 캐시 서버에서 사용할 수 있는 메모리는 약 500MB였습니다. 
이때 30만 건의 조회 요청 cardinality가 대략 2천건임을 추정했습니다. 
따라서 응답 데이터의 평균 크기가 약 1.5KB였기 때문에, 필요한 캐시 메모리는 약 3MB로 매우 적음을 추정할 수 있었습니다. 또한, **추후 캐시해야 할 데이터가 100배 이상 증가하더라도 t2.micro 인스턴스**가 충분히 캐시 서버로 활용 가능하다는 결론을 내렸습니다.

#### 5. 캐싱으로 인한 정합성 문제
저는 성능 요구사항을 만족하는 선에서 **실험적으로 가장 적은 TTL을 선택**하여 DB와 캐시 사이 정합성을 최대한 지키고 사용자들이 최신화된 데이터를 확인할 수 있게 했습니다. 그리고 재고나 판매 상태와 같이 최신화가 중요한 데이터에 대해서는 주문과 같이 상태 변화가 일어났을 때 주문과 같은 트랜잭션에 캐시를 같이 지워주어 캐싱으로 인해 최대한 정합성 문제가 발생하지 않게 설계했습니다.
또한 현재 캐시 서버에서 판매 상태와 재고를 각각 다른 키로 관리하고 있었기 때문에 상품이 품절되지 않았음에도 재고가 0개가 되어 주문이 되지 않는 등 데이터 무결성과 정합성이 유지되지 않는 문제가 발생할 수 있었습니다.
이를 해결하기 위해 **Redis Transaction을 이용**하여 상품의 판매 상태와 재고 **변경이 아토믹하게 적용**되도록 설계했습니다.

> 2025/05 리뷰
> 1. TTL 을 짧게 가져가면 최신성을 보장할 수 있지만 자칫 트래픽이 많은 서비스인 경우 DB에 순간적인 부하를 줄 수 있어 Cache Stampede, Duplicate Read 등을 고려하는 등의 설계에 주의가 필요합니다. 재고와 판매상태의 최신성을 보장하는 쪽으로 설계는 취지는 좋으나 현재 스펙에서 두 상태의 최신성은 굳이 매우 짧게 가져갈
> 필요가 있는지에 대한 의문이 존재합니다. 재고와 판매 상태 모두 상태가 변경되었을 때 캐시를 갱신해주는 방향으로 설계해야 합니다. 그리고 주문시에 재고는 한번 더 확인이 가능하기 때문에 실시간이 아니어도 "재고가 모두 소진되었습니다" 라고 충분히 UX로 풀어낼 여지가 있습니다. 따라서 재고의 TTL은 5초 정도, 판매 상태는 자주 변하는 값이 아니므로 10분 정도면 충분할 것이라고 생각합니다.
> 2. 따라서 Redis Transaction 또한 필요하지 않을 것 같습니다.

#### 6. 결과
위의 요구사항을 기준으로 **그라파나를 통해 확인**한 결과 조회 응답속도 평균은 300ms → 130ms로 **각각 2.5배 정도의 성능 개선**을 확인할 수 있었습니다. 

또한 DB 쿼리의 감소로 기존에 발생하던 커넥션 풀 병목도 평균적으로 유휴 커넥션이 50% 정도 존재하는 것을 확인하여 해결되었음을 확인했습니다. 결과적으로 커넥션 대기 시간으로 인한 응답 지연이 개선되어 주문 응답속도 또한 400ms → 185ms 로 2.3배의 성능 개선 효과를 얻은 것을 확인했습니다.

<img width="1404" alt="image" src="https://github.com/user-attachments/assets/278d1897-fe8e-447e-9679-816ace5c85be">
<img width="526" alt="image" src="https://github.com/user-attachments/assets/db2e40b9-64e2-4daf-b128-4cc4b46839b6">

<br></br>

> 2025/05 리뷰
> 1. 현재 조회 응답 속도(130ms)와 CPU 사용률 추이를 기반으로 볼 때 2 vCPU 환경에서 500 QPS는 한계에 근접한 상태입니다. 따라서 CPU 부하를 분산시키기 위해 인스턴스를 수평 확장한다면 응답 지연이 더욱 유의미하게 개선될 것으로 기대됩니다.

<br></br>

## 대량의 상품 데이터 삽입

### 1. 대량의 상품 및 옵션 데이터 배치
십만 건의 상품 데이터와 오십만 건의 상품 옵션 데이터를 대량으로 배치하는 기능을 구현했습니다. (상품별 옵션 5개씩 배치)

### 2. 인메모리 해시맵 사용
상품 옵션 삽입 시, 상품 레코드의 PK 값이 필요해 추가적인 쓰기 및 조회 쿼리가 필요했습니다. 이를 해결하기 위해, 상품과 옵션 간의 관계 정보를 인메모리 해시맵에 임시로 저장하는 방식을 선택했습니다.

싱글 인스턴스와 적은 키 개수(약 10만 개)를 고려해 Redis 등 캐시 서버 대신 인메모리 캐시를 사용했습니다. 해시맵을 사용한 이유는 키로 사용되는 상품명이 고유하며, 이를 기반으로 상수 시간에 조회가 가능하기 때문입니다.

```java
Map<String, List<ItemOptionDto>> itemNameOptionsMap = new HashMap<>(testItemDtos.size() * 2);
```

> 2025/05 리뷰
> 1. 대용량 데이터 삽입시 Redis 등 원격 서버를 사용하는 것은 네트워크로 인한 IO 증가 및 네트워크 비용 증가 문제 때문에 고려 대상이 아닙니다. 또한 기존 서비스가 사용하고 있는 Redis 라면 
> 2. 현재 데이터 사이즈는 로컬 캐시로 충분히 처리가능하고 추후 데이터 사이즈가 더 커진다고 해도 백그라운드 배치 작업이기 때문에 페이징 처리를 해서 조금씩 가져와서 처리하거나 분산 컴퓨팅을 통해서 처리할 수 있습니다.

### 3. JdbcTemplate 사용
전체 데이터 중 상품 및 옵션 데이터가 90% 이상을 차지하므로, JdbcTemplate을 이용한 벌크 연산으로 삽입을 구현했습니다.
JPA가 제공하는 bulk insert는 키 자동 생성 전략으로 인해 사용할 수 없었습니다. 이를 통해 삽입, 삭제에서는 90% 이상의 성능 개선을 얻었습니다.

> 2025/05 보완
> 1. 현재 DB 접근 기술로 사용하고 있는 JPA를 사용한다면 10만건의 상품 삽입에 대해서 10만 건의 쿼리가 발생합니다. 이는 10만 개의 트랜잭션이 생김을 의미하며 SQL이 제공하는 다중 삽입의 성능적인 이점을 얻지 못함을 의미합니다. 따라서 다중 삽입이 가능한 방법으로써 JdbcTemplate을 고려했습니다.
> 2. JPA 기본키 생성 전략으로 `Identity` 방식을 사용하고 있기 때문에 10만번의 `persist()`가 호출됨을 의미합니다.

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

### 6. 100만 건 삽입시 발생했던 이슈 트러블 슈팅
상품 데이터 규모를 10만 건에서 100만 건으로 확장하던 중 JVM OOM(OutOfMemoryError) 에러가 발생하여 원인 분석을 진행했습니다.

먼저 JVM 힙 메모리 크기는 1028MB로 설정되어 있었고 힙 덤프를 분석한 결과
상품 옵션 데이터를 삽입하기 위해 100만 개의 상품 객체를 한 번에 메모리에 적재하고 있었던 것이 문제의 원인으로 확인되었습니다.

이에 따라 Java Stream API를 사용할 때 불필요한 중간 상태 객체가 생성되지 않도록
map-struct 기반의 연산 파이프라인을 무상태(stateless) 연산 중심으로 재구성하였습니다.
즉 중간 결과를 수집하지 않고 즉시 처리되는 방식으로 변경하여 메모리 사용을 최적화했습니다.

> 2025/05 리뷰
> 무상태 연산으로 구성하더라도 데이터 사이즈가 더 커진다면 확률은 낮겠지만 똑같이 OOM 에러가 발생할 가능성이 있습니다. 그래서 안전하게 처리 가능한 단위만큼 데이터를 분할 로드하고 처리하는 페이징 전략을 고려해야 합니다.

그 결과, 상품 삽입 과정에서의 힙 메모리 사용량을 평균 약 50MB 수준으로 크게 감소시킬 수 있었고
대량 데이터 처리 시에도 OOM 없이 안정적으로 동작함을 확인했습니다.

<br>
</br>

<img width="344" alt="image" src="https://github.com/user-attachments/assets/efd5459f-b358-45f3-84c9-1e0e6b1ab0b0" />

<br>
</br>

# 📗 프로젝트 아키텍쳐

![image](https://github.com/user-attachments/assets/83220af8-2d35-42b7-90f0-02e57ce01921)

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

# 💻 인프라

- 메인 스프링부트 서버: t3.medium 1대(2코어 4GB), t2.micro 1대(1코어 1GB)
- MySQL 서버: db.t3.micro 1대
- Redis 서버: t2.micro 1대
- Prometheus + Grafana 서버: t2.micro 1대
- ELK 서버: t3.medium 1대
