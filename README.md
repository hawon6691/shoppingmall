# ShoppingMall - E-commerce REST API

> 전자상거래 플랫폼을 위한 RESTful API 서버

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [아키텍처](#아키텍처)
- [데이터베이스 설계](#데이터베이스-설계)
- [API 명세](#api-명세)
- [설치 및 실행](#설치-및-실행)
- [개발 포인트](#개발-포인트)

---

## 프로젝트 개요

ShoppingMall은 **Spring Data JDBC**를 활용한 전자상거래 RESTful API입니다. JWT 기반 인증, 장바구니 관리, 주문 처리 등 실무 쇼핑몰의 핵심 기능을 구현했습니다.

### 개발 목표

- **Spring Data JDBC** 활용: ORM 대신 SQL 중심의 가벼운 데이터 접근
- **JWT 인증**: Stateless한 토큰 기반 인증 시스템
- **트랜잭션 관리**: 주문 생성 시 재고 차감, 장바구니 비우기 등 원자성 보장
- **RESTful 설계**: 표준 HTTP 메서드와 상태 코드 활용
- **Swagger 문서화**: 자동화된 API 문서 제공

---

## 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.5.6** - 엔터프라이즈 애플리케이션 프레임워크
- **Spring Data JDBC** - SQL 중심의 경량 데이터 접근 계층
- **Spring Security** - JWT 기반 인증 및 보안

### Database
- **MySQL 8.x** - 주 데이터베이스
- **Docker Compose** - 로컬 MySQL 컨테이너 환경

### Documentation & Tools
- **SpringDoc OpenAPI 3** - API 자동 문서화
- **Swagger UI** - 인터랙티브 API 테스트
- **Lombok** - 보일러플레이트 코드 제거
- **Gradle** - 빌드 자동화

### Security
- **JWT (JJWT 0.12.3)** - HS256 알고리즘 기반 토큰 인증
- **BCrypt** - 비밀번호 암호화

---

## 주요 기능

### 🔐 인증 및 회원 관리
- ✅ 회원가입 (username, email 중복 체크)
- ✅ 로그인 (JWT 토큰 발급)
- ✅ BCrypt 비밀번호 암호화
- ✅ JWT 기반 인증 (1시간 유효)

### 🛍️ 상품 관리
- ✅ 전체 상품 조회 (페이지네이션 지원)
- ✅ 상품 상세 조회
- ✅ 카테고리별 상품 조회
- ✅ 상품 검색 (이름 기반)
- ✅ 상품 평점 및 리뷰 수 표시

### 🛒 장바구니
- ✅ 장바구니에 상품 추가
- ✅ 장바구니 조회 (총액, 총 수량 계산)
- ✅ 장바구니 항목 삭제
- ✅ 장바구니 비우기
- ✅ 재고 검증

### 📦 주문 관리
- ✅ 주문 생성 (장바구니 → 주문)
- ✅ 주문 시 재고 차감
- ✅ 주문 내역 조회
- ✅ 주문 상세 조회
- ✅ 주문 상태 변경 (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED)
- ✅ 주문 취소 (재고 복원)
- ✅ 트랜잭션 관리 (원자성 보장)

### ⭐ 리뷰 시스템
- ✅ 상품 리뷰 작성 (구매 고객만)
- ✅ 리뷰 목록 조회 (페이지네이션)
- ✅ 상품 평균 평점 계산
- ✅ 리뷰 삭제 (작성자만)
- ✅ 중복 리뷰 방지 (1주문당 1리뷰)

---

## 아키텍처

### 계층형 아키텍처 (Layered Architecture)

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST API Endpoints
│  (AuthController, ProductController) │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Service Layer              │  ← Business Logic
│  (AuthService, CartService, etc.)   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Repository Layer             │  ← Data Access
│  (UserRepository, ProductRepository)│
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         MySQL Database              │  ← Data Storage
└─────────────────────────────────────┘
```

---

## 데이터베이스 설계

### 주요 테이블

#### Users (사용자)
- 사용자 인증 및 프로필 정보
- BCrypt 암호화된 비밀번호
- 역할 기반 권한 (USER, ADMIN)

#### Products (상품)
- 상품 정보 및 재고 관리
- 카테고리 연결
- 활성/비활성 상태 관리

#### Carts & Cart Items (장바구니)
- 사용자별 장바구니
- 상품별 수량 관리

#### Orders & Order Items (주문)
- 주문 정보 및 배송 정보
- 주문 시점 상품 가격 스냅샷
- 주문 상태 관리 (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED)
- 주문 취소 시 재고 복원

#### Reviews (리뷰)
- 상품별 구매 후기
- 평점 (1-5점)
- 중복 리뷰 방지 (사용자 + 상품 + 주문 조합)

---

## API 명세

### 🔐 인증 API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | 회원가입 |
| POST | `/api/auth/login` | 로그인 (JWT 발급) |

### 🛍️ 상품 API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products?page=1&size=10` | 전체 상품 조회 (페이지네이션) |
| GET | `/api/products/{id}` | 상품 상세 조회 |
| GET | `/api/products/category/{categoryId}` | 카테고리별 조회 |
| GET | `/api/products/search?keyword={keyword}` | 상품 검색 |

### 🛒 장바구니 API (인증 필요)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart` | 장바구니 조회 |
| POST | `/api/cart/items` | 상품 추가 |
| DELETE | `/api/cart/items/{id}` | 항목 삭제 |
| DELETE | `/api/cart` | 장바구니 비우기 |

### 📦 주문 API (인증 필요)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | 주문 생성 |
| GET | `/api/orders` | 주문 내역 조회 |
| GET | `/api/orders/{id}` | 주문 상세 조회 |
| PATCH | `/api/orders/{id}/status` | 주문 상태 변경 (관리자) |
| POST | `/api/orders/{id}/cancel` | 주문 취소 |

### ⭐ 리뷰 API

| Method | Endpoint | Description | 인증 |
|--------|----------|-------------|------|
| POST | `/api/reviews` | 리뷰 작성 | 필요 |
| GET | `/api/reviews/product/{productId}?page=1&size=10` | 상품 리뷰 목록 | - |
| GET | `/api/reviews/product/{productId}/stats` | 리뷰 통계 (평점, 개수) | - |
| GET | `/api/reviews/my-reviews` | 내 리뷰 목록 | 필요 |
| DELETE | `/api/reviews/{id}` | 리뷰 삭제 | 필요 |

**API 문서**: http://localhost:8080/swagger-ui.html

---

## 설치 및 실행

### 1. 저장소 클론

```bash
git clone <repository-url>
cd 01_shoppingmall/shoppingmall
```

### 2. Docker Compose로 MySQL 실행

```bash
docker-compose up -d
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 4. API 문서 확인

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/v3/api-docs

---

## 개발 포인트

### 1. Spring Data JDBC 활용

JPA 대신 SQL 중심의 명확한 쿼리 제어를 위해 Spring Data JDBC 사용

```java
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT * FROM products WHERE name LIKE CONCAT('%', :keyword, '%') AND is_active = true")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT * FROM products WHERE is_active = true ORDER BY id DESC LIMIT :limit OFFSET :offset")
    List<Product> findAllActiveWithPagination(@Param("limit") int limit, @Param("offset") int offset);
}
```

### 2. JWT 인증

HS256 알고리즘 기반 JWT 토큰 생성 및 검증

```java
public String generateToken(String userId) {
    return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}
```

### 3. 트랜잭션 관리

주문 생성 시 재고 차감, 주문 아이템 생성, 장바구니 비우기를 하나의 트랜잭션으로 처리

```java
@Transactional
public OrderResponse createOrder(Long userId, OrderRequest request) {
    // 1. 장바구니 조회
    Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    // 2. 재고 검증
    for (CartItem item : cartItems) {
        if (product.getStockQuantity() < item.getQuantity()) {
            throw new InsufficientStockException("재고 부족");
        }
    }

    // 3. 주문 생성 및 재고 차감
    Order order = orderRepository.save(newOrder);

    // 4. 장바구니 비우기
    cartItemRepository.deleteByCartId(cart.getId());

    return orderResponse;
}
```

### 4. 주문 상태 전환 검증

상태 머신 패턴을 통한 유효한 주문 상태 전환만 허용

```java
private void validateStatusTransition(String currentStatus, String newStatus) {
    // PENDING → CONFIRMED, CANCELLED
    if (currentStatus.equals("PENDING") &&
        !List.of("CONFIRMED", "CANCELLED").contains(newStatus)) {
        throw new IllegalStateException("Invalid status transition");
    }
    // CONFIRMED → PROCESSING, CANCELLED
    // PROCESSING → SHIPPED
    // SHIPPED → DELIVERED
}
```

### 5. 리뷰 중복 방지

사용자가 동일 주문의 동일 상품에 대해 중복 리뷰 작성 불가

```java
@Transactional
public ReviewResponse createReview(Long userId, ReviewRequest request) {
    // 중복 리뷰 체크
    reviewRepository.findByUserIdAndProductIdAndOrderId(
        userId, request.getProductId(), request.getOrderId()
    ).ifPresent(r -> {
        throw new DuplicateReviewException("이미 리뷰를 작성했습니다");
    });

    // 구매 검증 (주문이 사용자의 것인지)
    Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (!order.getUserId().equals(userId)) {
        throw new UnauthorizedException("본인 주문만 리뷰 작성 가능");
    }

    return reviewRepository.save(review);
}
```

### 6. 페이지네이션 구현

대용량 데이터 효율적 처리를 위한 LIMIT/OFFSET 기반 페이지네이션

```java
@GetMapping
public ResponseEntity<Map<String, Object>> getAllProducts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {

    List<ProductResponse> products = productService.getProductsWithPagination(page, size);
    int totalCount = productService.getTotalProductCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    Map<String, Object> response = new HashMap<>();
    response.put("products", products);
    response.put("totalPages", totalPages);

    return ResponseEntity.ok(response);
}
```

### 7. 예외 처리

Global Exception Handler로 일관된 에러 응답 제공

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
```

---

## 향후 계획

- [ ] Refresh Token 구현
- [ ] Redis 캐싱 (상품 정보, 리뷰 통계)
- [ ] 이미지 업로드 (S3 연동)
- [ ] 결제 시스템 연동
- [ ] 관리자 페이지
- [ ] 상품 재입고 알림

---

**Last Updated:** 2025-01-08
