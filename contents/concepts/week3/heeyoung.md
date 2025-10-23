# [App center] 3주차 - Database & JPA 심화

# **🍀  쿼리 메서드와 JPQL은 어떻게 사용할까요?**

## 1.  Spring Data JPA 쿼리 메서드는 어떤 규칙에 따라 작성하며, 어떤 방식으로 동작할까요?

<aside>
💡

**Spring Data JPA 쿼리 메서드**

메서드 이름을 기반으로 자동으로 쿼리를 생성해 데이터베이스에서 데이터를 조회할 수 있게 해주는 기능입니다.

</aside>

## 1-2.  네이밍 규칙

1. **메소드 이름 시작**
    - find, count, save, delete, exists
    - 쿼리의 목적을 나타내는 키워드로 시작합니다.
2. **반환 데이터**
    - (default) : 조회된 단일 데이터 반환
    - All : 조회된 여러 데이터 반환
    - Distinct : 조회된 데이터 중 중복 데이터 제외 후 반환
    - First : 조회된 데이터 중 첫번째 데이터만 반환
    - Top(N) : 조회된 데이터 중 상위 N개까지 반환
3. **By**
4. **엔티티 필드명**
5. **검색 조건**
    - And, Or, Is/Equals, Between, LessThan, IsNull, Not, In …

```sql
Boolean existsByUserIdAndAssignmentId(Long id, Long assignmentId);
Optional<Recommendation> findByUserIdAndAssignmentId(Long id, Long assignmentId);
User findFirstByAgeLessThan(Integer age);
```

## 1-3.  동작 방식

아래와 같은 메소드가 있다고 가정하고 테스트 코드를 실행시켜보았습니다.

```sql
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findFirstByAgeLessThan(Integer age);
}
```

```sql
// 테스트 코드
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void findName_test() {
        // 실제 쿼리가 실행되는 코드
        User findUser = userRepository.findByName("kimheeyoung");
        User findUser2 = userRepository.findFirstByAgeLessThan(25);
        // 쿼리 로고 콘솔에서 확인
    }
}
```

```sql
// userRepository.findByName("kimheeyoung");
Hibernate: 
    select
        u1_0.id,
        u1_0.age,
        u1_0.name 
    from
        users u1_0 
    where
        u1_0.name=?
        
// userRepository.findFirstByAgeLessThan(25);
Hibernate: 
    select
        u1_0.id,
        u1_0.age,
        u1_0.name 
    from
        users u1_0 
    where
        u1_0.age<? 
    fetch
        first ? rows only
```

이런 식으로 쿼리가 생성되는데, 동작 방식은 아래와 같습니다.

### userRepository.findByName(”kimheeyoung”) 실행 시 내부동작

1. **Repository 인터페이스 호출**
    1. UserRepository 인터페이스는 구현체가 없기 때문에, Spring Data JPA 가 런타임에 프록시 객체를 만들어서 대신 구현해줍니다.
2. **프록시 객체가 호출 감지**
3. **메서드 이름을 분석해 → JPQL 쿼리를 생성**
    1. find + By + Name → select u from User u where [u.name](http://u.name) = :name
4. **Hibernate 가 JPQL → SQL 로 변환**
5. **DB 에 SQL 쿼리 실행**
6. **결과를 엔티티 객체로 매핑**
    1. Hibernate 가 DB 에서 가져온 row 를 User 엔티티 객체로 매핑
7. **객체 반환**

## 2.  쿼리 메서드만으로는 해결하기 어려울 때, @Query와 JPQL을 어떻게 활용할 수 있을까요?

<aside>
💡

- **@Query**
    
    사용자 정의 쿼리를 생성하는 방법입니다.
    
</aside>

## 2-1.  JPQL과 Native Query의 차이점은 무엇이고, 각각 언제 사용하는 것이 적절할까요?

### 🌀  JPQL

객체지향 쿼리 언어로, 데이터베이스의 테이블을 대상으로 하는 SQL 과 달리, **엔티티 객체를 대상**으로 쿼리를 작성할 수 있습니다.

`findUserByAssignments` 라는 메서드는 단순 엔티티 조회밖에 할 수 없기 때문에 @Query 와 JPQL 을 사용합니다. `userID` 에 해당하는 `Assignment` 들을 조회하고, 그 결과를 DTO 로 매핑해서 리스트로 반환하도록 합니다.

```sql
    @Query("""
        SELECT new heeyoung.hee.domain.Assignment.dto.response.UserAssignmentResponseDto(
            a.id, a.title, a.content, a.link, a.createdAt, a.recommendationCount)
        FROM Assignment a
        WHERE a.userId = :userId
        """)
    List<UserAssignmentResponseDto> findUserAssignments(@Param("userId") Long userId);
```

- **언제 사용?**
    - 데이터베이스로부터 독립적이기 때문에 데이터베이스 변경 가능성이 있을 때 사용합니다.
    - 복잡한 쿼리를 작성해야 할 때 사용합니다.
    - 엔티티에 매핑하여 사용하는 만큼 데이터베이스에 직접적으로 쿼리를 실행하는 Native Query 보다 속도적인 부분에서 차이가 있습니다.

### 🌀  Native Query

Native SQL Query 는 **JPA 를 통해 직접 SQL 을 실행**하는 방식입니다. Hibernate 가 JPQL → SQL 로 변환해주는 것이 아닌 개발자가 직접 쓴 SQL 그대로 데이터베이스에 실행시킵니다. 

@Query 어노테이션을 사용하고 **nativeQuery 속성을 true 로 설정**합니다. 이제 native query 를 통해 age 보다 적은 나이를 가진 User 객체 중 첫번째를 반환하게 됩니다.

```sql
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    @Query(
            value = "select id, name, age from users where age < :age",
            nativeQuery = true
    )
    User findFirstByAgeLessThan(Integer age);
}
```

다시 테스트 코드를 실행해보면 두 쿼리 로그에 차이가 발생합니다.

```sql
// Hibernate 가 JPQL -> SQL 변환한 경우
Hibernate: 
    select
        u1_0.id,
        u1_0.age,
        u1_0.name 
    from
        users u1_0 
    where
        u1_0.name=?
        
// nativeQuery 를 사용한 경우 
Hibernate: 
    select
        id,
        name,
        age 
    from
        users 
    where
        age < ?
```

- **언제 사용?**
    - 데이터베이스의 고유 기능이나 복잡한 SQL 문법을 이용해야 할 때 사용합니다.
    - 단, 성능 최적화로 인해 데이터베이스에 종속적이므로 데이터베이스가 변경되면 쿼리도 다시 작성해야 합니다.
- **cf. 데이터베이스에 종속적?**
    - JPQL 을 쓰면 쓰여진 쿼리문대로 JPA 에게 요청하게 됩니다. JPA 는 내부적으로 연결된 DB 에 맞게 알아서 번역하여 실행합니다. 따라서 DB 가 바뀌어도 엔티티 구조가 동일하다면 JPQL 수정이 불필요합니다.
    - Native Query 는 특정 데이터베이스의 sql 문법을 직접 사용하므로 DB 별 고유 함수나 문법을 사용하면 다른 DB 에서 오류가 발생할 가능성이 있습니다. 따라서 DB 변경 시 쿼리 수정이 필요할 수 있습니다.

## 2-2.  JPQL에서 사용하는 파라미터 바인딩 방법에는 무엇이 있나요?

<aside>
💡

**파라미터 바인딩**

쿼리에 작성되는 특정 속성을 매개변수로 매핑하는 것을 의미합니다.

</aside>

### 🌀  이름 기반 바인딩

`=:` 연산자를 사용합니다. 또 @Param 어노테이션을 통해 파라미터를 쿼리에 사용된 이름과 매핑합니다.

```sql
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("select a from Assignment a where a.userId = :userId")
    List<Assignment> findAllByUserId(@Param("userId")Long userId);
}
```

메서드에 매개변수 값이 할당되어 전달 되면 → @Param(”userId”) 어노테이션이 값을 가져와서 → @Query 안에 정의된 :userId 라는 자리 표시자에 매핑 합니다.

### 🌀  위치 기반 바인딩

`=?` 연산자를 사용합니다.

```sql
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("select a from Assignment a where a.userId =?1 AND a.userName =?2")
    List<Assignment> findAllByUserIdAndUserName(Long userId, String userName);
}
```

매개변수에 두 값이 할당 되면 각각 첫 번째, 두 번째 값으로 ?1 과 ?2 에 매핑됩니다.

다만 위치 기반 바인딩은 매개변수가 추가되거나 순서가 바뀔 경우 쿼리 내부의 모든 인덱스를 수정해야 하기 때문에 이름 기반 바인딩을 사용하는 것이 권장됩니다.

## 2-3.  @Modifying은 무엇이고 언제 필요할까요?

### 🌀 **@Modifying**

- @Query 어노테이션을 통해 작성된 변경이 일어나는 쿼리(INSERT, DELETE, UPDATE) 를 실행할 때 사용합니다.
- 주로 **벌크 연산 시에 사용**합니다.
    - **벌크 연산이란?**
        - 대량의 데이터를 다룰 때 여러 개의 데이터 레코드를 하나의 쿼리로 한 번에 처리하는 연산 방식입니다.

```sql
    @Modifying
    @Query("update Assignment a " +
            "set a.content = :content " +
            "where a.userId = :userId")
    void updateContentAllByUserId(@Param("content") String content, @Param("userId")Long userId);
```

```sql
    @BeforeEach
    void setUp() {
        // 1. User 데이터 생성 및 저장 (과제 소유자)
        User userA = User.createUser("Alice", 25);
        userRepository.saveAll(List.of(userA));

        userAId = userA.getId();

        // 2. Assignment 데이터 생성 및 저장
        // Alice의 과제 2개
        Assignment assignmentA1 = Assignment.builder()
                .title("Alice Task 1").content("Content A1")
                .userId(userAId)
                .build();

        Assignment assignmentA2 = Assignment.builder()
                .title("Alice Task 2").content("Content A2")
                .userId(userAId)
                .build();

        assignmentRepository.saveAll(List.of(assignmentA1, assignmentA2);
    }
```

```sql
    @Test
    @Commit
    void bulk_test() {
        List<Assignment> beforeDelete = assignmentRepository.findByUserId(userAId);

        // @Modifying 테스트
        assignmentRepository.updateContentAllByUserId("merong",userAId);

        List<Assignment> afterUpdate = assignmentRepository.findByUserId(userAId);
        afterUpdate.forEach(System.out::println);
    }
```

- Alice 라는 유저 객체와 Alice 의 assignment 2개까지 총 3개의 데이터가 영속성 컨텍스트에 저장 됩니다.
- updateContentAllByUserId() 메서드를 통해 userAId 를 가진 유저(Alice) 의 Content 를 “merong” 으로 벌크 처리합니다.
- **실행 결과**

```sql
Assignment(id=1, title=Alice Task 1, content=Content A1, userId=2)
Assignment(id=2, title=Alice Task 2, content=Content A2, userId=2)
```

분명 Content 를 수정했는데 영속성 컨텍스트에는 반영이 안 되어있습니다. 이는 @Modifying 는 1차 캐시를 무시하고 DB 로 바로 쿼리를 날리기 때문입니다. → 1차 캐시와 DB 불일치

### 🌀  **flushAutomatically() 와 clearAutomatically()**

```sql
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Modifying {
    boolean flushAutomatically() default false;

    boolean clearAutomatically() default false;
}
```

@Modifying 어노테이션을 열어 보면 두 가지 메서드가 정의되어있습니다.

- **flushAutomatically()**
    - true 면 해당 쿼리를 실행하기 전, 영속성 컨텍스트의 변경 사항을 DB 에 flush 합니다. (영속성 컨텍스트에 쌓여있는 SQL 명령들을 실제 db 에 전송하여 반영)
- **clearAutomatically()**
    - true 면 해당 쿼리를 실행한 후, 영속성 컨텍스트를 clear 합니다.
    - 따라서 위에서 발생한 문제는 clearAutomatically=true 를 통해 해결할 수 있습니다. 데이터 변경 후 영속성 컨텍스트를 비워주고, 이후에 객체를 조회할 시 해당 엔티티가 존재하지 않기 때문에 DB 조회 쿼리를 실행하게 됩니다.

```sql
    @Modifying(clearAutomatically = true)
    @Query("update Assignment a " +
            "set a.content = :content " +
            "where a.userId = :userId")
    void updateContentAllByUserId(@Param("content") String content, @Param("userId")Long userId);
    
    // 수정 후 실행 결과
    Hibernate: 
    update
        assignment 
    set
        content=? 
    where
        user_id=?
        
		Hibernate: 
		    select
		        a1_0.id,
		        a1_0.content,
		        a1_0.title,
		        a1_0.user_id 
		    from
		        assignment a1_0 
		    where
		        a1_0.user_id=?
	        
		Assignment(id=1, title=Alice Task 1, content=merong, userId=2)
		Assignment(id=2, title=Alice Task 2, content=merong, userId=2)
```

1차 캐시에 데이터가 없기 때문에 update 수행 후 select 실행 시 DB 의 최신 데이터를 가져와 반영하게 됩니다.

## 3.  JPA에서 제공하는 페이징과 정렬은 어떻게 구현할 수 있을까요?

## 3-1.  Page와 Slice는 어떤 차이가 있으며, 각각 어떤 경우에 선택하는 것이 좋을까요?

### 페이징 처리란?

한 번에 모든 데이터를 가져오지 않고 나눠서 데이터를 가져올 수 있도록 하는 것을 의미합니다. 

- page: 현재 페이지 번호 (0부터 시작)
- size: 해당 페이지에 담을 데이터 개수
- sort: 정렬 기준

이 파라미터들을 Pageable 이라는 구현체에 담아 페이징을 설정합니다.

### 🌀  Pageable

JpaRepository 가 상속하고 있는 PagingAndSortingRepository 에는 아래와 같은 메서드를 제공합니다.

```java
@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID> extends Repository<T, ID> {

	Iterable<T> findAll(Sort sort);

	Page<T> findAll(Pageable pageable);
}
```

- **Pageable 인터페이스**
    - 클라이언트가 Http 요청으로 page, size, sort 와 같은 정보를 가진 데이터를 보내게 되면, 해당 데이터는 Pageable 이라는 객체로 매핑되게 됩니다.
    - Pageable 은 인터페이스이므로 실제로 쓸 때는 **PageRequest** 라는 Pageable 구현체를 사용합니다.
    - 페이징 정보가 담긴 Pageable 객체는 **Page<T> 타입**으로 반환되게 됩니다.
- **Page<T> 인터페이스**
    - getNumber() : 현재 페이지 번호
    - getSize() : 현재 페이지가 제공하는 데이터 개수
    - getContent() : 현재 페이지의 데이터 리스트
    - getTotalPages() : 전체 페이지 수
    - getTotalElements() : 데이터의 총 개수

**Repository**

pageable 을 파라미터로 받으면 Spring Data JPA 가 자동으로 페이징 쿼리를 수행합니다.

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
	Page<Assignment> findByUserId(Long userId, Pageable pageable);
}
```

**Service**

PageRequest.of(page, size) 를 사용해 페이지 정보를 생성합니다. (sort 는 기본으로 unsort() 입니다)

assignmentRepository.findByUserId(userId, pageable) 호출 시 자동으로 페이징 쿼리가 실행됩니다.

```java
@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public Page<Assignment> getUserAssignmentsWithPaging(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return assignmentRepository.findByUserId(userId, pageable);
    }
}
```

**Controller**

GET 요청을 통해 userId 를 가진 user 의 0번째 페이지, 10개 데이터를 가져옵니다.

Page<Assignments> 를 반환하면 자동으로 JSON 응답으로 변환됩니다.

```java
@RestController 
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping("/{userId}")
    public Page<Assignment> getUsersAssignments(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return assignmentService.getUserAssignmentsWithPaging(userId, page, size);
    }
}
```

### 🌀  Page 와 Slice

Page 와 다르게 Slice 는 총 데이터 개수, 총 페이지 수를 알 수 없습니다.

Repository 에 Slice 를 반환하는 메서드를 추가했습니다.

```java
Slice<Assignment> findSliceBy(Pageable pageable);
```

테스트 코드 입니다.

```java
    @Test
    void paging_test() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Assignment> page = assignmentRepository.findByUserId(userAId, pageable);

        Pageable pageable2 = PageRequest.of(0, 2);
        Slice<Assignment> slice = assignmentRepository.findSliceBy(pageable2);
    }
```

Page 의 경우 :

```java
Hibernate: 
    select
        a1_0.id,
        a1_0.content,
        a1_0.title,
        a1_0.user_id 
    from
        assignment a1_0 
    where
        a1_0.user_id=? 
    offset
        ? rows 
    fetch
        first ? rows only
Hibernate: 
    select
        count(a1_0.id) 
    from
        assignment a1_0 
    where
        a1_0.user_id=?
```

Slice 의 경우 :

```java
Hibernate: 
    select
        a1_0.id,
        a1_0.content,
        a1_0.title,
        a1_0.user_id 
    from
        assignment a1_0 
    offset
        ? rows 
    fetch
        first ? rows only
```

- **비교**
    - **Page 객체**는 count 쿼리를 함께 실행해 전체 데이터 수와 전체 페이지 수를 제공합니다.
    - 따라서 게시판과 같이 **총 데이터 갯수**가 필요한 환경에 유리합니다.
    - **Slice 객체**는 count 쿼리를 실행하지 않아 전체 개수를 알 수 없습니다.
    - 따라서 무한 스크롤과 같이 **데이터 양이 많을 경우** 사용하는 것이 성능상 유리합니다.

## 4.  쿼리를 작성할 때, 성능 측면에서 고려해야 할 사항으로는 무엇이 있을까요?

### 4-1.  findAll()을 무분별하게 사용할 때 어떤 성능 이슈가 발생하고, 이를 어떻게 개선할 수 있을까요?

findAll() 은 데이터베이스에서 여러 레코드를 조회할 때 사용합니다. 대용량으로 데이터를 로드하기 때문에 네트워크 지연이 발생하고, 불필요한 데이터까지 조회할 우려가 있습니다. → 성능 저하

- **개선 방법**
    - **SELECT 를 할 때 필요한 컬럼만 선택하기**
        - findAll() 은 실행시 SELECT * FROM entity 와 같이 전체 필드 값을 불러오게 됩니다. 따라서 조건부 조회를 실행해 필요한 값만 선택적으로 가져올 수 있도록 하는 것이 좋습니다.

### 4-2.  데이터를 DB에서 처리하는 것과 자바 코드에서 처리하는 것에 어떤 차이가 있을까요?

|  | **DB 처리** | **자바 코드 처리** |
| --- | --- | --- |
| **처리 속도** | 빠름 | 상대적으로 느림 |
| **네트워크 부하** | 낮음 (결과만 전송) | 높음 (전체 데이터 전송) |
| **메모리 사용** | DB 서버 메모리 | 애플리케이션 메모리 |
| **복잡한 로직** | 제한적 | 자유로움 |
- **DB 처리**

```java
// Repository
@Query("select a from Assignment a where a.userId = :userId")
List<Assignment> findAllByUserId(@Param("userId")Long userId);

// Service
    public List<Assignment> getUserAssignments(Long userId) {
	    // DB 에서 이미 필터링된 결과만 받게 됩니다.
        return assignmentRepository.findByUserId(userId);
    }
```

- **자바 코드 처리**

```java
// Repository
List<Assignment> findAll();

// Service
    public List<Assignment> getUserAssignments(Long userId) {
        List<Assignment> allassignments = assignmentRepository.findAll();
        List<Assignment> result = new ArrayList<>();
        // 자바에서 직접 필터링을 거칩니다.
        for (Assignment assignment : allassignments) {
            if (assignment.getUserId().equals(userId)) {
                result.add(assignment);
            }
        }
        return result;
    }
```

자바 코드에서 데이터를 처리하려면 DB 에서 필터링을 거치지 못한 모든 데이터들을 자바 서버의 메모리로 옮겨와 비교해야 합니다. 따라서 보통 데이터 **조회, 필터링, 정렬, 집계** 등의 작업은 DB 에서 처리하는 방식이 일반적입니다.

다만, 복잡한 **비즈니스 로직**은 DB 가 수행하기 어렵기 때문에 (ex. 암호화 로직) 자바 서버에서 처리합니다. 

---

# **🍀 N+1 문제는 무엇이 어떻게 대처해야 할까요?**

## 1.  N+1 문제란 무엇이고 왜 발생하나요?

연관 관계에서 발생하는 이슈로, **연관 관계가 설정된 엔티티를 조회할 경우에 조회된 데이터 갯수(n) 만큼 연관관계의 조회 쿼리가 추가로 발생**하여 데이터를 읽어오게 됩니다. 즉, 1번의 쿼리를 날렸을 때 의도하지 않은 N번의 쿼리가 추가적으로 실행되는 것입니다.

- **When 언제 발생하는가?**
    - JPA Repository 를 활용해 인터페이스 메소드를 호출할 때
- **Who 누가 발생시키는가?**
    - 1:N 또는 N:1 관계를 가진 엔티티 조회 시 발생
- **How 어떤 상황에 발생되는가?**
    - JPA Fetch 전략이 EAGER 전략으로 데이터를 조회하는 경우
    - JPA Fetch 전략이 LAZY 일 때 데이터를 가져온 이후에 연관관계인 하위 엔티티를 다시 조회하는 경우
- **Why 왜 발생하는가?**
    - JPA Repository 로 find 시 실행하는 첫 쿼리에서 하위 엔티티까지 한 번에 가져오지 않고, 하위 엔티티를 사용할 때 추가로 조회하기 때문
    - JPQL 은 기본적으로 글로벌 Fetch 전략을 무시하고 JPQL 만 가지고 SQL 을 생성하기 때문

```java
	  // Assignment Entity
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // User Entity
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Assignment> assignments = new ArrayList<>();
```

```java
    @Test
    void nplus1_test() {
        // user 10 명 생성
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User("user"+i, i));
        }
        userRepository.saveAll(users);

        // Assignment 10 개 생성
        List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = users.get(i);
            Assignment assignment = Assignment.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .user(users.get(i))
                    .build();
            user.getAssignments().add(assignment);
            assignments.add(assignment);
        }
        assignmentRepository.saveAll(assignments);
        
        entityManager.clear();

        List<User> allUsers = userRepository.findAll();
    }
```

유저와 과제를 각각 10개씩 생성하고 유저 조회 후 과제를 확인하는 테스트 코드입니다.

```java
Hibernate: select u1_0.id,u1_0.age,u1_0.name from users u1_0
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
Hibernate: select a1_0.user_id,a1_0.id,a1_0.content,a1_0.title from assignment a1_0 where a1_0.user_id=?
```

처음 User 를 조회하는 쿼리 1번 + 각 User 의 Assignment 를 조회하는 쿼리 10번 = 총 11 번의 쿼리가 발생하였습니다. 이렇게 User 수만큼 추가 쿼리가 발생하는 것이 N+1 문제입니다.

### 1-1.  즉시 로딩(EAGER)과 지연 로딩(LAZY)의 차이는 무엇인가요?

### 🌀  즉시 로딩 (EAGER)

즉시 로딩은 엔티티 조회 시, 즉시 로딩으로 설정된 연관관계 엔티티를 한 번에 같이 조회하는 방식입니다.

```java
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Assignment> assignments = new ArrayList<>();
    // User 엔티티를 조회하는 쿼리가 실행될 때마다 assignments 엔티티들을 함께 조회해 옵니다.
```

만약 Assignments 의 개수가 1000~2000개 라면 User 를 한 번 조회했을 때 1000~2000 개의 쿼리가 추가로 나가게 됩니다. 따라서 성능 저하가 필연적이기 때문에 권장하지 않습니다.

### 🌀  지연 로딩 (LAZY)

연관된 엔티티를 **실제 사용할 때** 조회합니다. 

```java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
```

1. 로딩되는 시점에 LAZY 설정이 돼있는 User Entity 는 프록시 객체로 가져옵니다.
2. 그리고 실제 객체를 사용하는 시점 (User 를 호출하는 시점) 에 초기화가 되고, DB 에서 Query 가 호출됩니다.

하지만 지연 로딩을 사용해도 연관 관계 엔티티 조회시 쿼리가 나가기 때문에 N+1 문제가 근본적으로 해결되지 않습니다. 단순히 연관 엔티티의 조회를 ‘접근하는 시점’으로 미루는 것일 뿐이기 때문입니다.

## 2.  N+1 문제를 해결하는 방법에는 무엇이 있을까요?

### 2-1.  Fetch Join

Fetch Join 은 DB 에서 데이터를 가져올 때 처음부터 연관된 데이터까지 같이 가져오게 하는 방법입니다.

- **일반 Join과 fetch Join의 차이**
    - 일반 Join 은 조건용으로, filter 의 역할을 하며 지연 로딩 시 연관 객체는 채위지지 않습니다. → 추가 조회 쿼리 필요, N+1 문제 발생
    - Fetch Join 은 조회 시 한 번의 SQL 로 연관 데이터를 가져올 수 있습니다.
    - Fetch Join 이 걸린 Entity 모두를 영속화 하기 때문에, FetchType 이 Lazy 인 Entity 를 참조하더라도 이미 영속성 컨텍스트에 들어있어 따로 쿼리가 실행되지 않은 채로 N+1 문제가 해결됩니다.
    
    ```java
    // entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // repository
    @Query("select a from Assignment a join fetch a.user where a.user.id = :userId")
    List<Assignment> findAllByUserId(@Param("userId")Long userId);
    ```
    
    ```sql
    select a1_0.id, a1_0.content, a1_0.title,
           u1_0.id, u1_0.age, u1_0.name
    from assignment a1_0
    join users u1_0 on u1_0.id = a1_0.user_id
    where a1_0.user_id = ?
    ```
    
    - 이렇게 쿼리 한 번으로 Assignment 뿐 아니라 연관된 User 데이터도 한 번에 가져오게 됩니다.
    - LAZY 로 설정돼 있어도 Fetch Join 으로 미리 가져왔기 때문에 객체가 채워진 상태입니다.
- **Fetch Join 의 한계**
    - 1:N 에서, 즉 유저를 조회할 때 과제도 함께 Fetch Join 으로 조회하게 되면
    
    ```java
    // entity
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();
    ```
    
    ```java
    select u from User u join fetch u.assgnments where u.name = "heeyoung"
    ```
    
    - 위 쿼리를 실행하면 아래와 같은 결과가 나오게 됩니다. User 데이터가 Assignment 수만큼 반복됩니다.
    - 1:N 관계이기 때문에 User 1명이 Assignment 3개와 JOIN 됨 → 성능 저하로 이어질 수 있습니다.
    
    | user.id | user.name | assignment.id | assignment.title |
    | --- | --- | --- | --- |
    | 1 | heeyoung | 101 | t1 |
    | 1 | heeyoung | 102 | t2 |
    | 1 | heeyoung | 103 | t3 |

### 2-2.  Batch Size

Batch Size 는 N+1 문제에서 발생하는 추가 조회 쿼리를 1개의 쿼리로 줄이는 방법입니다. 2개 이상의 @OneToMany 컬렉션을 fetch Join 할 때 중복을 방지하기 위해 Batch Size 설정을 통해 해결합니다.

`SELECT * FROM assignment WHERE user_id IN (1, 2, 3)` 방식으로, IN 절로 모아서 쿼리를 보냅니다. (user_id 가 1,2,3 중 하나인 assignment 를 모두 가져오라는 뜻)

```java
  jpa:
    database: mysql
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        format_sql: true
        show_sql: true
    open-in-view: false
    default_batch_fetch_size: 100
```

application.yml 파일에 설정을 `default_batch_fetch_size` 를 통해 한 번에 최대 몇개의 연관 엔티티를 묶어서 가져올지 설정해줍니다.

- Batch Size 는 JOIN 을 사용하지 않습니다. User 테이블 쿼리와 Assignment 테이블 쿼리를 분리하여 실행하기 때문에 Fetch Join 처럼 1:N 컬렉션에서의 중복 row 문제가 발생하지 않습니다.

### 2-3.  @EntityGraph

JPQL 로 fetch join 을 직접 작성하지 않고 @EntityGraph 어노테이션을 붙여서 fetch join 을 편리하게 사용할 수 있게 도와줍니다.

```java
@EntityGraph(attributePaths = {"user"})
List<Assignment> findAllByUserId(@Param("userId")Long userId);
```

Repository 메서드에 @EntityGraph 를 붙여 fetch Join 을 적용할 수 있다. 

- **fetch join 과의 차이점**
    - fetch join 은 조건이나 join 구조에 따라 Inner/outer 선택이 가능하지만
    - @EntityGraph 는 내부적으로 outer join 으로 연관 엔티티를 가져옵니다.
    - cf. Join 의 종류
        - Inner Join : 두 테이블 모두 조건 만족할 때만 row 반환
        - outer join : 왼쪽 테이블 기준 항상 row 반환, 오른쪽 테이블이 없어도 Null 로 채움
    - fetch join 은 JPQL 문자열을 직접 작성해야 해 유지보수가 불편할 수 있어, 쿼리 로직이 간결하다면 @EntityGraph 를 사용하는 것이 적절할 수 있습니다.