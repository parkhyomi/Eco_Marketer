# Eco_Marketer Refactoring

## 프로젝트 개요

**Eco-Marketer**는 생성형 AI를 활용하여 중고 거래 물품의 판매글을 자동으로 생성해주는 Android 애플리케이션입니다.

본 프로젝트는 기존 코드베이스의 유지보수성, 확장성, 테스트 가능성을 향상시키기 위한 **전면 리팩토링**을 목표로 합니다.

---

##  리팩토링 목표

### 1. 코드 구조 개선 및 책임 분리
- **모듈성 강화**
  - 클래스와 함수 단위의 명확한 책임 분리
  - 높은 응집도와 낮은 결합도 지향
  - 재사용 가능한 컴포넌트 설계

- **코드 품질 개선**
  - 가독성과 유지보수성 향상
  - 중복 코드 제거 (DRY 원칙)
  - 명확한 네이밍 컨벤션 적용

### 2. MVVM 아키텍처 패턴 적용
- **MVVM (Model-View-ViewModel)** 패턴 도입
  - UI 로직과 비즈니스 로직 분리
  - ViewModel을 통한 상태 관리 및 데이터 바인딩
  - LiveData/StateFlow를 활용한 반응형 UI 구현
  - 비동기 작업 관리를 위한 Coroutines 활용
  
- **계층별 역할**
  - **View (UI Layer)**: Composable - 사용자 인터페이스 렌더링
  - **ViewModel**: UI 상태 관리 및 비즈니스 로직 처리
  - **Data Layer**: Repository, DataSource - 데이터 접근 및 관리

- **의존성 주입 (Dependency Injection)**
  - **Hilt** 도입으로 의존성 관리 자동화
  - 테스트 용이성 향상
  - 객체 생명주기 관리 개선

### 3. 디자인 패턴 적용
다음의 디자인 패턴을 선별적으로 도입하여 설계의 일관성과 확장성을 확보합니다:

- **생성 패턴 (Creational Patterns)**
- **구조 패턴 (Structural Patterns)**
- **행위 패턴 (Behavioral Patterns)**

---

## 학습 목표

- Clean Architecture 및 MVVM 패턴 실전 적용
- Hilt를 활용한 의존성 주입 패턴 학습
- 실무 수준의 코드 구조 및 설계 경험
- 디자인 패턴의 실제 활용 사례 이해
- 리팩토링 프로세스 및 Best Practice 습득

---

## 기술 스택

- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Async**: Coroutines, Flow
- **UI**: Jetpack Compose
- **Networking**: Retrofit, OkHttp
- **Build Tool**: Gradle
- **Version Control**: Git