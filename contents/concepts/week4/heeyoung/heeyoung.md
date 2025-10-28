# [App center] 4주차 - Controller & Service Layer

# 🍀 Controller Layer는 무엇인가요?

## 1. Controller Layer는 어떤 역할을 하며, 어떤 구조로 이루어져 있나요?

<aside>
💡

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image.png)

컨트롤러는 클라이언트로부터 요청을 받고 해당 요청에 대해 서비스 레이어의 절한 메소드를 호출하고, 그 결과를 다시 클라이언트에게 반환하는 역할을 합니다. 클라이언트의 요청을 가장 먼저 받아 처리하기 때문에 Presentation Layer 라고합니다.

</aside>

## 1-1.  Controller Layer 의 구조

```java
@RestController // #1
@RequestMapping("/api/users/") // #2
@RequiredArgsConstructor
public class UserController implements UserApiSpecification { 

    private final AuthService authService;
    private final UserService userService;
   
    @PostMapping("/auth/sign-out") // #2
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다.");
    }
```

### #1 : @RestController

- 해당 클래스가 컨트롤러 레이어라는 것을 정의합니다.
- @Controller + @ResponseBody 로, JSON/XML 형태로 객체 데이터를 반환합니다.
    - @Controller 는 View 를 반환합니다.

### #2 : RequestMapping

- HTTP Request 로 들어오는 url 을 특정 메소드로 연결시키는 역할을 합니다.
- 클래스 레벨: 공통 주소
- 메서드 레벨: 공통 주소 외 나머지 하위 주소
    - 아래 메서드 단에 위치한 @PostMapping 은 `/api/users/auth/sign-out` 경로의 요청을 처리합니다.
    - 요청 방식에 따라 GET, POST, PUT, PATCH, DELETE 를 구분합니다.

## 2.  ResponseEntity는 무엇이며, 어떻게 활용할 수 있나요?

```java
public class ResponseEntity<T> extends HttpEntity<T> {
    private final HttpStatusCode status; 
	    ㄴ public sealed interface HttpStatusCode extends Serializable permits DefaultHttpStatusCode, HttpStatus {
					int value();
					boolean is1xxInformational();
					boolean is2xxSuccessful();
					boolean is3xxRedirection();
					boolean is4xxClientError();
					boolean is5xxServerError();
```

ResponseEntity 는 스프링 프레임워크에서 제공하는 클래스로, HTTP 응답을 생성하고 제어하는 데 사용합니다.

### 2-1.  ResponseEntity 의 구조

- 상태 (Status)
    - `HttpStatusCode` 필드를 통해 api 요청의 성공/실패와 그 유형을 클라이언트에게 전달합니다.

- 본문 (Body)
    - 제네릭 타입 <T> 로 지정된 응답 데이터입니다. 주로 dto 객체를 담습니다.

- 헤더 (Header)
    - 응답에 필요한 부가 정보를 설정할 수 있습니다.

⇒  ResponseEntity 클래스를 사용하면 위 요소들을 모두 프론트에 넘겨줄 수 있습니다.

### 2-2.  ResponseEntity 사용방법

- **생성자 사용 방식**
    
    ```java
    return new ResponseEntity(body, hearders, HttpStatus.OK);
    ```
    
- **빌더 패턴 사용 방식**
    
    ```java
    return ResponseEntity.status(HttpStatus.OK)
                         .headers(headers)
                         .body(body);
    ```
    
    - 헤더나 바디가 필요없으면 호출을 생략할 수 있어 유연한 방식입니다.
- **상태 코드만 반환**
    
    ```java
    return ReponseEntity.ok().build();
    ```
    
- **body 포함**
    
    ```java
    return ResponseEntity.ok(body);
    ```
    

---

# 🍀 Service Layer는 무엇인가요?

## 1.   Service Layer는 어떤 역할을 하며, 어떤 구조로 이루어져 있나요?

<aside>
💡

**Service Layer** 

Controller 에서 전달 받은 클라이언트의 요청 데이터를 기반으로 실질적인 비즈니스 요구사항을 처리하는 핵심 계층입니다. ⇒ 서비스가 실제로 어떻게 동작해야 하는가를 코드로 구현!

cf. 비즈니스 요구사항이란?

- 예를 들어 회원가입 기능을 수행해야 한다고 할 때, Controller 는 클라이언트로부터 email, password 와 같은 요청을 받게 됩니다. 해당 요청은 Service 레이어로 전달 됩니다.
- Service 레이어는 이메일 중복검사, 비밀번호 암호화 같은 로직을 구현해야 합니다.
- 이렇게 저장 전에 검증, 계산 등과 같은 논리적 판단이 들어가는 일을 비즈니스 로직 처리라고 합니다.
</aside>

## 1-1.  Service Layer 의 구조

```java
@Service // #1
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // #2
       
    // 유저 삭제
    @Transactional // #3
    public void deleteUser(UserDetailsImpl userDetails) {

        // 유저 조회
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
```

### #1 : @Service

- 해당 클래스가 서비스 레이어라는 것을 정의합니다.
- @Component 를 포함하고 있어, 스프링의 컴포넌트 스캔 대상이 되어 **Bean으로 등록**됩니다.

### #2 : Repository

- Service 와 인접한 계층은 Respository (데이터를 가져오거나 저장할 때 Repository를 직접 사용) 이므로, Repository 객체를 선언해줍니다.

### #3 : Transactional

- 트랜잭션은 하나의 논리적 작업 단위를 묶어서, 그 안의 모든 작업이 모두 성공하거나 실패(롤백) 하도록 보장하는 기능입니다.
- Transactional 을 메서드에 선언하면, 해당 메서드는 하나의 트랜잭션으로 묶여 동작하게됩니다. 실행 도중 예외가 발생하면 자동으로 롤백 되어 데이터의 일관성을 유지합니다.
- cf.
    
    ### 🧩 트랜잭션이 없다면
    
    - 1단계: 유저 찾기 성공
    - 2단계: 유저 삭제 성공
    - 3단계: 이메일 삭제 중 예외 발생 ❌
        
        → 결과: **유저는 이미 삭제됐는데 이메일은 안 지워짐!**
        
    
    💥 이러면 **데이터 불일치(불일관성)** 문제가 생겨요.
    
    (즉, DB에 “유저는 없는데 이메일 정보는 남아있는” 이상한 상태)
    
    ---
    
    ### ✅ 트랜잭션(@Transactional)이 있다면
    
    Spring은 이 메서드 전체를 **하나의 트랜잭션(작업 단위)** 으로 묶어요.
    
    그래서 3단계에서 예외가 발생하면
    
    👉 **전체 작업을 취소(rollback)** 해요.
    
    즉,
    
    - 이미 삭제된 유저도 “없던 일로” 만들어서
    - **DB 상태를 메서드 실행 전 그대로 되돌립니다.**

---

# 🍀 **Spring Bean의 생명주기와 스코프는 무엇인가요?**

- **스프링 빈의 생명주기**
    - 생명주기란 스프링 컨테이너가 빈(객체) 를 만들고, 관리하고, 최종적으로 없애는 전체 과정을 의미합니다.
    
    | 생명주기의 단계 |  |
    | --- | --- |
    | 생성 | 객체가 메모리에 할당되고 초기화되는 단계 |
    | 사용 | 생성된 객체가 실제로 기능을 수행하며, 애플리케이션 내에서 활용되는 관계 |
    | 소멸 | 객체가 더 이상 필요하지 않을 때 메모리에서 해제되고 정리되는 단계 |
    - 빈의 생명주기는 객체, 애플리케이션 생명주기와 유사합니다.
    
    | 생명주기의 단계 | 설명 |
    | --- | --- |
    | 1. 빈 생성 (Instantiation) | 스프링 컨테이너가 설정 파일이나 어노테이션을 기반으로 빈의 인스턴스를 생성이 단계에서는 new 키워드를 사용하여 객체가 메모리에 할당됨. |
    | 2. 의존성 주입 (Dependency Injection) | 생성된 빈에 필요한 의존성을 주입 (예를 들어, 다른 빈을 참조하거나 설정 값을 주입받는 과정Setter 주입 또는 생성자 주입 방식을 사용) |
    | 3. 빈 이름 설정 및 팩토리 정보 제공 | 빈이 BeanNameAware, BeanFactoryAware 등의 인터페이스를 구현하고 있다면,스프링 컨테이너가 빈의 이름이나 팩토리 정보를 설정 |
    | 4. 빈 초기화 전 처리 (BeanPostProcessor) | BeanPostProcessor를 구현한 클래스가 있다면, 초기화 전에 추가적인 처리 가능예: AOP (Aspect-Oriented Programming)에서 프록시 객체를 생성 |
    | 5. 빈 초기화 (Initialization) | 빈이 InitializingBean 인터페이스를 구현했다면 afterPropertiesSet() 메서드를 호출또는, 설정 파일이나 어노테이션을 통해 지정한 커스텀 초기화 메서드가 실행이 단계에서 빈은 초기 설정을 마치고 사용 준비가 완료됨. |
    | 6. 빈 초기화 후 처리 (BeanPostProcessor) | 초기화 후에 BeanPostProcessor가 추가적인 처리 가능예: 프록시 설정, 추가 설정 적용 등 |
    | 7. 빈 사용 (Usage) | 애플리케이션에서 빈을 실제로 사용이 단계에서 빈은 비즈니스 로직을 수행 |
    | 8. 빈 소멸 (Destruction) | 애플리케이션 종료 시, 스프링 컨테이너가 빈의 소멸을 관리빈이 DisposableBean 인터페이스를 구현했다면 destroy() 메서드를 호출또는, 설정 파일이나 어노테이션을 통해 지정한 커스텀 소멸 메서드를 실행이 단계에서 리소스 해제, 연결 종료 등의 정리 작업 진행 |
- **cf. 의존성 주입**
    - ‘빈이 의존성을 주입받는다’ 라는 것은 **어떤 빈이 자신의 기능을 수행하기 위해 필요한 다른 빈 객체를 스프링 컨테이너로부터 제공받는 것** 을 의미합니다.
        - UserService 가 데이터베이스 작업을 해야 한다면, UserRepository 를 필요로 합니다.
        - 이때 UserService 는 UserRepository 에 의존한다고 합니다.
        - 이 의존관계에 있는 객체 (UserRepository) 를 스프링이 직접 만들어서 UserService 에 넣어주는 행위를 의존성 주입이라고 합니다.
        - 객체를 선언해주고, 생성자 어노테이션을 이용해 생성자를 자동 생성하여 사용합니다.

- **빈의 범위 (Scope)**
    
    빈 스코프는 스프링 컨테이너가 **빈을 생성하고 관리하는 범위와 생명 주기를 정의하는 개념**입니다.. 스코프에 따라 빈의 생성 시점, 사용 범위, 소멸 시점이 달라지며, 애플리케이션의 요구 사항에 맞게 적절한 스코프를 선택할 수 있습니다.
    

## 1.  Singleton 패턴은 무엇이며, 스프링에서 기본 스코프로 사용되는 이유는 무엇일까요?

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%201.png)

**Singleton 패턴이란?**

특정 클래스의 객체를 오직 하나만 생성하도록 보장하는 디자인 패턴, 앱이 켜질 때 한 번 만들어지고, 앱이 꺼질 때까지 계속 같은 객체를 사용합니다.

```java
@Service 
public class UserService {
```

- 이렇게 하면 스프링이 UserService 객체를 하나만 만들어서, 컨트롤러, 다른 서비스 등 애플리케이션 전역에서 공유되는 객체에 사용됩니다.
- 스프링의 기본 스코프로, 특별히 스코프를 지정하지 않으면 모든 빈은 싱글톤으로 관리됩니다.
    - 요청당 객체를 생성하는 것이 아닌, 애플리케이션이 시작될 때 단 한 번만 객체를 생성해 메모리에 올려두고, 이후 모든 요청이 그 하나의 객체를 공유하여 사용하기 때문에 → 객체 생성 비용이 절감되며 메모리를 효율적으로 사용할 수 있습니다.

## 2.  Bean Scope에는 어떤 종류가 있으며, 언제 각각을 사용할까요?

기본값인 싱글톤 스코프 이외에는 아래와 같은 종류가 있습니다.

- **프로토타입 (prototype)** : **빈의 생성부터 의존관계 주입까지** 관여되고 그 이후 사라지는 짧은 스코프
- **웹 관련**
    - request: **웹의 요청**이 들어오고 나갈 때까지 유지되는 스코프
    - session: **웹 세션**이 생성되고 종료될 때까지 유지되는 스코프
    - application: 웹의 **서블릿 콘텍스트**와 같은 범위로 유지되는 스코프

### 2-1.  프로토타입 (prototype)

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%202.png)

- 프로토타입 스코프를 스프링 컨테이너에 조회하면(클라이언트 요청이 들어왔을 때, 해당 요청을 처리하기 위해 필요한 빈을 스프링 컨테이너에게 요청하여 받는 행위), 스프링 컨테이너는 항상 새로운 인스터스를 생성하여 반환합니다.
- 결과적으로, 스프링 컨테이너는 프로토타입 빈을 생성하고, 의존 관계 주입과 초기화까지만 처리합니다. 한 번 반환된 빈에 대해서는 더이상 컨테이너가 개입하지 않습니다.
    - 기존 생성된 빈은 컨테이너에 의해 종료되지 않고, 일반 자바 객체처럼 가비지 컬렉터의 대상이 됩니다.

### 2-2.  Request

- 웹 애플리케이션에서 HTTP 요청 하나당 하나의 빈 인스턴스를 생성하고, 각 HTTP 요청마다 독립적인 빈 인스턴스를 제공하며, 요청이 완료되면 빈이 소멸됩니다.

### 2-3.  Session

- 웹 애플리케이션에서 HTTP 세션 하나당 하나의 빈 인스턴스를 생성하고, 특정 사용자의 세션 동안 빈이 유지되며, 세션이 종료되면 빈도 소멸됩니다.
- 사용자 A 와 B 가 웹 사이트 서버 하나에서 서로 완전히 독립적인 빈 인스턴스를 갖게 됩니다.

### 2-4.  Application

- 서블릿 컨텍스트 당 하나의 빈 인스턴스를 생성하고, 애플리케이션이 실행되는 동안 유지됩니다.

## 3.  Controller Layer와 Service Layer에서 Singleton과 Bean Scope 어떤 영향을 미칠까요?

 스프링 컨테이너는 기본적으로 모든 빈을 싱글톤으로 관리하기 때문에, 무상태성 객체입니다. (데이터를 저장하지 않고, 로직만 처리하는 객체) 

```java
@RestController
public class UserController {
    private String userName; 
```

- 따라서 이처럼 필드에 요청마다 달라지는 데이터를 저장하면 안 됩니다. 같은 객체를 여러 요청이 동시에 쓰기때문에, 스레드 간 데이터 충돌 문제가 생깁니다.
- 요청별 데이터는 객체의 필드에 저장하는 것이 아니라, 메서드의 파라미터를 통해 전달하고 메서드 내에서 지역변수로 처리해야 합니다.

---

# 🍀  Spring에서 요청을 어떤 방식으로 처리하나요?

<aside>
💡

### **Spring 에서 요청과 응답**

- 요청: 클라이언트→서버
    - HTTP Method, url, 프로토콜 명시
    - request 에 대한 부가 정보 (Headers)
    - request 에 대한 실제 내용 (Body)
- 응답: 서버→클라이언트
    - 응답 상태
    - 실제 응답 데이터 (Body)
    - response 에 대한 부가 정보 (Headers)
</aside>

## 1.  요청과 응답 과정

### 1-1.  Servlet

클라이언트로부터 요청이 들어오면, 서블릿이라는 프로그램이 요청 메세지를 읽고 응답을 만들어줍니다.

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%203.png)

```java
public abstract class HttpServlet extends GenericServlet {

    private static final long serialVersionUID = 1L;

    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";
    
    ...
  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String msg = lStrings.getString("http.method_get_not_supported");
    sendMethodNotAllowed(req, resp, msg);
	  }
	  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String msg = lStrings.getString("http.method_post_not_supported");
    sendMethodNotAllowed(req, resp, msg);
    }
    
    ...
```

- HttpServlet 의 일부입니다. HTTP 요청을 받아서 적절한 메서드 (doGet, doPost…) 를 실행하고 응답을 만들어 반환하는 클래스 입니다.
- 이처럼 클라이언트가 요청을 보내면, 웹 서버는 그 요청을 서블릿으로 전달합니다. HttpServlet 클래스는 이 요청을 HttpServletRequest 객체로 받아서 요청 방식을 확인하고, 그에 맞는 메서드를 실행합니다. 그 과정에서 필요한 비즈니스 로직을 호출하고 결과를 HttpServletResponse 객체에 담아 클라이언트에게 응답합니다.

### 1-2. Dispatcher Servlet

디스패처 서블릿이란, **HTTP 프로토콜로 들어오는 모든 요청을 가장 먼저 받아 적합한 컨트롤러에 위임해주는 프론트 컨트롤러(Front Controller)**라고 정의할 수 있습니다.

- 클라이언트가 HTTP 요청을 보내면, 제일 먼저 디스패처 서블릿으로 요청이 들어오게 됩니다.
- 디스패처 서블릿은 요청을 분석해 그 정보를 바탕으로 Controller 클래스의 @RequestMapping, @GetMapping 과 같은 어노테이션을 쭉 검사합니다.
- 이 메타데이터를 통해 어떤 클래스의 어떤 메서드가 처리해야 할지 결정하고 해당 메서드를 실행하도록 위임합니다.
- 컨트롤러에서 반환값을 반환하면, 다시 디스패처 서블릿이 그 결과를 받아와 클라이언트에게 반환합니다.

## 2.  Spring에서 요청을 어떻게 처리하나요?

### 2-1.  **스프링에서 객체의 직렬화와 역직렬화는 어떻게 이뤄질까요?**

<aside>
💡

💬  직렬화(Serialization) : 객체 → JSON (응답 보낼 때)

💬  역직렬화(Deserialization) : JSON → 객체 (요청 받을 때)

</aside>

```java
{
  "name": "희영",
  "age": 24
}
```

클라이언트에서 이런 JSON 요청을 보내면

```java
@PostMapping("/user")
public void createUser(@RequestBody User user) {
    System.out.println(user.getName());
```

스프링이 ObjectMapper 를 이용해 JSON 요청을 User 객체로 역직렬화 해줍니다.

👉 응답을 돌려줄 때는 반대로 User 객체를 JSON 문자열로 직렬화 합니다. (ResponstEntity 로 반환된 dto 객체 → JSON 문자열로 직렬화)

### 2-2.  **ObjectMapper의 작동방식**

ObjectMapper 는 Jackson 라이브러리의 직렬화/역직렬화 도구입니다.

- **Java Object → JSON**

```java
public void writeValue(JsonGenerator g, Object value)
```

자바 객체를 JSON 으로 직렬화 하기 위해서는 ObjectMapper 의 writeValue() 메서드를 이용합니다. 파라미터로 JSON 을 저장할 파일과 직렬화시킬 객체를 넣어주면 됩니다. (Jackson 라이브러리는 Getter 와 Setter 를 통해 prefix 를 잘라내고 필드를 식별하기 때문에, **직렬화 시킬 클래스에는 Getter 가 존재해야** 합니다.)

- **JSON → Java Object**

```java
public <T> T readValue(JsonParser p, Class<T> valueType)
```

JSON 을 자바 객체로 역직렬화 합니다. 파서 객체를 이용해 JSON 데이터를 읽어들입니다. valueType을 통해 어떤 자바 클래스 타입 객체로 만들지 지정합니다. 그렇게 지정된 클래스 타입의 객체가 생성되어 반환됩니다.

### 2-3.  @RequestBody, @ModelAttribute, @PathVariable, @RequestParam

- **@RequestBody**
    - 클라이언트가 전송하는 **JSON 형태**의 HTTP Body 를 java 객체로 변환시켜주는 역할을 합니다.

- **@ModelAttribute**
    - 클라이언트가 전송하는 **폼(form) 형태**의 HTTP Body와 요청 파라미터들을 생성자나 Setter로 바인딩하기 위해 사용됩니다.
        - HTML <form> 태그를 통해 전송되는 폼 데이터를 자바 객체 안에 넣어주는 역할

- **@PathVariable**
    - URI 경로의 일부를 메서드 매개변수에 바인딩할 때 사용합니다.

- **@RequestParam**
    - **1개의 HTTP 요청 파라미터**를 받기 위해서 사용됩니다. (?key=value 형식으로 전달)
    - required 와 defaultValue
        - @RequestParam.required : 디폴트 값은 true 이며, false 일 때는 파라미터가 넘어가지 않는 경우 null 로 채워집니다.
        - @RequestParam.defaultValue : 요청 파라미터의 디폴트 값을 설정할 수 있습니다. 빈 문자가 오면 디폴트 값이 적용됩니다.

💬  **@PathVariable vs @RequestParam**

: 특정 사용자를 조회해야 할 때는 리소스 식별자가 경로에 포함되기 때문에 PathVariable 을 사용합니다. 페이지네이션을 하거나 필터링을 할 때는 옵션, 조회 조건이 주어지기 때문에 RequestParam 을 사용합니다.

# 🍀  데이터 전달 객체란 무엇인가요? 그리고 Transaction은 무엇인가요?

## 1.  Spring에서 데이터 전달 객체로 무엇이 있나요?

### 1-1.  데이터 전달 객체를 왜 사용하나요?

계층 간 데이터 전송을 위해 도메인 모델 대신 사용되는 객체를 데이터 전달 객체 (Data Transfer Object) 라고 합니다.

- **관심사의 분리 (Separation of Concerns, SoC)**
    - Controller, Service, Repository 각 계층은 서로서로 필요로 하는 데이터의 형태와 정보 수준이 다릅니다.
    - Controller → Service (Request DTO)
        - 클라이언트가 입력한 최소한의 데이터만 필요합니다. (메뉴, 가격…)
        - 요청 유효성 검사는 dto 에서 처리되므로, service 계층은 깔끔한 데이터만 받습니다.
    - Service → Controller (Response DTO)
        - Service 는 DB 에서 가져온 모든 필드를 가지고 있지만, 클라이언트에게는 특정 필드만 보여주어야 합니다.
- **도메인 객체의 무결성 정보 은닉**
    - DB Entity 를 그대로 노출하면 내부 필드(패스워드 등) 가 유출 될 수 있기 때문에 DTO 에 필요한 필드만 담아서 전송해, 도메인 객체를 보호합니다.

### 1-2.  DTO, DAO, VO는 각각 무엇이며, 어떤 차이점이 있나요?

- DTO (Data Transfer Object)
    - 계층간 데이터 교환을 위한 객체입니다.

- DAO (Data Access Objct)
    - DB의 데이터에 접근하기 위한 객체 입니다. (Service 와 DB 를 연결하는 연결고리 역할)
    - Repository package 가 DAO 라고 볼 수 있습니다.

- VO (Value Object)
    - Read-Only 속성을 가진 값 오브젝트 입니다.
        - 개발자가 equals() 와 hashCode() 메서드를 오버라이드해, 두 객체가 가진 값이 같으면 두 객체를 논리적으로 동일하게 취급하도록 강제합니다. (원래는 값이 같아도 메모리 주소가 다르면 다른 객체)
    - VO 는 불변 클래스이기 때문에, getter 기능만 존재합니다.
    

## 2.  **Transaction은 무엇인가요?**

<aside>
💡

**트랜잭션**은 여러 데이터베이스 작업(ex. 여러 UPDATE/INSERT)을 하나의 단위로 묶어, 모두 성공하면 커밋(반영)되고, 하나라도 실패하면 모두 취소(롤백)되게 보장하는 메커니즘입니다.

</aside>

### 2-1.  ACID 원칙은 무엇인가요?

- **Aromicity (원자성)**
    - 트랜잭션은 모두 실행되거나, 모두 실행되지 않거나 둘 중 하나여야 합니다.
- **Consistency (일관성)**
    - 트랜잭션 실행 전후로도 항상 일관된 데이터 구조 및 제약을 가져야 합니다.
- **Isolation (격리성)**
    - 각 트랜잭션은 독립된 상태여야 합니다. A 와 B 트랜잭션이 동시에 실행되더라도, 서로 간에 영향을 주지 않아야 합니다.
- **Durability (지속성)**
    - 적용된 상태가 계속해서 지속되어야 합니다. 한번 commit 된 내용은 영구적으로 저장되어야 합니다.

### 2-2.  Spring에서는 Transaction을 어떤 방식으로 관리하나요? (**@Transactional**)

스프링에서는 @Transactional 어노테이션을 통해 트랜잭션을 관리할 수 있습니다. @Transactional 어노테이션은 클래스 또는 메소드에 선언이 가능합니다. 

- 트랜잭션 시작: 메서드가 실행되기 전에 트랜잭션을 시작
- 비즈니스 로직 수행: 메서드 내부의 데이터 변경 작업 수행
- 정상 종료 → commit()  실행
- 예외 발생 → rollback() 실행

---

# ✅  week5 과제

## # Cart

### 1. getCart (장바구니 조회)

`@RequestMapping("/users/{userId}/cart")` 

```java
// controller 
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        CartResponseDto response = cartService.viewCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
// Service 
    public CartResponseDto viewCart(Long userId) {

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저의 장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_NOT_FOUND));

        return CartResponseDto.from(cart);
    }
```

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%204.png)

- 현재 유저가 가진 현재 장바구니를 보기 위해 userId 를 파라미터로 받습니다.
- 유저를 확인한 뒤, 해당 유저가 장바구니를 가지고 있는지 조회 합니다.
    
    ```java
        @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartMenuList WHERE c.user.id = :userId")
        Optional<Cart> findByUserIdWithMenus(@Param("userId") Long userId);
    ```
    
    - userId 와 일치하는 User 를 가진 Cart 로 필터링하여, Cart 와 연관 관계인 cartMenuList 를 left join 을 통해 함께 조인합니다.
    - **cf. left join**
        - 왼쪽 테이블 (cart) 의 모든 레코드를 포함하고, 오른쪽 테이블 (cartmenu) 에서 일치하는 레코드만 포함합니다.
        - 해당 userId 를 가진 cart 가 장바구니 항목 (cartMenu) 를 가지고 있지 않더라도 (리스트가 비어있더라도) cart 객체 자체는 조회되어야 하기 때문에 사용합니다.
    - fetch : cart 객체를 가져온 후 코드에서 cart.getCartMenuList() 를 호출할 때 n+1 쿼리가 발생하는 것을 방지합니다.
- CartResponseDto 에 Cart 객체를 담아 반환합니다.
    - CartResponseDto
        - CartMenuList 에 들어갈 CartMenu 에 대한 정보는 메뉴와 수량 뿐입니다. 따라서 Dto 를 만들어 전송해주도록합니다. CartMenuList 를 스트림으로 순회하며 각 CartMenu 엔티티를 CartMenuDto 로 변환합니다.
        
        ```java
        package server.Heeyoung.domain.Cart.dto.response;
        
        import lombok.Builder;
        import lombok.Getter;
        import server.Heeyoung.domain.Cart.entity.Cart;
        import server.Heeyoung.domain.CartMenu.entity.CartMenu;
        import server.Heeyoung.domain.Store.entity.Store;
        
        import java.util.List;
        import java.util.stream.Collectors;
        
        @Getter
        @Builder
        public class CartResponseDto {
        
            private final Long cartId;
        
            private final List<CartMenuDto> cartMenuList;
        
            private final Long storeId;
        
            public static CartResponseDto from(Cart cart) {
                return CartResponseDto.builder()
                        .cartId(cart.getId())
                        .cartMenuList(
                                cart.getCartMenuList().stream()
                                        .map(cm -> CartMenuDto.builder()
                                                .menuName(cm.getMenu().getMenuName())
                                                .quantity(cm.getCartMenuQuantity())
                                                .build())
                                        .collect(Collectors.toList())
                        )
                        .storeId(cart.getStore().getId())
                        .build();
            }
        
        }
        ```
        

## # CartMenu

### 3.  addMenuToCart (장바구니에 메뉴 추가)

`@RequestMapping("/cart/{cartId}/items")`

```java
// controller
    @PostMapping
    public ResponseEntity<CartMenuResponseDto> addMenuToCart(
            @PathVariable("cartId") Long cartId,
            @RequestParam Long userId,
            @RequestParam Long storeId,
            CartMenuRequestDto dto
    ) {
        CartMenuResponseDto response = cartMenuService.addMenuToCart(dto, userId, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
// service
    @Transactional
    public CartMenuResponseDto addMenuToCart(CartMenuRequestDto dto, Long userId, Long storeId) {

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new RestApiException(ErrorCode.STORE_NOT_FOUND));

        // 유저의 기존 장바구니 확인
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        Cart cart;
        // 유저가 해당 가게에 대한 장바구니가 있다면
        if (existingCart.isPresent()) {
            Cart existing = existingCart.get();

            // 다른 가게의 장바구니인 경우
            if (!existing.getStore().getId().equals(storeId)) {
                throw new RestApiException(ErrorCode.DIFFERENT_STORE_CART_EXISTS);
            }

            cart = existing;
        } else {
            // 새 장바구니 생성
            cart = cartRepository.save(Cart.builder()
                    .user(user)
                    .store(store)
                    .build());
        }

        // 메뉴 조회
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(()->new RestApiException(ErrorCode.MENU_NOT_FOUND));

        // 장바구니 메뉴 객체 생성
        CartMenu cartMenu = CartMenu.builder()
                .cartMenuQuantity(dto.getCartMenuQuantity())
                .menu(menu)
                .cart(cart)
                .build();

        // 저장
        cartMenuRepository.save(cartMenu);

        return CartMenuResponseDto.from(cartMenu);
    }
```

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%205.png)

- 요청 파라미터는 `userId` (장바구니 주인, 로그인된 사용자), `storeId` (메뉴를 추가하려는 가게), `dto` (추가하려는 메뉴 정보) 입니다.
- **Controller 에 요청**
    - 클라이언트가 `POST /cart/{cartId}/items?userId=1&storeId=3` 로 요청을 보냅니다
    - 요청 바디에는 메뉴id 와 수량이 포함됩니다.
- **Service 흐름**
    - 유저와 가게를 조회하여 존재를 확인합니다. (없으면 404 예외 발생)
    - 사용자가 이미 장바구니를 가지고 있는지 판단합니다. Null 값을 허용하기 때문에 옵셔널로 선언합니다. (existhingCart)
    - 한 유저는 동시에 서로 다른 가게의 장바구니를 가질 수 없습니다. 따라서 existingCart 가 존재하면, existingCart 의 Cart 객체 (기존 장바구니) 가 파라미터로 받은 storeId 와 같은지 판단합니다. (다른 가게의 것이라면 예외를 발생시킵니다.)
    - 같은 가게면 기존 장바구니를 사용합니다.
    - existingCart 가 존재하지 않으면 새 장바구니를 생성합니다.
    - 메뉴를 조회합니다. (없으면 404 예외 발생)
    - CartMenu 를 생성하고 ResponseDto 로 반환합니다.

### 4.  updateCartMenuQuantity (장바구니에 메뉴 수량 수정)

`@PutMapping("/{cartMenuId}")`

```java
// controller
    @PutMapping("/{cartMenuId}")
    public ResponseEntity<CartMenuResponseDto> updateCartMenuQuantity(
            @PathVariable("cartMenuId") Long cartMenuId,
            @RequestParam Long userId,
            @RequestParam Long newQuantity
    ) {
        CartMenuResponseDto response = cartMenuService.updateCartMenuQuantity(userId, cartId, cartMenuId, newQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
// service 
    @Transactional
    public CartMenuResponseDto updateCartMenuQuantity(Long userId, Long cartMenuId, Long newQuantity) {

        // 수량 검증
        if (newQuantity <= 0) {
            throw new RestApiException(ErrorCode.INVALID_QUANTITY);
        }

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_MENU_NOT_FOUND));

        // 본인 소유 검증
        if (!cartMenu.getCart().getUser().getId().equals(userId)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 수량 업데이트
        cartMenu.updateQuantity(newQuantity);

        return CartMenuResponseDto.from(cartMenu);
    }
```

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%206.png)

- **Service 흐름**
    - `newQuantity` 파라미터를 통해 받은 값(수량) 이 0 이하인 경우 예외를 발생시킵니다.
    - 장바구니에 메뉴가 존재하는지 확인합니다.
    - `if (!cartMenu.getCart().getUser().getId().equals(userId))` 를 통해 장바구니 메뉴가 현재 요청한 유저의 것인지 확인합니다.
    - CartMenu 엔티의 수량 필드를 새로운 값으로 변경합니다.
    - CartMenuResponse 로 변환하여 전송합니다.

### 5.  deleteCartMenu (장바구니 메뉴 삭제)

```java
// controller 
    @DeleteMapping
    public ResponseEntity<String> deleteCartMenu(
            @PathVariable("cartMenuId") Long cartMenuId,
            @RequestParam Long userId
    ) {
        cartMenuService.deleteCartMenu(userId, cartMenuId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 메뉴가 삭제되었습니다.");
    }
    
// service
        @Transactional
    public void deleteCartMenu(Long userId, Long cartMenuId) {

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(() -> new RestApiException(ErrorCode.CART_MENU_NOT_FOUND));

        // 본인 소유 확인
        if (!cartMenu.getCart().getUser().getId().equals(userId)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 삭제
        cartMenuRepository.delete(cartMenu);
    }
```

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%207.png)

- Service 흐름
    - 해당 메뉴가 실제로 장바구니에 존재하는지 확인합니다. (삭제하려는 메뉴가 이미 삭제되었거나, 존재하지 않는 경우 허용 x)
    - 삭제하려는 메뉴가 사용자의 장바구니에 속한 메뉴인지 검증합니다.
    - 메뉴를 삭제하고 클라이언트에게는 삭제 메세지를 날립니다.

## # Menu

### 6.  createMenu (메뉴 등록)

`@RequestMapping("/stores/{storeId}/menus)"` 

```java
// controller
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDto dto
    ) {
        MenuResponseDto response = menuService.createMenu(dto, storeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
// service
    @Transactional
    public MenuResponseDto createMenu(MenuCreateRequestDto dto, Long storeId) {
        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new RestApiException(ErrorCode.STORE_NOT_FOUND));

        // 메뉴 객체 생성
        Menu menu = Menu.builder()
                .menuName(dto.getMenuName())
                .price(dto.getPrice())
                .menuPicture(dto.getMenuPicture())
                .build();

        // store 의 addMenu 메서드로 메뉴 추가
        store.addMenu(menu);

        // 저장
        menuRepository.save(menu);

        // 응답 dto 에 담아서 반환
        return MenuResponseDto.from(menu);
    }
```

![image.png](%5BApp%20center%5D%204%EC%A3%BC%EC%B0%A8%20-%20Controller%20&%20Service%20Layer/image%208.png)

- **Service 흐름**
    - 가게 존재를 확인합니다.
    - 메뉴 객체를 생성합니다.
    - Store 의 menuList 에도 menu 를 추가해줍니다.
    - 메뉴를 저장하고 ResponseDto 로 반환합니다.