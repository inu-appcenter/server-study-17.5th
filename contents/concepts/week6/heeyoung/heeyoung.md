# [App center] 6주차 - Validation & Exception Handling

# 🍀 유효성 검사는 무엇이고 Spring에서 어떻게 적용할 수 있나요?

# 1.  유효성 검사란?

## 1-1.  유효성 검사의 의미와 이유는 무엇인가요?

### 유효성 검사의 의미 (validation)

유효성 검사란, 데이터가 어떤 특정 조건에 맞는 값을 가질 수 있도록 검사하는 것을 의미합니다. 서버에 데이터를 제출하거나, 사용자를 인증할 때 시행됩니다. 

### 유효성 검사를 하는 이유

- 서버에서의 유효성 검사는 DB 에 저장되기 전에 데이터를 검증하여 **데이터의 무결성을 유지**할 수 있습니다.
- 잘못된 데이터가 시스템에 저장되거나 처리되는 것을 방지해 **에러 발생 가능성**을 줄입니다.
- 악의적인 입력으로부터 **시스템을 보호**합니다.

# 2.  Spring에서의 유효성 검사는 어떻게 할까요?

## 2-1.  어디에서 유효성 검사를 실시해야 할까요?

유효성 검사는 주로 3단계에서 일어날 수 있습니다. 각각의 layer 에 적절한 유효성 검사를 위치시켜야 책임이 분산됩니다.

### 🌀  Controller layer

사용자의 요청 (RequestBody, RequestParam 등 사용자 요청) 이 들어올 때 잘못된 값이 없는지 검사합니다.

→ 클라이언트가 보낸 request 에 담긴 데이터가 service 에게 넘겨주기 올바른 형태인가? 를 검사해야 합니다.

```jsx
{
    "email": "heeyoung@gmail.com",
    "age": 24,
}
```

위와 같은 데이터를 기대했다면 다음과 같은 항목을 검증할 수 있습니다.

- email 이 string 이며, @NotBlank 여야함
- age 는 int 이며 @Min 1 이상인 형식이어야함

이런 검증은 **`DTO`** 를 이용해 수행합니다. Controller 는 DTO 에 @Valid 어노테이션을 붙여 Spring 이 자동으로 검증하게 합니다.

```jsx
public class UserCreateDTO {

    @NotBlank(message = "필수 입력 값입니다.")
    private String email;
    
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;
}
```

### 🌀  Service layer

Controller 에서 받은 Input 으로 비즈니스 로직을 수행하는 레이어 입니다. 

```jsx
public class CartService {
    // 장바구니 조회
    @Transactional(readOnly = true)
    public CartResponseDto findCart(Long userId) {

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저의 장바구니 조회
        Cart cart = cartRepository.findByUserIdWithMenus(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_NOT_FOUND));
}
```

- Service layer 에서 하는 유효성 검사는 요청 데이터 형식이 올바른지가 아닌, **비즈니스 규칙 위반을 확인**하는 과정입니다.
- 단순히 잘못된 입력이 아닌, 시스템이 처리할 수 없는 상황을 취급하기 때문에 **예외(Exception)로 처리**하게 됩니다.

### 🌀   Entity layer

엔티티 단에서는 DB 의 무결성을 보장하기 위해 유효성 검사를 시행할 수 있습니다.

```jsx
public class User {
    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
}
```

## 2-2.  Bean Validation은 무엇이며 왜 등장하게 됐나요?

<aside>
💡

**Bean Validation 이란?**

특정 필드에 대한 검증 로직을 모든 프로젝트에 적용할 수 있도록 표준화한 것입니다.

`@NotBlank`, `@Email`, `@Min`, `@Size` 같은 것들이 모두 Bean Validation 규약을 따른 것입니다.

검사 로직 대신, **어노테이션 선언만으로 공통적인 검증 로직을 재사용**할 수 있도록 등장했습니다.

</aside>

다음 의존성을 추가하여 사용합니다.

```jsx
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

## 2-3.  @Valid와 @Validated 의 차이는 무엇인가요?

두 어노테이션 모두 유효성 검사를 편하게 하기 위해 사용합니다.

### 2-3-1.  @Valid

@Valid 는 JSR-303 표준 (자바 표준) 스펙으로써, **Bean Validation 을 이용해 객체의 제약 조건을 검증하도록 지시하는 어노테이션**입니다.

### 사용 예시

```jsx
// DTO
// email, age 필드에 대해 유효성 검증 수행 
public class UserSignUpDTO {

    @NotBlank(message = "필수 입력 값입니다.")
    private String email;
    
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;
}
```

```jsx
// 컨트롤러
// 메서드 인자에 @Valid 지정 -> 요청이 들어오면 해당 인자에 대한 유효성 검사 실행
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUpUser(@Valid @RequestBody UserSignUpDto dto) {
        userAuthService.signUp(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공하였습니다.");
    }
```

### 동작 원리

1. 모든 요청은 프론트 컨트롤러인 디스패처 서블릿을 통해 컨트롤러로 전달됩니다. 이 때 컨트롤러에서 JSON 형식의 데이터를 받는 @ResponseBody 어노테이션을 사용하는 경우,  JSON 을 Java 객체 (DTO) 로 바꿔주는 과정이 필요합니다.
2. *HttpMessageConverter* 가 이 변환을 담당하고, *ArgumentResolver* 가 DTO 를 컨트롤러 메서드의 인자로 주입합니다. (이 때 DTO는 아직 검증 전 상태입니다.)
3. 이 과정 중에 @Valid 가 붙어있다면 *RequestResponseBodyMethodProcessor* 가 해당 요청을 처리하게 됩니다. 
    - *RequestResponseBodyMethodProcessor*
        
        ```jsx
        	// RequestResponseBodyMethodProcessor
        	@Override
        	@Nullable
        	public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
        			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        
        		parameter = parameter.nestedIfOptional();
        		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
        
        		if (binderFactory != null) {
        			String name = Conventions.getVariableNameForParameter(parameter);
        			ResolvableType type = ResolvableType.forMethodParameter(parameter);
        			WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name, type);
        			if (arg != null) {
        				**validateIfApplicable(binder, parameter);**
        				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
        					throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
        				}
        			}
        			if (mavContainer != null) {
        				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        			}
        		}
        
        		return adaptArgumentIfNecessary(arg, parameter);
        	}
        ```
        
        - 해당 클래스의 *resolveArgument()* 메서드 내부에서 유효성 검증이 진행되며, 문제가 있을 경우*MethodArgumentNotValidException* 예외를 발생시키게 됩니다.
        
        ```jsx
        	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        		Annotation[] annotations = parameter.getParameterAnnotations();
        		for (Annotation ann : annotations) {
        			**Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);**
        			if (validationHints != null) {
        				**binder.validate(validationHints);**
        				break;
        ```
        
        - 유효성 검증이 일어나는 validateIfApplication() 메서드입니다.
        - 유효성 검증을 위한 도구를 찾아오기 위해 추상 클래스인 *ValidationAnnotationUtils*의 *determineValidationHints()* 메서드가 실행됩니다.
        - 해당 부분을 통해 가져온 검증 도구를 가지고, 최종적으로 validate() 메서드를 통해 검증을 진행합니다.
            - **validate() 이후…**
                
                binder.validate(validationHints); 가 호출되면, 내부적으로 스프링은 이 Bean validation 어댑터를 부릅니다. (여기서부터는 Spring → Hibernate Validator 로 넘어감!)
                
                1. Hibernate Validator 는 Bean Validation 의 구현체로, validate() 를 호출하면 Hibernate Validator 이 객체의 모든 필드에 달린 제약을 검사합니다. 
                2. @Email 에 대한 검증을 수행한다고 할 때, Hibernate Validator 는 @Email 어노테이션을 읽은 뒤 이 어노테이션에 연결된 Validator 클래스를 찾아서 호출합니다. → EmailValidator 클래스 실행
                
                ```jsx
                public class EmailValidator extends AbstractEmailValidator<Email>
                ```
                
                - EmailValidator 를 상속 구조를 보면, 실제 검증 로직은 부모 클래스인 AbstractEmailValidator 안에 들어있고, EmailValidator 는 거기에 추가적인 정규식 옵션 처리만 덧붙인 클래스임을 알 수 있습니다.
                
                ```jsx
                public class AbstractEmailValidator<A extends Annotation> implements ConstraintValidator<A, CharSequence> {
                
                	private static final int MAX_LOCAL_PART_LENGTH = 64;
                
                	private static final String LOCAL_PART_ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uFFFF-]";
                	private static final String LOCAL_PART_INSIDE_QUOTES_ATOM = "(?:[a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uFFFF-]|\\\\\\\\|\\\\\\\")";
                	/**
                	 * Regular expression for the local part of an email address (everything before '@')
                	 */
                	private static final Pattern LOCAL_PART_PATTERN = Pattern.compile(
                			"(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" +
                					"(?:\\." + "(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" + ")*", CASE_INSENSITIVE
                	);
                ```
                
                *AbstractEmailValidator* 에 **기본 이메일 패턴 정규식**이 있고,
                
                *EmailValidator* 는 그 위에 **사용자 지정 정규식(@Email(regexp=”…”))** 을 추가로 검사하는 구조!
                
        
4. 검증에 오류가 있다면 MethodArgumentNotValidException 예외가 발생하게 되고, 디스패처 서블릿에 기본으로 등록된 DefaultHandlerExceptionResolver 에 의해 400 BadRequest 에러가 발생합니다.

→  @Valid 는 기본적으로 **컨트롤러에서만 동작**하고, 다른 계층에서는 검증되지 않습니다.

다른 계층의 파라미터를 검증하기 위해서는 @Validated 를 사용합니다.

### 2-3-1.  @Validated

컨트롤러가 아닌 다른 계층에서 파라미터를 검증해야 하는 경우 사용합니다. 

- AOP(Aspect Oriented Programming) 기반으로 메서드의 요청을 가로채 유효성 검증을 진행합니다.
    - **AOP란?**
        
        ![image.png](%5BApp%20center%5D%206%EC%A3%BC%EC%B0%A8%20-%20Validation%20&%20Exception%20Handling/image.png)
        
        위와 같이 흩어진 관심사를 Aspect 로 모듈화하고 핵심적인 비즈니스 로직에서 분리해 재사용하겠다는 것입니다.
        
        Aspect:  여러 메서드나 클래스에서 공통으로 실행해야 할 로직을 따로 분리해서 관리하는 단위입니다. 
        
        공통 관심사인 **로깅** 을 구현한 Aspect 클래스 예시입니다.
        
        ```jsx
        @Aspect
        @Component
        public class LoggingAspect {
        
            // 메서드 실행 전에 로그 찍기
            @Before("execution(* com.example.service.*.*(..))")
            public void logBefore(JoinPoint joinPoint) {
                System.out.println("메서드 시작: " + joinPoint.getSignature().getName());
            }
        
            // 메서드 실행 후에 로그 찍기
            @After("execution(* com.example.service.*.*(..))")
            public void logAfter(JoinPoint joinPoint) {
                System.out.println("메서드 종료: " + joinPoint.getSignature().getName());
            }
        }
        ```
        
        | 개념 | 설명 |
        | --- | --- |
        | **Aspect** | 공통 기능(로깅, 트랜잭션 등)을 모아둔 클래스 |
        | **Target** | Aspect 를 적용하는 곳 (클래스, 메서드…) |
        | **Advice** | 공통 기능이 실행되는 시점의 코드 (@Before, @After 등) |
        | **Pointcut** | 어떤 메서드에 적용할지 지정 |
        | **JoinPoint** | 실제 실행되는 메서드 지점 |
- JSR 표준 기술이 아니며, Spring Framework 에서 제공하는 어노테이션 및 기능입니다.
- 제약 조건이 적용될 검증 그룹을 지정할 수 있습니다.
- 유효성 검증에 실패하면 ConstraintViolationException 예외가 발생합니다.

### 사용 예시

클래스에 @Validated 를 붙이고, 파라미터에 유효성 검증 어노테이션을 붙여주면 됩니다.

```jsx
@Service
@Validated
public class OrderService {
    public void createOrder(@NotNull Long userId) {
        // userId가 null이면 MethodValidationException 발생
    }
}
```

### 동작 원리

1. 클래스에 유효성 검증 AOP 가 적용되도록 @Validated 를, 검증을 진행할 메서드에는 유효성을 검증할 어노테이션을 선언합니다.
    1. 스프링이 해당 클래스에 유효성 검증 AOP 를 등록합니다.
2. 해당 클래스의 메소드가 호출될 때 AOP 가 요청을 가로채서 유효성 검증을 진행합니다.

## 2-4.  @Validated는 Controller에서 언제, 어떤 상황에 쓰일까요?

@Validated 는 @Valid 과 다르게 **특정 검증 그룹(Group)** 을 지정할 수 있습니다. 같은 DTO 를 쓰더라도 상황에 따라 검증 규칙을 다르게 적용하고 싶을 때 사용하는 기능입니다.

검증 그룹을 지정하기 위해서는 **마커 인터페이스** 를 생성하여 사용합니다.

마커 인터페이스란, 메서드를 가지지 않고 특정기능이나 속성을 표시하기 위해 사용되는 인터페이스 입니다.

1. **그룹 인터페이스 정의**
    
    ```java
    public interface ValidationGroup1 {}
    public interface ValidationGroup2 {}
    ```
    
    - 이 검증은 Group1 에서만 동작하고, 이 검증은 Group2 에서만 동작함을 표시하기 위한 인터페이스입니다.
    
2. **도메인 모델 내 그룹 지정**
    
    아래와 같이 그룹을 지정합니다. groups 속성을 지정하지 않은 필드에는 DafaultGroup 이 자동으로 적용됩니다. (기본그룹)
    
    ```java
    public class UserSignUpDto {
    
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String loginId;
    
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함해 8자 이상이어야 합니다."
        )
        private String password;
    
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = "전화번호 형식은 010-XXXX-XXXX 이어야 합니다."
        )
        private String phoneNum;
    
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        private String name;
    
        **// Group1
        @Size(max = 20, message = "닉네임은 20자를 넘을 수 없습니다.", groups = ValidationGroup1.class)
        private String nickname;
    
        // Group2
        @NotBlank(message = "주소는 필수 입력 값입니다.", groups = ValidationGroup2.class)
        private String address;**
    }
    ```
    

1. **Controller 에서 실제로 검증 그룹 선택**
    
    ```java
    @PostMapping("/test1")
    public ResponseEntity<String> testGroup1(
        @Validated(ValidationGroup1.class) @RequestBody UserSignUpDto dto
    ) {
        ...
    }
    
    @PostMapping("/test2")
    public ResponseEntity<String> testGroup2(
        @Validated(ValidationGroup2.class) @RequestBody UserSignUpDto dto
    ) {
        ...
    }
    
    ```
    
    - Controller 에서 @Validated 에 어떤 그룹을 지정하느냐에 따라 검증이 다르게 동작합니다.
    - /test1 요청 시, DefaultGroup 과 ValidationGroup1 의 필드에서 검증이 시행됩니다.
    - /test2 요청 시, DefaultGroup 과 ValidationGroup2 의 필드에서 검증이 시행됩니다.

## 2-5.  유효성 검사를 위한 어노테이션은 어떤 것들이 있을까요? (@Email, @NotNull 등)

| 어노테이션 | 설명 |
| --- | --- |
| `@Null` | null만 허용한다. |
| `@NotNull` | 빈 문자열(`""`), 공백(`" "`)은 허용하되, null은 허용하지 않음 |
| `@NotEmpty` | 공백(`" "`)은 허용하되, null과 빈 문자열(`""`)은 허용하지 않음 |
| `@NotBlank` | null, 빈 문자열(`""`), 공백(`" "`) 모두 허용하지 않는다. |
| `@Email` | 이메일 형식을 검사한다. 단, 빈 문자열(`""`)의 경우엔 통과시킨다. (`@Pattern`을 통한 정규식 검사를 더 많이 사용) |
| `@Pattern(regexp = )` | 정규식 검사를 수행할 때 사용한다. |
| `@Size(min=, max=)` | 문자열, 컬렉션 등의 길이를 제한할 때 사용한다. |
| `@Max(value = )` | 지정된 value 이하의 값만 허용한다. |
| `@Min(value = )` | 지정된 value 이상의 값만 허용한다. |
| `@Positive` | 값이 양수여야 한다. |
| `@PositiveOrZero` | 값이 0 또는 양수여야 한다. |
| `@Negative` | 값이 음수여야 한다. |
| `@NegativeOrZero` | 값이 0 또는 음수여야 한다. |
| `@Future` | 현재 시각보다 미래의 날짜/시간이어야 한다. |
| `@FutureOrPresent` | 현재 또는 미래의 날짜/시간이어야 한다. |
| `@Past` | 현재 시각보다 과거의 날짜/시간이어야 한다. |
| `@PastOrPresent` | 현재 또는 과거의 날짜/시간이어야 한다. |

## 2-6.  중첩 객체의 유효성 검사는 어떻게 할까요? (@Valid의 중첩 적용)

<aside>
💡

**중첩 객체란?**

DTO 안에 또 다른 객체가 있는 것을 의미합니다. 중첩 객체의 유효성 검사란, 그 내부 객체까지 유효성 검사를 진행하는 것을 의미합니다.

```java
@Builder
public class CartResponseDto {
		// 중첩객체
    private final List<CartMenuDto> cartMenuList;
}
```

</aside>

- 위에서 List<CartMenuDto> 는 DTO 안에 또 다른 객체(DTO) 들의 리스트가 들어있는 구조입니다.
    
    ```java
    public class CartMenuDto {
        @NotBlank
        private final String menuName;
    
        @NotBlank
        private final Long quantity;
    }
    ```
    

- 위처럼 중첩된 객체일 경우 유효성 검사를 적용하고 싶다면, 중첩 객체를 포함하고 있는 바깥 DTO 필드에 @Valid 어노테이션을 붙입니다.
    
    ```java
    public class CartResponseDto {
        @Valid
        private final List<CartMenuDto> cartMenuList;
    }
    ```
    

- Validator 가 해당 필드를 객체로 인식하고, 내부 필드까지 재귀적으로 유효성 검증을 시행합니다.

---

# 🍀 에러와 예외의 차이는 무엇일까요? Spring에서의 예외처리는 어떻게 진행할까요?

# 1.  예외 처리의 개념

## 1-1.  에러와 예외의 차이는 무엇인가요?

<aside>
💡

- **오류 (Error)**
    
    오류 (Error) 는 시스템이 종료되어야 할 수준의 상황과 같이 수습할 수 없는 심각한 문제를 의미합니다.
    
- **예외 (Exception)**
    
    예외 (Exception) 는 개발자가 구현한 로직에서 발생한 실수나 사용자의 영향에 의해 발생합니다. 오류와 달리 개발자가 미리 예측해서 방지할 수 있기에, 상황에 맞는 예외처리 (Exception Handle) 를 해야 합니다.
    

⇒  예외가 발생하면 프로그램이 종료된다는 것은 에러와 동일하지만, 예외는 예외처리를 통해 프로그램을 종료시키기 않고 정상적으로 작동되게 만들어줄 수 있습니다.

</aside>

## 1-2.  예외 처리의 방법  (예외 복구, 예외 처리 회피, 예외 전환, 예외 전파)

### 1-2-1.  예외 복구

예외 복구는 예외 상황을 파악하고 문제를 해결하여 정상 상태로 돌려놓는 방법입니다. 예외를 잡아서 일정 시간이나 조건만큼 대기하고 재시도를 반복합니다. 최대 재시도 횟수를 넘기게 될 경우에 예외를 발생시킵니다.

```java
final int MAX_RETRY = 100;

public Object someMethod() {
    int maxRetry = MAX_RETRY;
    while (maxRetry > 0) {
        try {
            ...
        } catch (SomeException e) {
            // 로그 출력. 정해진 시간만큼 대기한다.
        } finally {
            // 리소스 반납 및 정리 작업
        }
    }
    // 최대 재시도 횟수를 넘기면 직접 예외를 발생시킨다.
    throw new RetryFailedException();
}
```

예외가 발생하더라도 어플리케이션의 로직은 정상적으로 실행이 되게 처리한다는 의미입니다. 예외가 발생하면 일정시간 동안 대기 시킨 후 해당 로직을 재시도합니다. 

⇒ 대부분의 상황에서 예외를 복구할 수 있는 경우는 거의 없기 때문에 자주 사용되지 않습니다.

### 1-2-2.  예외 처리 회피

예외 처리를 직접 담당하지 않고 호출한 쪽으로 던져 회피하는 방법입니다.

```java
// 예시1. 메서드 선언부에 예외 명시 
public void add() throws SQLException {
    // ...생략
}
public void callAdd() {
    try {
        add();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// 예시2. try-catch 후 예외 재던지기 
public void add() throws SQLException {
    try {
        // ... 생략
    } catch(SQLException e) {
        // 로그를 출력하고 다시 날린다!
        throw e;
    }
}
```

호출한 쪽으로 예외를 던져버리기 때문에, 어딘가에서는 결국 예외를 처리해야 합니다.

### 1-2-3.  예외 전환

예외 회피와 비슷하게 메서드 밖으로 예외를 던지지만, 그냥 던지지 않고 적절한 예외로 전환해서 넘기는 방법입니다.

```java
// 조금 더 명확한 예외로 던진다.
public void add(User user) throws DuplicateUserIdException, SQLException {
    try {
        // ...생략
    } catch(SQLException e) {
        if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
            throw DuplicateUserIdException();
        }
        else throw e;
    }
}

// 예외를 단순하게 포장한다.
public void someMethod() {
    try {
        // ...생략
    }
    catch(NamingException ne) {
        throw new EJBException(ne);
    }
    catch(SQLException se) {
        throw new EJBException(se);
    }
    catch(RemoteException re) {
        throw new EJBException(re);
    }
}
```

- 조금 더 명확한 의미로 전달되기 위해 적합한 의미를 가진 예외로 변경합니다.
- 예외 처리를 단순하게 만들기 위해 포장(wrap) 할 수 있습니다.
- 또한 Checked Exception 이 발생했을 경우 이를 Unchecked Exception 으로 전환하여 호출한 메서드에서 예외처리를 일일이 선언하지 않아도 되도록 처리할 수도 있습니다.
- 즉, **발생한 예외에 대해서 또 다른 예외로 변경하여 던지는 것**입니다.

### 1-2-4.  예외 전파

예외가 다른 계층으로 전달될 때, 이전 에외를 원인으로 가지는 새로운 예외를 던지는 것을 예외 전파라고 합니다. 이를 이용한 **예외 체이닝**이라는 방법이 존재하는데, **예외가 전달될 때마다 새로운 예외에 포함시켜 다시 던지는 과정**입니다.

0으로 나누려는 시도로 `ArithmeticException` 을 throw 하는 메서드가 있다고 하면, 사실 이것의 실제 원인은 나누는 수를 0으로 설정함으로써 일어난 `IOException` 입니다. 하지만 메서드에선 `ArithmeticException` 을 던질 것이고 `IOException` 에 대해선 모르게 되는 것입니다. 이럴 때 예외 체이닝을 사용합니다.

```java
public class MyChainedException {

    public void main(String[] args) {
        try {
            throw new ArithmeticException("Top Level Exception.") // 현재 발생한 예외 설정
              .initCause(new IOException("IO cause.")); // Exception의 근본적 원인 설정
        } catch(ArithmeticException ae) {
            System.out.println("Caught : " + ae); // 현재 원인이 출력됨.
            System.out.println("Actual cause: "+ ae.getCause()); // 근본적인 원인이 출력됨.
        }
    }    
}

// 결과
Caught: java.lang.ArithmeticException: Top Level Exception.
Actual cause: java.io.IOException: IO cause.
```

`getCause()` : 현재 예외와 관련된 실제 원인을 반환합니다.

`initCause()` : Exception 을 호출해 근본적인 원인을 설정합니다.

## 1-3.  자바의 예외 클래스는? (Checked Exception / Unchecked Exception)

### 예외 클래스 상속 구조

```java
Object
 └── Throwable   ← 모든 에러와 예외의 조상
      ├── Error                     ← 시스템 에러 (개발자가 처리 ❌)
      └── Exception                 ← 예외 (개발자가 처리 ⭕)
           ├── Checked Exception     ← 반드시 처리해야 함 (try-catch or throws)
           └── RuntimeException      ← Unchecked Exception (명시적 처리 선택)
```

### Exception 의 2가지 종류

![image.png](%5BApp%20center%5D%206%EC%A3%BC%EC%B0%A8%20-%20Validation%20&%20Exception%20Handling/image%201.png)

1. **Checked Exception**
    - **예외처리가 필수**이며, 처리하지 않으면 컴파일되지 않습니다.
        - try-catch 로 감싸거나 throw 로 던져서 예외처리합니다.
    - 컴파일 시점에서 확인됩니다.
    - 예외 발생 시 롤백 하지 않습니다.
    - `IOException`, `ClassNotFoundException` …
2. **Unchecked Exception**
    - **명시적으로 예외처리를 하지 않아도 됩니다.**
    - RuntimeException 하위의 모든 예외 입니다.
    - 예외 발생시 트랜잭션을 롤백 합니다.
    - `NullPointerException` , `ClassCastException` …

### 예외 클래스

- `ArithmeticExcepton` : 정수를 0 으로 나누는 경우에 발생합니다.
- `ArrayIndexOutOfBoundsException` : 배열에서 인덱스 범위를 초과하는 경우에 발생합니다.
- `NullPointerException` : null값의 참조 변수로 객체접근연산자(.)를 사용하는 경우 발생합니다.
- `NumberFormatException` : 문자열 데이터를 숫자로 변경하는 과정에서 발생합니다.

이들은 모두 Unchecked Exception 으로, RuntimeException 을 상속받습니다. 

- **예외 클래스의 사용 방법**으로는 아래와 같은 방법들이 있습니다.
    - throws 키워드
    - try-catch 문
    - finally 와 try-with-resource 문
    - throw

# 2.  Spring에서의 예외 처리

## 2-1.  @ControllerAdvice, @ExceptionHandler 은 무엇이며 또한 이들을 활용한 예외처리 방식은 무엇인가요?

Spring 은 에러처리라는 공통 관심사를 메인 로직으로부터 분리하는 다양한 예외 처리 방식을 고안하였고, 예외 처리 전략을 추상화한 `HandlerExceptionResolver` 인터페이스를 만들었습니다.

```java
public interface HandlerExceptionResolver {
	@Nullable
	ModelAndView resolveException(
			HttpServletRequest request, // 현재 HTTP 요청 정보
			HttpServletResponse response, // HTTP 응답 정보
			@Nullable Object handler, // 예외가 발생한 Controller 객체
			Exception ex // 실제 발생한 예외 객체
		); 
}
```

컨트롤러에서 예외가 던져졌을 때, 그 **예외를 잡아서 어떤 응답을 반환할지 결정하는 객체**입니다.

스프링은 대표적으로 ControllerAdvice, ExceptionHandler 와 같은 도구들로 ExceptionResolver 를 동작시켜 에러를 처리할 수 있습니다.

Controller 실행 도중 예외가 발생한 이후, `DispatcherServlet` 이 예외를 `HandlerExceptionResolver` 구현체들에게 전달할 때, `ExceptionHandlerExceptionResolver` 가 동작하면서 실행됩니다. 즉, 요청 처리의 **후반부** 에서 예외를 전역적으로 가로채 처리하는 역할을 합니다.

- **HandlerExceptionResolver 의 구현체들**
    - **DefaultErrorAttributes**: 에러 속성을 저장하며 직접 예외를 처리하지는 않습니다.
    - **ExceptionHandlerExceptionResolver**: 에러 응답을 위한 Controller나 ControllerAdvice에 있는 ExceptionHandler를 처리합니다. ⭐️
    - **ResponseStatusExceptionResolver**: Http 상태 코드를 지정하는 @ResponseStatus 또는 ResponseStatusException를 처리합니다.
    - **DefaultHandlerExceptionResolver**: 스프링 내부의 기본 예외들을 처리합니다.

### 2-1-1.  @ExceptionHandler

@Controller, @RestController 가 적용된 Bean 내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 기능을 합니다. 다음에 어노테이션을 추가함으로써 에러를 처리할 수 있습니다.

- 컨트롤러의 메소드
- @ControllerAdvice 나 @RestControllerAdvice 가 있는 클래스의 메소드

```java
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMenuService cartMenuService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponseDto response = cartService.findCart(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<String> handleRestApiException(RestApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
```

@ExceptionHandler 라는 어노테이션을 쓰고 인자로 캐치하고 싶은 예외클래스를 등록해주면 됩니다.

### 2-1-2.  @ControllerAdvice

@ExceptionHandler 가 하나의 클래스에 대한 것이라면, @ControllerAdvice 는 모든 @Controller, 즉 전역에서 발생할 수 있는 예외를 잡아 처리해주는 어노테이션입니다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomError 발생시 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponseDto> handleRestApiException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();

        // errorcode 기반 ResponseEntity 만들어서 리턴
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.res(e));
    }

    // 유효성 에러 발생시 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidExcecption(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
    }
}
```

- DispatcherServlet 이 예외를 캐치합니다.
- HandlerExceptionResolver 가 실행되어
    
    → ExceptionHandlerExceptionResolver 가 @ControllerAdvice 안의 @ExceptionHandler 를 탐색합니다.
    
- 예외 타입이 일치하는 메서드를 찾아 실행합니다.
- ResponseEntity 로 에러 응답을 반환합니다.

⇒ 컨트롤러 하나하나에 @ExceptionHandelr 를 붙이지 않아도 모든 컨트롤러에서 발생한 예외를 한 번에 처리할 수 있게 해줍니다. 

## 2-2.  ControllerAdvice와 RestControllerAdvice의 차이가 무엇인가요?

`@RestControllerAdvice` 는 `ControllerAdvice + ResponseBody` 입니다.

@ControllerAdvice 와 동일한 역할 (예외를 잡아 핸들링 할 수 있도록 하는 기능) 을 수행하면서, @ResponseBody 를 통해 객체를 리턴할 수도 있습니다.

ViewResolver 를 통해 예외 처리 페이지로 리다이렉트 시키려면 @ControllerAdvice 만 써도 되고, 에러 응답으로 객체를 리턴해야 한다면 @Responsebody 어노테이션이 추가된 @RestControllerAdvice 를 적용하면 됩니다.

## 2-3.  ControllerAdvice 내 우선 순위와 ExceptionHandler 내 우선 순위

### 2-3-1. ControllerAdvice 내 우선순위

우선순위란, 여러 개가 동시에 처리될 수 있을 때 누가 먼저 실행되는지를 정하는 기준입니다.

예를 들어 아래와 같이 전역 예외 처리기가 여러개 있다고 가정했을 때,

```java
@RestControllerAdvice
@Order(1)
public class AHandler {
    @ExceptionHandler(Exception.class)
    public String handleA(Exception e) {
        return "A에서 처리함";
    }
}

@RestControllerAdvice
@Order(2)
public class BHandler {
    @ExceptionHandler(Exception.class)
    public String handleB(Exception e) {
        return "B에서 처리함";
    }
}
```

어떤 컨트롤러에서 예외가 터진다면 스프링은 예외를 처리할 수 있는 ControllerAdvice 가 두 개가 있음을 확인하고 우선순위 (@Order) 가 낮은 숫자부터 시도합니다.

- @Order(1) → AHandler 가 먼저 실행 시도
- 만약 AHandler 가 예외를 처리하면 → 끝
- 처리하지 못하면 → 다음 우선순위(BHandler) 가 처리를 시도합니다.

즉, `@Order` 어노테이션을 통해 ControllerAdvice 의 우선순위를 설정합니다.

### 2-3-2. ExceptionHandler 내 우선순위

하나의 @ControllerAdvice 나 @Controller 안에는 여러 개의 @ExceptionHandler 가 있을 수 있습니다. 이와 같은 경우에는 다음 순서로 우선순위가 결정됩니다.

- 가장 구체적인 예외 타입을 처리하는 메서드가 우선입니다. (상속 구조상 더 하위 클래스인 예외를 잡는 핸들러가 먼저 호출됩니다.)
    
    ```java
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNPE(NullPointerException e) { ... }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) { ... }
    ```
    
    - NullPointException 발생 시, handleNPE 메서드가 호출됩니다.
    - 다른 예외는 handleException() 이 잡게 됩니다.