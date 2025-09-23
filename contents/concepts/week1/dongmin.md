### 🍀 웹/앱 서비스에서 클라이언트와 서버는 어떻게 데이터를 주고 받을까요?

#### HTTP란 무엇이고 어떻게 동작할까요?
- HTTP(Hypertext Transfer Protocol)
- 하이퍼텍스트 링크를 사용하여 웹에서 데이터를 주고받는 서버-클라이언트 모델의 프로토콜
- 클라이언트 요청(HTTP 요청 메서드) -> 서버 응답(데이터는 body에 담아서)

---

#### REST API란?

> **REST API**는 *Representational State Transfer(표현 상태 전송)* 아키텍처 스타일의 설계 원칙을 따르는 **애플리케이션 프로그래밍 인터페이스(API)**를 의미한다. </br>
> **RESTful API** 또는 **RESTful Web API** 

---

#### REST의 주요 원칙
1. **균일한 인터페이스 (Uniform Interface)**  
   - API 설계가 일관성 있게 이루어져야 하며, 리소스는 고유한 URL로 식별된다.  
   - 예: `/users/1` -> ID가 1인 사용자를 조회  

2. **무상태성 (Stateless)**  
   - 각 요청은 독립적으로 처리되며, 서버는 클라이언트의 상태를 보존하지 않는다. 
   - 요청마다 필요한 모든 정보를 포함해야 한다. 

3. **계층화 시스템 (Layered System)**  
   - 클라이언트는 직접 연결된 서버 외에 중간 서버(프록시, 로드 밸런서 등)가 개입하는지 알 필요가 없다.  
   - 이를 통해 보안, 캐싱, 확장성을 강화

---

#### REST API의 장점
1. **확장성 (Scalability)**  
   - 서버를 쉽게 분산 및 확장 가능

2. **유연성 (Flexibility)**  
   - 다양한 데이터 형식(JSON, XML 등)을 지원하고, 클라이언트와 서버가 독립적으로 발전 가능

3. **독립성 (Independence)**  
   - 서버와 클라이언트가 명확히 분리되어 있어, 서로 다른 플랫폼·환경에서도 원활히 통신 가능

---


#### 데이터 교환 형식
1. XML
    - 태그 기반 데이터 표현 (사용자가 직접 태그 정의 가능)  
    - 문서 구조 표현에 유리 (계층 구조를 잘 나타냄)  
    - 다소 무겁고, 파싱 속도가 느린 단점

2. JSON
    - Key-Value 쌍 기반의 데이터 표현  
    - 가독성이 뛰어나고 직관적  
    - 파싱과 생성이 간단 -> 대부분의 API에서 채택  

---

### 직렬화와 역직렬화

| 구분       | 설명                                                                 |
|------------|----------------------------------------------------------------------|
| **직렬화**   | 객체(Object)를 지정된 포맷(String)으로 변환 -> 주로 서버가 클라이언트에 데이터를 전달할 때 사용 |
| **역직렬화** | 지정된 포맷(String)을 다시 객체(Object)로 변환 -> 클라이언트가 보낸 데이터를 객체화하여 DB 저장 또는 로직 수행 |

---

### 🍀 Spring은 무엇이고 어떻게 구성되어 있을까요?
> 자바 기반의 애플리케이션을 개발하기 위한 오픈소스 프레임워크 </br>
> 방대한 양의 기술 API를 제공하여 엔터프라이즈 애플리케이션 개발에 활용

#### Spring의 주요 구성 요소
1. **Data Access/Integration**
    - Spring JDBC, ORM, Transactions 지원
    - 데이터 접근을 단순화 및 균일하게 관리
2. **Web**
    - 웹 애플리케이션 개발 지원
    - Rest API, Web Sockets 연계
3. **Core Container**
    - Spring 핵심 모둘 (BeanFactory, ApplicationContext ...)
    - 객체 생성 및 의존성 관리
4. **Test**
    - 테스트 기능 지원

---

#### Spring과 SpringBoot의 차이점
| 구분        | Spring | Spring Boot |
|-------------|------------------|-------------|
| **설정** | XML, Java Config 기반 수동 설정 필요 | 자동 설정(Auto Configuration) 지원 |
| **목표**     | 유연하고 세밀한 프레임워크 제공 | 빠른 개발 환경 제공 (Production-ready) |
| **장점**     | 모듈 단위로 세밀한 제어 가능 | 내장 톰캣/제티/언더토우 지원, 별도 WAS 필요 없음 |
| **난이도** | 상대적으로 가파름 | 진입 장벽이 낮음 |

---

#### Spring Framework의 주요 특징
1. **POJO (Plain Old Java Object)**  
   - 특별한 규약 없이 순수한 자바 객체로 개발 가능  

2. **IoC (Inversion of Control, 제어의 역행)**  
   - 객체의 생성 및 생명 주기를 개발자가 아닌 컨테이너가 관리  

3. **DI (Dependency Injection, 의존성 주입)**  
   - 객체 간 의존 관계를 외부에서 주입 → 결합도를 낮추고 유지보수성 향상  

4. **AOP (Aspect-Oriented Programming, 관점 지향 프로그래밍)**  
   - 로깅, 보안, 트랜잭션 같은 공통 기능을 모듈화  

5. **PSA (Portable Service Abstraction, 추상화를 통한 간결함)**  
   - 트랜잭션, 메시징, 데이터 접근 등의 기술을 추상화하여 일관된 사용법 제공

---

### 🍀 Servlet Container와 Spring Container는 무엇인가요? 그리고 어떻게 동작하나요?

#### MVC 패턴이 무엇인가요? 또 이 패턴은 어떻게 동작하나요?
- Spring MVC 패턴이란?
    > 애플리케이션을 **Model, View, Controller** 세 영역으로 분리하여 각 역할에 맞게 코드를 작성하는 아키텍처 패턴
    - Model
        - 클라이언트의 요청을 처리하기 위한 **비즈니스 로직과 데이터**를 담당
    - View
        - 클라이언트에게 보여질 **화면 출력**을 담당
    - Controller
        - 클라이언트의 요청을 직접적으로 전달받는 **엔트포인트** 
        - Model과 View 중간에서 상호 작용을 해주는 역할
- Spring MVC 패턴의 동작 흐름
    1. 클라이언트가 요청을 보냄 (`/users/1`)  
    2. 요청이 **DispatcherServlet**으로 전달됨 (Front Controller)  
    3. DispatcherServlet은 **Handler Mapping**을 통해 요청을 처리할 Controller를 탐색  
    4. 해당 **Controller**가 비즈니스 로직(Service, DAO)을 호출 -> Model 생성  
    5. 처리된 결과(Model)를 반환하고, View Resolver를 통해 View 결정  
    6. **View**가 렌더링되어 클라이언트에게 응답 반환

---

#### Servlet Container는 무엇인가요?
- Servlet이란?
    > 동적 웹 페이지를 만들 때 사용되는 자바 기반의 웹 애플리케이션 프로그래밍 기술 </br>
    > 문자열 파싱 등 신경쓰지 않고 비즈니스 로직에 더욱 집중할 수 있다.

- Servlet Container의 사용자 요청 처리 방식
    1. 사용자가 URL을 클릭 -> **HTTP Request**를 **Servlet Container**에 보낸다.
    2. **Servlet Container**는 **HttpServletRequest, HttpServletResponse** 두 객체를 생성한다.
    3. 사용자가 요청한 URL을 분석하여 어느 서블릿에 대한 요청인지 찾는다.
    4. Container는 서블릿 **service() 메소드**를 호출 -> POST/GET 여부에 따라 doGet() 또는 doPost()가 호출된다.
    5. 동적인 페이지를 생성한 후 **HttpServletResponse** 객체에 응답을 보낸다.
    6. 응답이 완료되면 HttpServletRequest, HttpServletResponse 두 객체를 소멸시킨다.

---

#### Spring Container는 무엇인가요?
- 프론트 컨트롤러 패턴이란? DispatcherServlet이란?
    > 클라이언트가 보내는 모든 HTTP 요청을 받아서 적합한 Controller에 위임
    > Front Controller와 DispatcherServlet은 같다.
- Spring Container의 Bean 관리 방식
    > BeanFactory와 이를 상속한 ApplicationContext가 존재 -> 의존성 주입된 빈들을 제어하고 관리
    - **BeanFactory**  
        - Spring Container의 최상위 인터페이스  
        - Bean 등록, 생성, 조회, 의존성 관리 담당  
        - `getBean()` 메서드를 사용해 Bean을 인스턴스화 가능

    - **ApplicationContext**  
        - BeanFactory를 확장한 컨테이너  
        - 국제화(i18n), 이벤트 발행, AOP, 트랜잭션 관리 등 **추가 기능 제공**