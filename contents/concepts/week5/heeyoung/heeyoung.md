# [App center] 5주차 - Spring Security

# 🍀 Spring Security란 무엇인가요? CORS 에러가 무엇인가요?

## 1.  Spring Security란 무엇인가요?

<aside>
💡

스프링 기반의 애플리케이션에서 인증(Authentication) 과 인가(Authorization)를 처리하기 위한 보안 프레임워크입니다.

</aside>

- **인증 (Authentication)**
    - 사용자가 누구인지 확인하는 절차
    - 로그인 폼, 아이디/비밀번호 검사 등이 여기에 해당됩니다.
    - 인증이 성공하면 Authentication 객체가 만들어지고, SecurityContext 에 저장됩니다.
    - 인증이 실패하면 `401 UNAUTHORIZED` 에러를 응답받습니다.
- **인가 (Authorization)**
    - 인증된 사용자가 특정 자원에 접근할 수 있는 권한이 있는지 확인하는 과정
    - 인가가 있으려면 인증이 선행되어야 합니다.
    - 인가가 실패하면 `403 FORBIDDEN` 에러를 응답받습니다.
    - ex) 관리자는 /admin 접근 가능, 일반 사용자는 불가능

## 2.  Spring Security의 동작과정

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image.png)

1. **Http Request 수신**
    1. 사용자가 로그인 정보와 함께 인증 요청을 합니다.
2. **유저 자격을 기반으로 인증토큰 생성**
    1. AuthenticationFilter 가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken 이라는 인증용 객체를 생성합니다.
3. **Filter 를 통해 AuthenticationToken 을 AuthenticationManger 로 위임**
    1. AuthenticationManager 의 구현체인 ProviderManager 에게 생성한 UsernamePasswordToken 객체를 전달합니다.
4. **AuthenticationProvider 의 목록으로 인증을 시도**
    1. AuthenticationManager 는 등록된 AuthenticationProvider 들을 조회하며 인증을 요구합니다.
5. **UserDetailsService 의 요구**
    1. 실제 데이터베이스에서 사용자 인증정보를 가져오는 UserDetailsService 에 사용자 정보를 넘겨줍니다.
6. **UserDetails 를 이용해 User 객체에 대한 정보 탐색**
    1. 넘겨받은 사용자 정보를 통해 데이터베이스에서 찾아낸 사용자 정보인 UserDetails 객체를 만듭니다.
7. **User 객체의 정보들을 UserDetails 가 UserDetailsService(LoginService) 로 전달**
    1. AuthenticationProvider 들은 UserDetails 를 넘겨받고 사용자 정보를 비교합니다.
8. **인증 객체 or AuthenticationException**
    1. 인증이 완료 되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환합니다.
    2. 인증이 실패하면 AuthenticationException 을 날립니다.
9. **인증 끝**
    1. 다시 최초의 AuthenticationFilter 에 Authentication 객체가 반환됩니다.
10. **SecurityContext 에 인증 객체를 설정**
    1. Authentication 객체를 Security Context 에 저장합니다.

## 3.  Spring Security의 구조

### 3-1.  AuthenticationFilter

```java
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		username = (username != null) ? username.trim() : "";
		String password = obtainPassword(request);
		password = (password != null) ? password : "";
		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
				password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		// 인증 매니저에 전달
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
```

- 요청 파라미터에서 username 과 password 를 꺼내옵니다.
- 꺼내온 정보를 기반으로 UsernamePasswordAuthenticationToken 객체를 만듭니다.
- AuthenticationManager에게 위임합니다.

### 3-2.  AuthenticationManager

```java
public interface AuthenticationManager {
	Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
```

- 등록된 AuthenticationProvider 목록을 순서대로 돌면서 인증을 시도합니다.
    - **AuthenticationProvider 목록?**
        - AuthenticationProvider 란 인증 방식을 구현해 놓은 클래스 입니다. SpringSecurity 에서 기본으로 제공하는 프로바이더 (DAO, Ldap) 이외에 Jwt토큰을 통한 인증, 혹은 다른 인증 방식을 사용하길 원한다면 AuthenticationProvider 를 직접 구현하여 사용해야 합니다.
        - ProviderManager 안에 providers 리스트가 “등록된 AuthenticationProvider 목록” 입니다.
        `private List<AuthenticationProvider> providers = Collections.*emptyList*();`

### 3-3.  AuthenticationProvider

```java
public interface AuthenticationProvider {

	Authentication authenticate(Authentication authentication) throws AuthenticationException;

	boolean supports(Class<?> authentication);

}
```

- 실제 인증을 담당하는 인터페이스입니다.
- **Authenticate 메서드**
    - 인증 전에 Authentication 객체를 받아 인증이 완료된 객체를 반환하는 역할을 합니다.
    - 이 메서드 내에서 실제 인증 로직을 수행합니다.
- **supports 메서드**
    - Provider 가 처리할 수 있는 인증 타입인지 체크합니다.
        - ex. jwt 인증방식을 사용할경우,,,
            - 위 코드가 `true`를 반환하면 → 이 Provider는 **JWT 토큰 인증 처리 가능**
            - `false`를 반환하면 → Provider는 패스하고, 다른 Provider로 넘어감
    - 이 메소드를 통해 해당 AuthenticationProvider가 다룰 수 있는 타입의 Authentication을 명시한다.

### 3-4.  UserDetailsService

```java
public interface UserDetailsService {
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

- username 을 통해 DB 에서 유저 정보를 가져옵니다.

### 3-5.  UserDetails

```java
public interface UserDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities(); // 계정 권한 목록 리턴

    String getPassword(); // 계정 비밀번호 리턴
 
    String getUsername(); // 계정 고유한 값 리턴

    boolean isAccountNonExpired(); // 계정 만료 여부 리턴

    boolean isAccountNonLocked(); // 계정 잠김 여부 리턴

    boolean isCredentialsNonExpired(); // 비밀번호 만료 여부 리턴

    boolean isEnabled(); // 계정 활성화 여부 리턴
}
```

- 사용자의 정보를 담는 인터페이스입니다.
- DB 엔티티에 구현할 수도 있지만, 별도의 UserDetailsImpl 같은 클래스를 만들어 DB 엔티티를 감싸서 사용합니다.

### 3-6. 주요 모듈

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%201.png)

### [ Authentication ]

인증에 성공하면 사용자의 principal 과 credential 정보를 Authentication 객체 안에 담습니다.

### [ SecurityContext ]

Authentication 을 보관하는 역할을 하며, SecurityContext 를 통해 Authentication 객체를 꺼내올 수 있습니다.

### [ SecurityContextHolder ]

보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 컨텍스트에 대한 세부 정보가 저장됩니다.

- **cf.  왜 SecurityContext에 저장하는가?**
    
    인증을 성공하고 나서, 이후 요청 처리에서도 인증 정보를 계속 사용하고 있어야 하기 때문입니다.
    
    예를 들어 게시글 작성, 댓글 작성 등 매번 DB 에서 사용자 정보를 조회할 필요 없이, SecurityContext 에서 바로 가져올 수 있습니다.
    
    로그아웃을 하면 SecurityContext 에 있던 Authentication 객체는 삭제되고 SecurityContextLogoutHandler 가 호출되어 SecurityContext 를 비우고 세션도 무효화합니다.
    

---

# 🍀 Jwt란 무엇이며, 어떤 역할을 하나요?

## 1.  Jwt란 무엇인가요?

<aside>
💡

JWT(Json Web Token) 란 Json 포맷을 이용해 사용자에 대한 속성을 저장하는 Claim 기반 Web Token 입니다. 

- Claim: Json 문자열 안에 들어있는 각각의 정보 항목 (key-value)
- Web Token: 웹 환경에서 통용되는 인증 증명서

⇒  JSON 형식으로 사용자 정보를 담은 인증 토큰

</aside>

### 1-1.  쿠키, 세션, 토큰 각각의 인증 방식은 무엇이고 각각 어떤 차이가 있을까요?

HTTP 통신은 요청 → 응답이 종료되면 stateless (무상태성) 의 특징을 갖습니다. 따라서 로그인과 같은 일을 할 때, ‘누가’ 로그인 중인지 **상태** 를 기억하기 위해 쿠키, 세션, 토큰을 사용합니다.

### 🍪  Cookie 인증

쿠키는 공개 가능한 정보를 사용자의 브라우저에 저장시킵니다. (서버와 클라이언트의 매개체)

- 사용자가 서버에 요청을 보냅니다.

```java
GET /login
```

- 서버가 응답할 때 이런 헤더를 같이 보냅니다.

```java
Set-Cookie: user=heeyoung; Max-Age=3600
```

⇒ 브라우저는 이걸 보고 `user=heeyoung` 이라는 쿠키를 저장합니다.

- 이후 해당 웹사이트에 방문할 때마다 브라우저는 해당 쿠키를 포함한 요청을 서버에 전송하게 됩니다. 서버는 이 쿠키로 사용자가 누군지 알아낼 수 있습니다.

### 🔐  Session 인증

세션은 클라이언트의 민감한 인증 정보를 브라우저가 아닌 **서버** 측에 저장하고 관리합니다. (서버 메모리나 로컬 파일, 데이터베이스에 저장함)

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%202.png)

1. 유저가 웹사이트에서 로그인하면 세션이 서버 메모리 상에 저장됩니다. 이때 세션을 식별하기 위한 Session Id를 기준으로 정보를 저장합니다.
2. 서버는 이 세션ID 를 클라이언트에게 쿠키로 보냅니다. (쿠키는 인증이 아닌, 그냥 세션ID 를 클라이언트에게 전달하기 위한 수단)
3. 브라우저는 쿠키를 저장합니다.
4. 사용자가 다음 요청을 보낼 때마다 자동으로 쿠키를 함께 전송합니다.
5. 서버는 클라이언트가 보낸 세션ID 와 서버 메모리로 관리중인 세션ID 를 비교하여 인증을 수행합니다.

### 💰  Token 인증

토큰 기반 인증 시스템은 클라이언트가 서버에 접속을 하면 서버에서 해당 클라이언트에게 인증되었다는 의미로 **토큰** 을 부여합니다. 이 토큰은 유일하며, 토큰을 받은 클라이언트는 다시 요청을 보낼 때 요청 헤더에 토큰을 함께 보냅니다. 서버는 클라이언트로부터 받은 토큰을 검증하여, 해당 토큰이 서버에서 발급한 것인지 확인하고 인증을 처리합니다.

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%203.png)

1. 유저가 로그인을 하면, 서버 측에서 클라이언트에게 토큰을 발급합니다.
2. 클라이언트는 서버에게 받은 토큰을 쿠키에 저장해두고, 서버가 요청 할 때마다 해당 토큰을 HTTP 요청 헤더에 포함시켜 전달합니다.
3. 서버는 전달받은 토큰을 검증 (유효성 확인) 하고, 요청에 응답합니다.
    
    토큰에는 요청한 사람의 정보가 담겨있기 때문에 서버는 DB 를 조회하지 않고 누가 요청하는지 알 수 있습니다.
    

### [ 서버 기반 vs 토큰 기반 ]

- **서버(세션) 기반 인증 시스템**
    - 서버의 세션을 사용해 사용자 인증을 하는 방법은, 클라이언트로부터 요청을 받으면 클라이언트의 상태를 계속 유지하고 사용합니다. (Stateful) 이는 사용자가 증가함에 따라 서버 메모리 부담이 증가한다는 단점이 있습니다.
- **토큰 기반 인증 시스템**
    - 인증받은 사용자에게 토큰을 발급하고, 인증이 필요한 작업일 경우 헤더에 토큰을 함께 보내 인증받은 사용자인지 확인합니다.
    - 서버는 상태를 기억하지 않고 토큰으로 인증만 처리하기 때문에 (Stateless), 서버 부하가 적습니다.

### 1-2.  Jwt는 어떤 구조로 이루어져 있나요?

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%204.png)

- **Header**
    - 서명 생성에 사용된 해시 알고리즘과 토큰 타입을 명시합니다.
- **Payload**
    - 토큰에 담을 클레임 정보를 포함합니다. (sub, name, iat 이 각각 하나의 클레임)
    - 클레임의 정보는 세 종류가 있습니다.
        - **등록된 클레임 (registered claim)** : 토큰 정보를 표현하기 위해 이미 정해진 종류의 데이터들입니다. (sub 가 여기에 해당합니다. (토큰제목))
        - **공개 클레임 (public claim)** : 사용자 정의 클레임으로, 공개용 정보를 위해 사용됩니다. 충돌 방지를 위해 URI 포맷을 이용합니다. ⇒ 여러 서비스가 토큰을 공유해야 하는 경우 사용합니다.
        `"https://mangkyu.tistory.com": true`
        - **비공개 클레임 (private claim)** : 사용자 정의 클레임으로, 서버와 클라이언트 사이에 임의로 지정한 정보를 저장합니다.
- **Signature**
    - 점(.) 을 구분자로 해서 헤더와 페이로드를 합친 문자열을 서명한 값입니다.

생성된 토큰은 HTTP 통신을 할 때 Authorization 이라는 key 의 value 로 사용됩니다. 일반적으로 value 에는 Bearer 이 앞에 붙여집니다.

```jsx
 "Authorization": "Bearer {생성된 토큰 값}"
```

### 1-3.  Jwt의 장점과 단점은 각각 무엇일까요?

- **장점**
    - header 와 payload 를 가지고 signature 를 생성하므로 데이터 위변조를 막을 수 있습니다.
    - 인증 정보에 대한 별도의 저장소가 필요없습니다. (서버 부하 낮음)
    - 토큰은 한 번 발급되면 유효기간이 만료될 때까지 계속 사용할 수 있습니다.

- **단점**
    - 쿠키나 세션과 다르게 JWT는 토큰의 길이가 길어 인증 요청이 많아질수록 네트워크 부하가 심해진다.
    - Payload 인코딩: 페이로드 자체는 암호화 된 것이 아니라, 중간에 Payload 를 탈취하여 디코딩하면 데이터를 볼 수 있으므로 중요 데이터를 넣지 않아야 합니다.

## 2.  Spring에서 Jwt를 어떻게 활용할 수 있을까요?

### 2-1.  AccessToken과 RefreshToken은 각각 무엇일까요?

### [ AccessToken ]

**JWT 형태로 발급된 실제 인증용 토큰**입니다. 

- JWT 인증 방식은 서버에서 상태를 관리하지 않습니다. 즉, access token 으로 JWT 를 사용하여 사용자 검증을 진행하면 서버에서 토큰의 상태를 제어할 수 없습니다.
- access token 만 사용한다면, 이용 중에 토큰이 만료된다면 다음 서비스를 이용하려다가 갑자기 로그아웃되거나 오류가 발생할 수 있습니다. **→ 유효시간이 짧다면 편의상 문제 발생**
- 또한 외부 공격자가 토큰을 탈취한다면 토큰이 만료될 때까지 속수무책이라는 문제도 있습니다. → **유효시간이 길다면 보안상의 문제 발생**

### [ RefreshToken ]

**access token 이 만료되었을 때 서버에서 이를 확인하고 새로운 access token 을 발급받기 위한 토큰**입니다.

- access token 의 유효시간을 짧게 하는 대신, 유효시간이 긴 refresh token 을 함께 발급하여 access token 자체를 계속 갱신하는 것입니다.
- refresh token 에는 사용자 정보가 거의 없기 때문에 (서버가 발급한 토큰이 맞는지만 검증) 탈취당했을 때도 상대적으로 안전하다고 볼 수 있습니다.

### 2-2.  각 토큰은 클라이언트와 서버에서 어떤 방식으로 관리되어야 할까요?

**Access Token** 은 서버에 저장하지 않습니다.

**Refresh Token** 은 서버가 저장하거나 검증 가능한 형태로 관리해야 합니다.

### DB 를 활용한 Refresh Token 관리

<aside>
💡

1. DB 의 사용자 테이블에 Refresh Token 컬럼을 추가합니다.
2. 로그인, 회원가입 시 Refresh Token 을 DB 에 저장합니다.
3. 토큰을 재발급 하는 api 를 구현합니다.
</aside>

# 🍀 CORS란 무엇인가요?

<aside>
💡

### Cross-origin-resource-sharing (교차 출처 리소스 공유)

출처(origin) 이 다른 자원들을 공유한다는 뜻으로, 한 출처에 있는 자원에서 다른 출처에 있는 자원에 접근하도록 하는 개념입니다.

</aside>

## 1.  Origin이 무엇인가요?

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%205.png)

출처(Origin) 이란 웹사이트의 주요 구성요소입니다. **Protocol + Host + Port** 3가지가 같으면 동일 출처(same-origin) 라고 합니다. 이 셋 중 하나라도 다르다면 교차 출처 (cross-origin) 라고 합니다.

## 2.  SOP 정책이란 무엇인가요?

보통 브라우저는 보안상 출처가 다르면 요청을 막아버립니다. 이를 SOP(Same-Origin Policy) 정책, 동일 출처 정책이라고 합니다. 

예를 들어, [http://www.heeyoung.com](http://www.heeyoung.com) URL 로 서버에 요청을 보내면,

서버는  [http://www.heeyoung.com](http://www.heeyoung.com) URL 로만 응답을 보낼 수 있습니다.

- 이는 보안상의 장점이 있지만, 유효한 요청 또한 막아버린다는 단점이 있습니다. 출처가 서로 다른 곳에서 리소스를 가져와서 사용하는 행위를 모두 막아버리면 웹 애플리케이션이 원활하게 동작하기 힘들게 될 것입니다.
- 따라서 출처가 다른 리소스를 사용할 수 있도록 하는 몇 가지 예외 조항이 있는데, 그중 하나가 바로 **CORS 정책을 지킨 리소스 요청**입니다. 즉, CORS 정책을 지킨다면 SOP에서 기본적으로 제한하고 있는 행위들의 적용을 받지 않게 된다는 뜻입니다.

## 3.  Spring에서 CORS를 어떤 방식으로 관리해야 할까요?

### [ 브라우저의 CORS 기본 동작 ]

- Origin: `https://www.google.com`
- **서버:** Host가 `server.example.com`
1. **클라이언트에서 출처가 다른 곳에 요청할 때, HTTP 요청 헤더에 `Origin` 을 담아 전달합니다.**
    1. 만약 클라이언트와 서버의 출처가 다르면 교차 출처 요청이 됩니다.
    
    ```jsx
    Origin: https://www.google.com
    ```
    
2. **서버는 응답헤더에 `Acess-Control-Allow-Origin` 을 담아 클라이언트에게 전달합니다.**
    1. 이후 서버가 이 요청에 대한 응답을 할 때 응답 헤더에 Acess-Control-Allow-Origin 라는 필드를 추가하고, 값으로 ‘이 리소스를 접근하는 것이 허용된 출처 url’ 을 함께 보냅니다.
    
    ```jsx
    Access-Control-Allow-Origin: https://www.google.com
    ```
    
3. **응답을 받은 브라우저는 자신이 보냈던 요청 `Origin` 과 서버 응답의 `Access-Control-Allow-Origin` 값을 비교합니다.**
    1. 일치하면 출처가 다른 요청일지라도 유효한 요청으로 보고, 받아온 리소스를 사용할 수 있게 합니다.
    2. 일치하지 않으면 브라우저가 접근을 차단합니다 → CORS 에러!

### 3-1.  단순 요청 (Simple Request)

요청과 응답을 한 번 주고받는 것을 의미합니다. 한 번 일어나는 만큼 안전성을 보장할 수 있도록 조건이 까다롭습니다.

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%206.png)

1. 요청 메서드는 GET, HEAD, POST 중 하나여야 합니다.
2. Content-Type 헤더에는 아래와 같은 값들만 설정할 수 있습니다.
    - application/x-www-form-urlencode
    - mulipart/form-data
    - text/plain
3. User Agent 가 자동으로 설정한 헤더를 제외하면, 아래와 같은 헤더들만 사용할 수 있습니다.
    - Accept
    - Accept-Language
    - Content-Language
    - Content-Type
    - DPR
    - Downlink(en-US)
    - Viewport-Width
    - Width

⇒ 다른 Origin 으로 요청을 보낼 때 헤더에 자신의 Origin 을 설정하고, 서버로부터 응답을 받으면 응답의 Allow-Control-Allow-Origin 헤더에 설정된 목록에서 요청된 Origin 헤더값이 포함되는지 검사하는 것입니다.

⇒ 대부분의 HTTP 요청은 text/xml 이나 application/json 으로 통신하기 때문에 잘 사용하지 않습니다.

### 3-2.  예비 요청 (Preflight Request)

단순 요청의 조건에 충족하지 못한 경우, 서버에 실제 요청을 보내기 전에 예비 요청을 먼저 보내 실제 요청이 안전한지 확인하는 것입니다. 안전한 요청임이 확인되면, 실제 요청을 서버에게 보냅니다 ⇒ 총 2번의 요청 전송

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%207.png)

1. 브라우저가 서버로 HTTP OPTIONS 메서드를 통해 예비 요청을 먼저 보냅니다.
    1. Origin 헤더에 자신의 Origin 을 넣습니다.
    2. Access-Control-Request-Method 헤더에 실제 요청에 사용할 메서드를 설정합니다.
    3. Access-Control-Request-Headers 헤더에 실제 요청에 사용할 헤더들을 설정합니다.
2. 서버는 예비 요청에 대한 응답으로 어떤 것을 허용하고 어떤 것을 금지할지에 대한 헤더 정보를 담아 반환합니다.
    1. Access-Control-Request-Origin 헤더에 사용되는 Origin 목록을 설정합니다.
    2. Access-Control-Request-Methods 헤더에 허용되는 메서드 목록을 설정합니다.
    3. Access-Control-Request-Headers 헤더에 허용되는 헤더 목록을 설정합니다.
    4. Access-Control-Request-Age 헤더에 해당 예비 요청이 브라우저에 캐시 될 수 있는 시간을 설정합니다.
3. 이후 브라우저는 보낸 요청과 서버가 응답해준 정책을 비교해, 해당 요청이 안전한지 확인하고 본 요청을 보냅니다.
4. 서버가 본 요청에 대한 응답을 합니다.

### 3-3.  인증된 요청 (Credentialed Request)

인증된 요청은 클라이언트에서 서버에게 **자격 인증 정보(Credential)** 을 실어 요청할 때 사용되는 요청입니다. ⇒ 세션 ID 가 저장돼있는 쿠키 혹은 Authorization 헤더에 설정하는 토큰 값 등을 의미합니다.

![image.png](%5BApp%20center%5D%205%EC%A3%BC%EC%B0%A8%20-%20Spring%20Security/image%208.png)