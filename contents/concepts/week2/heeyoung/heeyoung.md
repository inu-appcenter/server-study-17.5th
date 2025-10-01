# [app center] 2주차 - Database & JPA 기초

# 1.  ORM

## 1-1.  ORM 은 무엇일까요?

<aside>
💡

**ORM (Object Relational Mapping)**

객체(Object) 와 DB 의 테이블을 자동으로 연결 (Mapping) 시켜 RDB 테이블을 객체 지향적으로 사용하게 해주는 기술입니다.

→ 객체는 객체대로 설계할 수 있고 RDB 는 RDB 대로 설계가 가능하도록 **ORM 프레임워크가 중간에서 매핑**을 해줍니다.

</aside>

### 1-1-1.  ORM 의 장단점은 무엇일까요?

- **장점**
    - **객체 지향적인 코드로 인해 더 직관적이고 비즈니스 로직에 더욱 집중할 수 있습니다.**
        
        ```java
        // SQL
        INSERT INTO User(1,"heeyoung",24)
        
        // ORM
        User user = User.create(1,"heeyong",24)
        ```
        
    - **재사용 및 유지보수가 편리합니다.**
        - `User` 객체를 조회하는 메소드는 로그인 기능, 사용자 정보 수정 기능 등 여러 곳에서 동일하게 호출하여 사용됩니다.
        - 만약 DB 의 특정 컬럼 이름이 변경된다면, 해당 Java 클래스 필드와 매핑 정보만 수정하면 됩니다.
    - **DBMS 에 대한 종속성이 줄어듭니다.**
        - 객체 간의 관계를 바탕으로 SQL 을 자동으로 생성하기 때문에 RDBMS 의 데이터 구조와 Java 의 객체지향 모델 사이의 간격을 좁힐 수 있습니다.

## 1-2.  스프링에서 ORM 을 어떻게 활용하나요?

### 1-2-1.  Spring Data JPA 란 무엇이고 어떤 기능을 제공할까요?

<aside>
💡

- **JPA (Java Persistent API)**
    - 자바에서 ORM 를 사용하기 위한 표준입니다.
- **Spring Data JPA**
    - Spring 에서 JPA 를 사용할 때, JPA 들의 구현체들을 좀 더 쉽게 사용할 수 있도록 도와주는 Spring 프레임워크 라이브러리 입니다.
    - JPA 를 한 단계 더 추상화 시킨 Repository 인터페이스를 제공합니다.
</aside>

### 🌀  Spring Data JPA 사용 방법

```java
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

- build.gradle 에 의존성을 추가하여 사용할 수 있습니다.

```java
public interface AssignmentRepository extends JpaRepository<Assignment, Long>
```

- JpaRepository<> 인터페이스를 상속받아 사용합니다. JpaRepository 의 제네릭에 관리할 <객체, id 타입> 을 주면 JpaRepository 가 제공하는 CRUD(Create, Read, Update, Delete) 기능들을 모두 사용할 수 있습니다.

```java
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
	Optional<Recommendation> findByUserIdAndAssignmentId(Long id, Long assignmentId);
}
```

- 사용자가 Repository 인터페이스에 정해진 규칙대로 메소드를 입력하면, Spring 이 알아서 해당 메소드 이름을 분석해 적합한 쿼리를 날리는 구현체를 만들어 Bean 으로 등록해둡니다.

### 1-2-2.  Repository Layer 란 무엇일까요?

entity 에 의해 생성된 데이터베이스 테이블에 접근하는 메서드들을 사용하기 위한 인터페이스 입니다. 데이터를 처리하기 위해서는 CRUD 가 필요합니다. 이 때 이러한 **CRUD 를 어떻게 처리할지 정의하는 계층이 Repository** 입니다.

### 1-2-3.  JpaRepository 인터페이스에는 어떤 기능들이 포함되어 있나요?

JpaRepository 인터페이스는 세 가지 주요 인터페이스를 extends 하고 있습니다.

```java
public interface JpaRepository<T, ID>
		extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> 
```

- ListCrudRepository<T, ID>
    - CRUD 기능을 담당하는 메서드들을 정의하고 있습니다.
    - save(), findById(), deleteById(), findAll() 등이 정의되어 있습니다.

- ListPagingAndSortingRepository<T, ID>
    - 페이징 및 정렬 기능을 제공합니다.
    - findAll(Sort sort), findAll(Pageable pageable) 이 정의되어 있습니다.

- QueryByExampleExecutor<T>
    - 엔티티의 일부 필드를 사용하여 검색할 때 동적 쿼리를 만들어 주는 기능을 제공합니다.
    

---

# 2.  영속성 컨텍스트 (Persistence Context) 란?

<aside>
💡

**데이터 (Entity) 를 영구 저장하는 환경** 이라는 뜻으로, 애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 데이터베이스 같은 역할을 합니다.

</aside>

## 2-1.  영속성 컨텍스트의 생명주기는 어떻게 되나요?

데이터베이스 트랜잭션의 범위와 일치합니다. **트랜잭션이 시작될 때마다 영속성 컨텍스트가 생성되고, 트랜잭션이 커밋되거나 롤백될 때 영속성 컨텍스트도 함께 종료**됩니다.

**cf. 트랜잭션**

데이터베이스의 상태를 바꾸는 일종의 작업 단위.

- Commit : 한개의 단위(트랜잭션) 에 대한 작업이 성공적으로 끝나면 이 변경사항을 한꺼번에 DB 에 반영한다.
- Rollback : 부분 작업이 실패하면 트랜잭션 실행 전으로 되돌린다.

## 2-2.  영속성 컨텍스트의 특징으로는 무엇이 있을까요?

- **1차 캐시 (First-Level Cache)**
    - EntityManager 는 엔티티를 조회할 때 1차 캐시에 먼저 저장한 후, 같은 엔티티를 다시 조회하면 DB 에서 가져오는 것이 아니라 캐시된 엔티티를 반환합니다. (캐시는 map 형태로 key-value 로 저장)
    - ex.
        - PK 가 1 인 user 엔티티를 생성 후 영속화
        - em.find(User.class, 1)
        - JPA 는 **영속성 컨텍스트에서** 1차 캐시 확인
        - key = 1 있으면 캐시에서 값을 가져옴, 없으면 DB 조회 → DB 에 데이터가 있다면 가져와 1차 캐시에 저장 후 데이터 반환
        
- **동일성 보장**
    - 같은 트랜잭션 안에서는 같은 엔티티의 `==` 비교를 보장
    - 조회 시마다 새 객체를 생성하는 것이 아니라, 이미 로딩된 엔티티를 반환합니다.

- **쓰기 지연 SQL 저장소**
    - em.persist() 를 호출해도 즉시 INSERT SQL 이 실행되지 않고, 쓰기 지연 SQL 저장소 (버퍼) 에 저장됩니다.
    - transcation.commit() 시에 한꺼번에 DB 에 반영됩니다.

- **변경 감지**
    - 엔티티의 상태 변화를 자동으로 추적하고, 트랜잭션이 커밋되는 시점에 그 변경사항을 데이터베이스에 반영하는 동작입니다.

## 2-3.  플러시 (Flush) 란 무엇인가요?

플러시란 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하는 것을 의미합니다. 

### **2-3-1.  플러시는 언제 발생할까요?**

- **명시적 호출**
    - em.flush() 를 통해 메서드를 직접 호출할 때 발생합니다.
- **자동 호출**
    - 트랜잭션 커밋 시 플러시를 자동 호출합니다.
        - 트랜잭션이 성공적으로 완료되려면, 영속성 컨텍스트에서 수행했던 작업이 데이터베이스에 반영되어야 하기 때문입니다.
    - JPQL 쿼리 실행 시 플러시를 자동 호출합니다.
        - em.persist(newUser) 후 em.createQuary(”select u from User u”) 을 실행하면 DB 에 반영되지 않은 newUser 를 포함해야 하므로 플러시가 먼저 발생합니다.

## 2-4.  준영속 상태의 특징과 merge() 의 동작은 무엇일까요?

### 2-4-1.  준영속 상태의 특징

- 영속성 컨텍스트가 더 이상 해당 엔티티를 관리하지 않습니다. 따라서 값을 변경해도 변경 감지가 작동하지 않으며, 트랜잭션 커밋 시 DB 에 반영되지 않습니다.
- 한 번 영속상태였기 때문에 식별자 값을 가지고 있습니다.

### 2-4-2.  merge()

준영속 상태의 엔티티를 다시 영속 상태의 엔티티로 만들 땐 merge() 메소드를 이용합니다. 

```java
Member mergeM = em.merge(member);
```

파라미터로 전달된 엔티티의 식별자 값으로 영속성 컨텍스트를 조회합니다.

- **준영속 상태일 때**
    - 데이터베이스에서 해당 엔티티를 찾는다.
    - 찾은 엔티티를 영속성 컨텍스트에 추가한다.
    - 전달된 엔티티의 데이터로 기존 엔티티를 업데이트한다. (수정)
- **비영속 상태일 때**
    - 전달된 엔티티를 새로 생성해 영속성 컨텍스트에 추가한다.
    - 데이터베이스에 새로 저장하기 위해 INSERT SQL 실행 (저장)

---

# 3.  Entity 란?

<aside>
💡

**Entity**

데이터베이스에서 쓰일 테이블과 칼럼을 정의한다.

</aside>

- **cf. entity 필드 접근 제한자를 private 으로 줘야 하는 이유**
    
    클래스의 필드에 `private` 접근 제어자를 사용하는 이유는 주로 캡슐화(encapsulation)와 관련이 있습니다. 캡슐화는 객체 지향 프로그래밍의 핵심 원칙 중 하나로, 객체의 세부 구현 내용을 숨기고, 외부에서 직접적인 접근을 제한함으로써 객체의 무결성을 유지하는 것을 목표로 합니다.
    
    `private` 키워드를 사용함으로써 다음과 같은 이점을 얻을 수 있습니다:
    
    1. **데이터 은닉(Data Hiding)**: `private` 필드는 클래스 외부에서 접근할 수 없으므로, 클래스의 내부 상태를 외부의 간섭으로부터 보호할 수 있습니다. 이를 통해 클래스 사용자는 필드의 내부 구현에 신경 쓰지 않고, 제공된 메서드를 통해 클래스와 상호작용할 수 있습니다.
    2. **데이터 보호(Data Protection)**: 필드에 직접 접근을 허용하면, 예상치 못한 값을 설정하여 객체의 상태를 잘못된 상태로 만들 수 있습니다. `private`을 사용하면 이러한 상황을 방지할 수 있으며, 필요한 경우 유효성 검사를 거친 후에만 데이터를 변경할 수 있는 메서드(예: setter)를 제공할 수 있습니다.
    3. **유지보수성(Maintainability)**: 클래스의 내부 구현을 변경하더라도, 해당 필드에 접근하는 public 메서드의 인터페이스가 동일하다면, 클래스를 사용하는 코드를 변경하지 않아도 됩니다. 이는 코드의 유지보수성을 높이는 데 도움이 됩니다.
- **cf. 자료형으로 `wrapper class` 를 쓰는 이유**
    - null 값을 가질 수 있어야 한다.
        - DB 에서는 컬럼 값이 NULL 일 수 있는데, java 의 기본형 자료형은 Null 을 가질 수 없음, wrapper class 는 객체 타입이기 때문에 null 을 허용할 수 있다.
        

## 3-1. Entity 의 생명주기는 어떻게 되나요?

 엔티티가 영속성 컨텍스트 내에서 어떤 상태에 있는지에 따라 비영속, 영속, 준영속, 삭제 4가지 상태로 나뉩니다.

```java
    @PersistenceContext // EntityManager 를 주입받는 JPA 어노테이션
    private EntityManager entityManager;

    @Transactional
    public void User(Long id, String name, Integer age) {

        // 객체 생성 (비영속)
        User user = new User(id, name, age);

        // 객체 영속화
        entityManager.persist(user);

        // 준영속
        entityManager.detach(user);

        // 삭제
        entityManager.remove(user);
    }
```

1. **비영속 (New)**
    
     user 객체가 단순히 Java 메모리에 존재하는 객체일 뿐인 상태입니다.
    
2. **영속 (Managed)**
    
    `entityManager.persist(user);`  JPA 가 user 객체를 영속성 컨텍스트라는 메모리 공간에 저장합니다. 이 상태에서 엔티티는 데이터베이스에 저장될 수 있으며, 영속성 컨텍스트를 통해 자동으로 추적되고 관리됩니다.
    
3. **준영속 (Detached)**
    
    `entityManager.detach(user);` 영속성 컨텍스트에 의해 더 이상 관리되지 않는 상태입니다.
    
4. **삭제 (Removed)**
    
    `entityManager.remove(user);`  영속성 컨텍스트에서 엔티티가 삭제된 상태입니다. 엔티티는 영구적으로 데이터베이스에서 삭제됩니다.
    

## 3-2.  Entity 에 관련된 어노테이션은 무엇이 있나요?

### @Entity

해당 클래스가 Entity 임을 나타내며, JPA 가 이를 데이터베이스 테이블과 매핑하여 관리할 수 있게 됩니다.

### @Getter

```java
public String getName() {
	return name;
}
```

- 객체의 필드 값을 반환한다.
- 캡슐화를 통해 객체 내부의 데이터를 직접 접근하지 않고, 메서드를 통해 접근할 수 있게 한다.
    - cf. 캡슐화: 객체지향에서 객체의 속성(Field) 와 행위(Method) 를 하나로 묶고, 외부에서 직접 접근하지 못하게 은닉한다.

### @NoArgsConstructor

- 기본 생성자를 생성해준다.
- `access = AccessLevel.PROTECTED`
    - 접근 제한을 걸어서 안전성을 높이기 위해 사용합니다. (JPA 에서 기본 생성자는 최대 접근 제한이 protected 이기 때문에 private 이 아닌 protected 사용)
- **cf. Builder 붙인 생성자와 차이점**
    
    : Builder 가 붙은 생성자는 매개변수가 있는 생성자를 의미합니다. 예를 들어 서비스 레이어에서 값을 세팅하고자 할 때 Builder 가 붙은 생성자를 통해 객체를 생성합니다. NoArgsConstructor 은 JPA 가 DB 에서 데이터를 가져올 때 필요합니다. DB 에서 레코드 하나를 읽어올 때, 먼저 빈 객체를 만들어야 하는데 이때 NoArgsConstructor 를 통해 매개변수가 없는 빈 객체를 만듭니다. 그 다음, JPA 가 DB 에서 읽어온 각 컬럼의 값을 이 빈 객체의 필드에 하나씩 채워 넣습니다.
    
    요악하자면…
    
    - **`NoArgsConstructor`**: 프레임워크가 **DB에 있는 기존 데이터를 불러와서** 객체를 만들 때 사용합니다.
    - **빌더(Builder)**: 개발자가 **새로운 데이터를 가지고** 객체를 만들 때, 코드를 더 깔끔하게 작성하기 위해 사용합니다.

### @Id

식별자 필드로, 엔티티의 필드를 테이블의 기본키(PK, Primary Key) 에 매핑하는 역할을 합니다.

### @GeneratedValue

PK 의 값을 자동 생성하기 위해 사용합니다. 

- `strategy = GenerationType.*IDENTITY*`
    - 데이터베이스의 자동증가 기능을 사용합니다.

### @Column

엔티티의 필드를 테이블의 칼럼에 매핑합니다. 선택적 속성으로는 name, nullable, length 가 있습니다.

- name: 엔티티 필드가 매핑될 데이터베이스 테이블의 실제 칼럼 이름을 지정합니다.
- nullable: 데이터베이스 칼럼에 NULL 값 허용 여부를 지정합니다.
- length: String 타입 필드에 대해 칼럼의 길이를 지정합니다.

## 3-2.  Entity 객체 생성 방법으로는 무엇이 있을까요?

🌀  **생성자 패턴**

```java
    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
```

가장 기본적인 엔티티 생성 방법으로, 자바의 기본 생성자 또는 매개변수를 받는 생성자를 사용합니다.

**🌀  정적 팩토리 메서드**

```java
    public static User createUser(Long id, String name, int age) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.age = age;
        return user;
    }
```

클래스의 인스턴스를 반환하는 정적 메서드입니다. 직접적으로 생성자를 호출하는 대신 객체 생성의 세부 사항을 캡슐화 합니다.

**🌀  빌더 패턴** 

```java
    @Builder
    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    
    
```

- @Builder 어노테이션을 통해 Lombok 이 컴파일 시점에 UserBuilder 라는 별도의 내부 클래스를 자동으로 만들어줍니다.
- 생성자로 설정해야하는 값이 많을 경우 빌더를 쓰는 것이 가독성이 좋고, 어떤 값을 먼저 설정하든 상관 없습니다.

```java
    public static User createUser(Long id, String name, int age) {
        return User.builder().id(id).name(name).age(age).build();
    }
```

- 이처럼 객체를 생성할 수 있는 빌더를 builder() 함수를 통해 얻고 거기에 세팅하고자 하는 값을 세팅하고 마지막에 build() 를 통해 빌더를 작동시켜 객체를 생성합니다.

---

# 4. 연관관계 매핑은 무엇이고 어떻게 활용할 수 있을까요?

<aside>
💡

연관관계 매핑이란 객체의 참조와 외래키를 매핑하는 것을 의미합니다.

</aside>

## 4-1.  연관관계 매핑에는 어떤 종류가 있을까요?

연관관계를 맺는 두 엔티티 간에 생성할 수 있는 연관관계의 종류는 다음과 같습니다.

- **다중성에 따른 구분**
    - OneToOne (1:1)
    - OneToMany (1:N)
    - ManyToOne (N:1)
    - ManyToMany (N:M)
- **방향에 따른 구분**
    - 단방향: 한쪽만 다른 쪽을 참조합니다. (user.getAssignment() 만 가능)
    - 양방향: 서로를 참조하여 양쪽 모두 탐색 가능합니다. (Assignmnet.getUser() 도 가능)

## 4-2.  양방향 연관관계 설정 시 주의사항은 무엇일까요?

### 4-2-1. 연관관계의 주인을 어떻게 지정할까요?

연관관계의 주인을 지정한다는 것은, 객체의 두 관게 중 제어의 권한(조회, 저장, 수정, 삭제) 를 갖는 실질적인 객체가 무엇인지 JPA 에게 알리는 것입니다. (주인이 아니면 조회만 가능)

![image.png](%5Bapp%20center%5D%202%EC%A3%BC%EC%B0%A8%20-%20Database%20&%20JPA%20%EA%B8%B0%EC%B4%88%20277158a560658001a6ade26b67ae3635/image.png)

주인은 외래 키가 있는 곳을 주인으로 지정합니다. 이 예시에서는 주문 엔티티가 회원의 FK 를 가지고 있으므로 연관관계의 주인이 됩니다.

```java
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
```

연관관계 주인을 설정하기 위해 `mappedBy` 속성을 사용합니다.

- **양방향 연관관계 설정 시 주의사항**
    - 두 관계 양쪽에 값을 넣어주어야 합니다.
    
    ```java
    order.setUser(user);
    User.getorders().add(user);
    ```
    
    - 양방향 관계에서는 위 두 코드를 하나의 것처럼 사용하는 것이 안전합니다. 따라서 이를 하나로 묶은 코드를 연관관계 편의 메서드라고 합니다.

## 4-3.  영속성 전이는 무엇이고 어떻게 활용할 수 있을까요?

부모-자식 관계는 하나의 엔티티가 다른 엔티티의 존재를 전적으로 소유하고 관리한다는 개념입니다. 예를 들어 게시글과 추천의 관계가 있습니다. 추천은 게시글이 없으면 독립적으로 존재할 의미가 없는 종속적인 객체이며, 함께 저장되거나 삭제됩니다. **영속성 전이 (Cascade) 는 이렇게 특정 엔티티의 상태 변경이, 연관된 다른 엔티티에도 함께 전파되도록 설정하는 기능**입니다.

### 4-3-1.  CASCADE 옵션의 종류와 사용 시기

- `CascadeType.PERSIST`
    - 부모 엔티티를 persist() 할 때, 연관된 자식 엔티티도 함께 영속화 합니다.
    - 부모 저장 시 자식도 필수적으로 함께 저장해야 할 때 사용합니다.
- `CascadeType.REMOVE`
    - 부모 엔티티를 remove() 할 때, 연관된 자식 엔티티도 데이터베이스에서 함께 삭제합니다.
- `CascadeType.MERGE`
    - 부모 엔티티를 병합할 때, 연관된 자식 엔티티도 병합합니다.
    - 준영속 (Detached) 상태의 엔티티를 다시 Managed 상태로 만들 때 사용합니다.
- `CascadeType.ALL`
    - 위의 모든 상태 변화를 전이합니다.
    - 부모가 자식의 생명주기 전체를 책임질 때 사용합니다.

### 4-3-2.  CASCADE.REMOVE 와 orphanRemoval 의 차이

- CASCADE.REMOVE
    - 부모 엔티티가 명시적으로 삭제 (em.remove(parent)) 될 때 작동합니다. ⇒ 부모 삭제 시 연쇄 삭제
- orphanRemoval = true
    - 부모 엔티티가 삭제될 뿐만 아니라, 부모 엔티티로부터 참조를 잃었을 때도 작동합니다.
    - CASCADE.REMOVE 는 고아 객체가 발생하더라도 실제 DB 에서 제거되지 않는 반면, orphanRemoval = true 옵션은 고아 객체 발생 시 자동 제거합니다.

cf. ‘참조를 잃는다’ 는 것은 부모 객체가 가진 List 와 같은 컬렉션에서 해당 자식 객체를 제거하는 행위를 의미합니다. ex) `User.orders.remove(order1)`