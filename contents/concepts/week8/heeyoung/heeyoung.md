# [app center] 8주차 - Docker, CI/CD와 깃허브 액션

# 🍀 Docker란 무엇일까요?

## 1. 컨테이너 기술은 무엇이고 왜 필요할까요?

소프트웨어는 OS 와 라이브러리에 의존성을 띄는데, 하나의 컴퓨터에서 성격이 다른 (OS, 라이브러리 버전이 다른) 소프트웨어를 한 번에 실행할 때 어려움을 가질 수 있습니다. 컨테이너 (Container) 는 개별 Software 의 실행에 필요한 실행환경을 독립적으로 운용할 수 있도록 하는 기반환경을 의미합니다.

소프트웨어 개발에 필요한 코드, 라이브러리, 버전 등을 한 번에 묶어서 컨테이너로 만들고, 컨테이너를 통해 다른 컴퓨터(서버) 에 운반할 수 있게 합니다.

## 2. 도커란 무엇일까요?

<aside>
💡

**Docker 란 컨테이너 기반의 오픈소스 가상화 플랫폼** 입니다.

- **컨테이너 기반**
    - 다른 OS 에서도 문제 없이 개발 가능하게 합니다.
- **오픈소스**
    - 소프트웨어의 소스 코드가 공개 됩니다.
- **가상화**
    - OS 수준의 가상화 를 사용합니다. 즉, 응용 프로그램 계층을 격리합니다.
</aside>

### 2-1.  도커 이미지란 무엇이고 스프링 애플리케이션을 어떻게 이미지화할 수 있을까요?

Container 를 만들기 위해서는 dockerfile 과 image 라는 게 필요합니다. 먼저 dockfile 에서 image 를 만들고, image 를 통해 container 를 만듭니다.

### [ Dockerfile ]

dockerfile 은 image 를 생성하기 위한 용도로 작성하는 파일입니다. (컨테이너를 어떻게 만들어야 하는지) 즉, 개발자가 만든 애플리케이션을 어떤 환경에서, 어떤 설정으로, 어떻게 실행해야 하는지에 대한 정보를 담고 있는 파일입니다.

🛠️  Dockerfile 을 사용하는 방법 

### [ Docker Image ]

Image 는 컨테이너 실행에 필요한 파일과 설정값 등을 포함하고 있는 Archive File 입니다. (현재 구동되고 있는 애플리케이션의 내용을 캡쳐해서 이미지로 만들어 두는 것) → 이렇게 만들어진 이미지는 불변의 상태입니다.

### **2-1-1.  Spring Boot 프로젝트를 Docker 이미지로 만들기**

- **DockerFile 작성**
    
    프로젝트 루트에 Dockerfile 을 생성해서 아래와 같이 작성합니다.
    
    ```java
    FROM eclipse-temurin:17-jdk
    
    LABEL version=0.1
    
    ARG JAR_NAME=server-1.0-SNAPSHOT.jar
    ARG JAR_PATH=./build/libs/${JAR_NAME}
    
    RUN mkdir -p /app
    WORKDIR /app
    
    COPY ${JAR_PATH} /app/app.jar
    
    CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]
    ```
    
    이 Dockerfile 은 openJDK 17 환경이 포함된 Docker 이미지를 기반으로, 스프링 부트 애플리케이션을 컨테이너 안에서 시작하기 위한 설정 파일입니다.
    
    프로젝트를 Gradle 로 빌드하면 생성되는 `server-1.0-SNAPSHOT.jar` 을 컨테이너 내부의 `/app` 폴더로 복사하고, 컨테이너가 시작될 때 
    
    java -jar -Dspring.profiles.active=prod /app/app.jar
    
    명령어를 자동으로 실행하여 prod 프로필로 스프링 애플리케이션이 시작되도록 구성되어 있습니다.  
    

- **스프링 부트 프로젝트 빌드**
    
    ```java
    ./gradlew clean build
    ```
    
    ![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image.png)
    

- **DockerFile 이미지 빌드**
    
    ```java
    docker build -t heeyoung-server:1.0 .
    ```
    
    ![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%201.png)
    
    이미지가 로컬에 정상 생성되었습니다.
    

### 2-2.  여러 대의 컨테이너를 어떻게 동시에 띄울 수 있을까요?

현재는 Spring Boot 서버만 띄우고 있는 상태입니다. (단일 컨테이너로 서버 실행) 하지만 실제 서비스 환경에서는 데이터베이스 등 여러 서비스가 동시에 실행되어야 합니다.

멀티 컨테이너를 구성하기 위해서는 Docker-compose 를 이용할 수 있습니다.

### [ Docker-compose ]

```java
services:
  heeyoung-server:
    container_name: heeyoung-server
    networks:
      - heeyoung-network
    image: ${DOCKERHUB_USERNAME}/heeyoung-server:latest
    ports:
      - "8001:8080"
    environment:
      - TZ=Asia/Seoul
      - SPRING_DATASOURCE_URL=jdbc:mysql://heeyoung-database:3306/heeyoungdb
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=0910
    depends_on:
      - heeyoung-database

  heeyoung-database:
    container_name: heeyoung-database
    networks:
      - heeyoung-network
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=0910
      - MYSQL_DATABASE=heeyoungdb
      - TZ=Asia/Seoul
    ports:
      - "3306:3306"
```

- **networks**
    
    컨테이너들이 사용할 네트워크를 정의합니다. 서로 다른 컨테이너 간의 Docker 내부에서의 통신이 가능하게 하려면, 같은 네트워크 상에 속해 있어야 합니다.
    

- **services**
    
    실행 시킬 여러 개의 서비스를 정의합니다. 각 서비스는 Docker 컨테이너를 생성하고 실행합니다.
    
    - 해당 compose 파일에서는 Spring Boot Application 과 Database 를 사용할 것이므로 두 개의 서비스를 만들어 주었습니다.
    - Container_name 을 통해 컨테이너 이름을 정해줄 수 있으며, networks 를 통해 컨테이너 간 네트워크 사용을 연결시켜줍니다.

### [ Docker - compose 빌드 및 실행 ]

1. **docker-compose build**
    
    빌드하게 되면, docker-compose 안에서 사용된 Dockerfile 들의 빌드가 일괄적으로 자동으로 진행되며, 각각의 이미지들을 생성하게 됩니다.
    
2. **docker-compose up**
    
    아래 명령어를 통해 docker-compose 를 실행합니다.
    
    -d 는 docker-compose 를 백그라운드 실행하겠다는 옵션입니다.
    
    ![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%202.png)
    
3. **docker-compose ps**
    
    기동된 서비스(컨테이너) 를 조회합니다.
    
    ![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%203.png)
    
    - 현재 heeyoung-database 는 mysql 서버가 사용 가능하며, Docker 설정으로 인해 3307 로 열려 있는 상태입니다.
    - heeyoung-server 는 Spring Boot 서버가 정상 실행 중이며 브라우저에서 접근 가능합니다.

# 🍀 CI/CD란 무엇일까요?

## 1. CI/CD의 의미

### 1-1.  CI의 의미

CI 는 개발자를 위한 자동화 프로세스인 **지속적인 통합 (Continuous Integration)** 을 의미합니다. 

- 개발자들이 각자 만든 코드를 합치면서 자동 빌드, 자동 테스트, 자동 코드 검사가 실행되도록 하는 개발 문화 + 자동화 시스템 입니다.
- CI가 잘 구축되면 새로운 코드 변경 사항이 **정기적으로 자동 빌드 → 테스트 → 통합**되는 흐름이 만들어집니다.

### 왜 필요한가?

각자 브랜치에서 오래 작업하다가 마지막에 한꺼번에 머지하면 되면 충돌되는 코드들이 다수 발생하게 됩니다. 

따라서 가능한 한 작업을 작은 단위로 나누고, 주기적으로 자주 통합해 나가는 것이 중요합니다.

→ 이를 자동으로 지원해주는 것이 CI 입니다.

### 1-2.  CD의 두가지 의미

CD 는 **지속적인** **서비스 제공 (Continuous Delivery)** 또는 **지속적인 배포 (Continuous Deployment)** 를 의미합니다.

![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%204.png)

- **지속적인 제공 (Continuous Delivery)**
    - CI 단계에서 Build 와 Test 가 완료된 후, 배포 준비 상태가 확인되면 개발자 혹은 검증팀이 **수동으로 배포**하는 것입니다.

- **지속적인 배포 (Continuous Deployment)**
    - CI 단계에서 Build 되고 Test 가 완료된 후, 배포 준비 상태가 확인되면 **자동으로 배포까지** 진행하는 것입니다.

### 왜 의미가 두 개인가?

배포까지 전부 자동화해도 되지만, 실제 운영 환경에서는 잘못된 배포가 바로 사용자에게 영향을 주게 됩니다.

테스트로 잡을 수 없는 문제나, 배포 일정, 검증 절차 등이 존재하기 때문에 **운영 배포를 사람이 마지막에 확인하는 과정이 필요한 경우가 많습니다.**

따라서 기업에서는 운영 서버까지 **자동 배포되는 방식(Continuous Deployment)**과,

운영 배포 직전까지만 자동으로 준비해두고 **사람이 승인하는 방식(Continuous Delivery)**을 구분해 사용하게 됩니다.

## 2.  다양한 CI/CD 툴

### 2-1.  Jenkins

가장 많이 쓰이는 CD/CD 자동화 도구입니다. Git 과 같은 버전관리시스템과 연동하여 소스의 커밋을 감지하면 자동적으로 자동화 테스트가 포함된 빌드가 작동되게 설정할 수 있습니다.

### 2-2.  GoCD

파이프라인을 시각적으로 관리하는 데 특화된 CD/CD 도구 입니다. 복잡한 배포 흐름 (다중 단계, 다중 환경) 을 관리할 때 유용합니다.

- **가치 흐름 지도 (Value Stream Map, VSM)**
    - 전체 배포 경로를 단일 뷰로 시각화합니다.

![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%205.png)

# 🍀 Github Actions란 무엇일까요?

## 1.  Github Actions 소개

<aside>
💡

Github 저장소를 기반으로 Workflow 를 자동화할 수 있는 도구입니다. 깃허브에 push 하거나 PR 하면 자동으로 CI/CD 파이프라인이 실행됩니다.

</aside>

- Workflow 는 `.yml` 파일에 의해 구성되며, 테스트, 배포 등 기능에 따라 여러개의 Workflow 를 만들 수 있습니다.
- 생성된 Workflow 는 `.github/workflows` 디렉토리 밑에 위치합니다.

### 1-1.  Github Actions 구성요소

![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/image%206.png)

- **워크플로우 (workflow)**
- **이벤트 (event)**
    - 워크플로우에 특정한 이벤트를 정의하면, 해당 이벤트가 저장소에서 트리거 되었을 때 워크플로우가 실행됩니다.
    - PR 생성, 이슈 생성, 커밋-푸시 등이 이벤트에 속합니다.
- **작업 (job)**
    - 워크플로우를 구성하는 실행 단위를 의미합니다. 워크플로우 내의 모든 작업은 기본적으로 병렬로 실행됩니다.
    - 또한 같은 워크플로우 내의 작업들은 기본적으로 서로 종속성이 존재하지 않습니다.
    - 작업은 여러 개의 단계 (step) 을 포함합니다.
- **단계 (step)**
    - 작업 내의 단계들은 순차적으로 실행되며, 각 단계는 동일한 러너 (runner) 에서 실행되므로 상호 데이터를 공유할 수 있습니다.
- **액션 (action)**
    - 자주 사용되는 작업 단위를 재사용이 가능하도록 만든 실행 단위 입니다.
    - Github Marketplace 에서 공유될 수 있으며, 다른 사용자들이 자신의 워크플로우에 가져가 사용할 수 있습니다.
- **러너 (runner)**
    - Github Actions 워크플로우를 실제로 실행하는 서버를 의미합니다. Github 에서는 Ubuntu, Windows, macOS 환경의 러너를 제공합니다.
    - 기타 독립적인 환경에서 실행하고 싶을 때는 Self-Hosted Runner 를 사용할 수 있습니다.

## 2.  Workflow란?

<aside>
💡

하나 이상의 작업이 실행되는 **자동화 프로세스**입니다. 각 작업은 자체 가상 머신 또는 컨테이너 내부에서 실행됩니다.

- **push → 테스트 → 빌드 → 배포**

이러한 전체 흐름을 **workflow** 라고 부릅니다.

</aside>

### 2-1.  Workflow를 작성하기 위한 문법들

<aside>
💡

**[ workflow 파일의 전체 구조 ]**

```java
name: 워크플로 이름

on:    # 이벤트
  push:
    branches: [ "main" ]

jobs:
  job이름:
    runs-on: ubuntu-latest
    steps:
      - 이름
      - 명령 실행
```

</aside>

- workflow
    
    main 브랜치에 push 하면 실행 → 이벤트 트리거
    
    Spring Boot 빌드 
    
    Docker image build
    
    Docker Hub 로 push
    
    EC2 에 SSH 접속
    
    기존 컨테이너 중지 후 새 이미지로 다시 실행
    
    ### ✔ GitHub Actions (CI)
    
    1. 코드 checkout
    2. gradle build
    3. Docker image build
    4. DockerHub push
    
    ### ✔ GitHub Actions (CD)
    
    1. EC2에 SSH 접속
    2. EC2에서 docker-compose.yml을 최신 이미지 기준으로 다시 실행
        - 기존 컨테이너 중지
        - 최신 이미지 pull

- Workflow 를 작성하기 위해서는 `.github/workflows` 경로에 `spring-boot-build.yml` 파일을 생성해야 합니다.
    
    ![image.png](%5Bapp%20center%5D%208%EC%A3%BC%EC%B0%A8%20-%20Docker,%20CI%20CD%EC%99%80%20%EA%B9%83%ED%97%88%EB%B8%8C%20%EC%95%A1%EC%85%98/c3b1b012-2c09-4dda-83b3-1390d227efb7.png)
    

```java
// heeyoung-CICD.yml

name: heeyoung Workflow

on:
  push:
    branches: ["main"]

jobs: 
  build:
    runs-on: ubuntu-latest

  steps:
    - name: Checkout
      uses: actions/checkout@v3

    - name: Login to Dockerhub
      uses: docker/login-action@v1
      with:
        username: ${{ secrets.DOCKERHUB_USERNAME}}
        password: ${{ secrets.DOCKERHUB_TOKEN}}
      
    ...
```

- **name**
    - workflow 의 이름을 지정합니다.

- **on**
    - 이벤트를 담당하는 구문으로, 다양한 이벤트를 통해 workflow 를 활성화 시킬 수 있습니다.
    - 해당 파일에서는 main 브랜치에 push 가 발생하면 workflow 가 자동 실행 되도록 하고 있습니다. (trigger)
        - `git push origin main` 시 YAML 이 발동합니다.

- **jobs**
    - workflow 안에는 여러 개의 job 을 넣을 수 있습니다. (test, build, deploy…)
    - **build**
        - `runs-on: ubuntu-latest` : GitHub Actions 이 우분투 가상머신 하나를 켜서, 그 안에서 워크플로의 Job 을 실행시킵니다. → workflow 의 step 들이 이 VM 에서 수행됩니다.
    - **step**
        - job 내부에서 실행할 세부 명령들을 순서대로 나열하는 구문입니다.
        - checkout, JDK 설치, Docker 로그인, 이미지 빌드/푸시, 배포 등의 과정이 순서대로 실행됩니다.
        - 모든 step 은 ‘runs-on’ 에서 지정한 가상머신 내부에서 실행됩니다.
        - `- name: Checkout` : GitHub Actions 이 방금 push 된 레포지토리 코드를 받아옵니다. → 즉, VM 안에 프로젝트 파일이 복사됩니다.
        - `- name: Login to Dockerhub` : DockerHub 에 로그인합니다. → Docker image push 가 가능해집니다.