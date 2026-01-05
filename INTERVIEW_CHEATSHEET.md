# 🎯 INTERVIEW QUICK REFERENCE CHEAT SHEET

---

## 📌 ONE-LINER INTRO
> "A Spring Boot REST API for stock trading - instruments lookup, order management, trade tracking, and portfolio with P&L calculation."

---

## 🛠️ TECH STACK (Memorize This)

| Technology | Version | Why |
|------------|---------|-----|
| Java | 17 | LTS, required for Spring Boot 3 |
| Spring Boot | 3.2.0 | Auto-config, embedded Tomcat |
| Maven | 3.6+ | Dependency management |
| Lombok | Latest | Reduce boilerplate |
| Springdoc OpenAPI | 2.3.0 | Swagger UI (modern replacement) |
| ConcurrentHashMap | Built-in | Thread-safe in-memory storage |

---

## 🌐 API ENDPOINTS

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/instruments` | List all stocks |
| GET | `/api/v1/instruments?exchange=NSE` | Filter by exchange |
| POST | `/api/v1/orders` | Place order |
| GET | `/api/v1/orders` | List all orders |
| GET | `/api/v1/orders/{id}` | Get specific order |
| DELETE | `/api/v1/orders/{id}` | Cancel order |
| GET | `/api/v1/trades` | List trades |
| GET | `/api/v1/portfolio` | Get holdings + P&L |
| GET | `/api/v1/portfolio/summary` | Total P&L summary |

---

## 📁 KEY FILES (Quick Open)

```
controller/OrderController.java    → REST endpoints
service/OrderService.java          → Business logic (ORDER FLOW)
repository/OrderRepository.java    → ConcurrentHashMap storage
dto/OrderRequest.java              → Validation annotations
exception/GlobalExceptionHandler   → Centralized error handling
config/DataInitializer.java        → Sample data on startup
```

---

## 🔄 ORDER FLOW (Draw if Asked)

```
POST /orders → Controller → Service → Repository
                   ↓
              Validate Order
                   ↓
         ┌────────┴────────┐
         ↓                 ↓
    MARKET Order      LIMIT Order
         ↓                 ↓
    Execute Now       Stay PLACED
         ↓
   Create Trade
         ↓
   Update Portfolio
```

---

## ❓ QUICK Q&A

### "Why no database?"
> "Mock SDK - in-memory is faster to develop. ConcurrentHashMap is thread-safe. For production, I'd use PostgreSQL with JPA - only Repository layer changes."

### "Why ConcurrentHashMap not HashMap?"
> "Thread-safety. Multiple users placing orders simultaneously won't corrupt data."

### "Why Springdoc not Swagger 2?"
> "Swagger 2 (springfox) is discontinued. Springdoc is the modern replacement for Spring Boot 3."

### "Why Lombok?"
> "Reduces 50+ lines of getters/setters/builders to just annotations. Cleaner code."

### "Why separate DTO from Entity?"
> "Security - control what goes in/out. Entities might have internal fields we shouldn't expose."

### "Why @RestControllerAdvice?"
> "Centralized error handling. No try-catch in every controller method. DRY principle."

### "How to add authentication?"
> "Spring Security + JWT. Replace MOCK_USER_ID with user from SecurityContext."

### "How to add database?"
> "1. Add spring-data-jpa dependency, 2. Add @Entity to models, 3. Change Repository to extend JpaRepository"

### "Is it thread-safe?"
> "Yes. ConcurrentHashMap handles concurrent operations safely."

### "What if SELL > holdings?"
> "Throws InsufficientHoldingsException → 400 Bad Request"

---

## 💻 COMMANDS TO RUN

```powershell
# Start app
cd d:\Placement\Bajaj\trading-sdk
mvn org.springframework.boot:spring-boot-maven-plugin:run

# Run tests
mvn test

# Build JAR
mvn clean package
```

**URLs when running:**
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

---

## 📊 SAMPLE REQUEST/RESPONSE

### Place Order Request:
```json
{
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "orderType": "BUY",
  "orderStyle": "MARKET",
  "quantity": 10
}
```

### Success Response:
```json
{
  "success": true,
  "data": { "orderId": "ORD-ABC123", "status": "EXECUTED" },
  "message": "Order placed successfully"
}
```

### Error Response:
```json
{
  "success": false,
  "data": null,
  "message": "Insufficient holdings for SELL order"
}
```

---

## 🧮 P&L FORMULA

```
Current Value = Quantity × Current Price
Investment = Quantity × Average Buy Price
P&L = Current Value - Investment
P&L% = (P&L / Investment) × 100
```

---

## 🏗️ ARCHITECTURE LAYERS

```
┌─────────────────────────────────┐
│  Controller (HTTP handling)    │  @RestController
├─────────────────────────────────┤
│  Service (Business logic)      │  @Service
├─────────────────────────────────┤
│  Repository (Data access)      │  @Repository
├─────────────────────────────────┤
│  ConcurrentHashMap (Storage)   │  In-memory
└─────────────────────────────────┘
```

---

## 🔑 KEY ANNOTATIONS TO KNOW

| Annotation | Purpose |
|------------|---------|
| `@RestController` | REST endpoints, returns JSON |
| `@Service` | Business logic layer |
| `@Repository` | Data access layer |
| `@Valid` | Trigger validation |
| `@NotNull`, `@NotBlank`, `@Min` | Validation rules |
| `@RestControllerAdvice` | Global exception handler |
| `@ExceptionHandler` | Handle specific exception |
| `@Data`, `@Builder` | Lombok - generate code |

---

## 🚀 IF ASKED TO ADD FEATURE

**Pattern to follow:**
1. "I would add endpoint in Controller"
2. "Business logic goes in Service"
3. "Data access through Repository"
4. "Validate with annotations or custom logic"

**Example: "Add order history"**
> "Add `GET /orders/history` endpoint, filter by status and date range, add pagination with `Pageable`"

---

## ⚠️ THINGS TO AVOID SAYING

| ❌ Don't Say | ✅ Say Instead |
|--------------|----------------|
| "I don't know" | "I'd approach it by..." |
| "It's too complex" | "Let me think about this..." |
| "I copied from tutorial" | "I designed based on best practices" |
| "My code is perfect" | "For production I'd add..." |

---

## 💡 SMART QUESTIONS TO ASK THEM

1. "What database does Bajaj use - PostgreSQL or NoSQL?"
2. "Do you use microservices architecture?"
3. "How do you handle high-frequency trading requests?"
4. "What's the tech stack of your trading platform?"

---

## 🎯 DEMO FLOW (If Asked)

1. Start app → "Sample data loaded"
2. Open Swagger UI → "Interactive documentation"
3. GET /instruments → "15 stocks available"
4. POST /orders (MARKET) → "Executes immediately"
5. GET /trades → "Trade record created"
6. GET /portfolio → "Holdings with P&L"
7. DELETE /orders/{id} → "Cancel order"

---

## 📝 DESIGN DECISIONS SUMMARY

| Decision | Choice | Reason |
|----------|--------|--------|
| Storage | ConcurrentHashMap | Thread-safe, no setup |
| Docs | Springdoc | Modern, Spring Boot 3 compatible |
| Validation | Jakarta Bean | Declarative, clean |
| Errors | @RestControllerAdvice | Centralized, DRY |
| DTOs | Separate from Entity | Security, flexibility |

---

**🍀 GOOD LUCK! You built this - you know it best!**
