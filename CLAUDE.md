# CLAUDE.md — auth-service

## Project Overview

`auth-service` is a stateless JWT-based authentication microservice built with **Spring Boot 4.0.0** and **Java 17**. It is part of the larger `crash2cost` platform and handles user registration and login, issuing JWT tokens consumed by other services.

- **Server port:** `8002`
- **Database:** MongoDB (`crash2cost` db, `users` collection)
- **Base package:** `io.github.mrlevi1112.authservice`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.0 |
| Language | Java 17 |
| Build | Maven (via `mvnw` wrapper) |
| Security | Spring Security (stateless) |
| Database | MongoDB via Spring Data MongoDB |
| JWT | jjwt 0.12.x (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) |
| Validation | Jakarta Bean Validation |
| Boilerplate | Lombok |

---

## Directory Structure

```
auth-service/
├── pom.xml
├── mvnw / mvnw.cmd                  # Maven wrapper scripts
├── src/
│   ├── main/
│   │   ├── java/io/github/mrlevi1112/authservice/
│   │   │   ├── AuthServiceApplication.java          # Entry point
│   │   │   ├── common/
│   │   │   │   ├── constants/AuthServiceConstants.java  # All string/int constants
│   │   │   │   └── enums/UserRole.java                  # USER, ADMIN
│   │   │   ├── config/
│   │   │   │   ├── MongoConfig.java                 # MongoDB client + template
│   │   │   │   └── SecurityConfig.java              # Spring Security filter chain
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java              # /api/auth/ endpoints
│   │   │   ├── dto/
│   │   │   │   ├── SignUpDTO.java
│   │   │   │   ├── LogInDTO.java
│   │   │   │   ├── TokenDTO.java
│   │   │   │   └── ErrorDTO.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java      # @RestControllerAdvice
│   │   │   ├── model/
│   │   │   │   └── User.java                        # MongoDB document
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java              # MongoRepository
│   │   │   ├── security/
│   │   │   │   ├── JwtUtil.java                     # Token generation + validation
│   │   │   │   ├── JwtAuthenticationFilter.java     # OncePerRequestFilter
│   │   │   │   └── CustomUserDetailsService.java    # UserDetailsService impl
│   │   │   └── service/
│   │   │       └── AuthService.java                 # Business logic
│   │   └── resources/
│   │       ├── application.yml                      # Primary config
│   │       └── application.properties               # MongoDB URI/database
│   └── test/
│       └── java/io/github/mrlevi1112/authservice/
│           └── AuthServiceApplicationTests.java     # Context load smoke test
```

---

## API Endpoints

All auth endpoints are publicly accessible (no JWT required).

| Method | Path | Body | Response |
|---|---|---|---|
| POST | `/api/auth//signup` | `SignUpDTO` | `TokenDTO` (200) or 400 |
| POST | `/api/auth//login` | `LogInDTO` | `TokenDTO` (200) or 401 |

> **Note:** The base path constant `AUTH_API_BASE = "/api/auth/"` has a trailing slash and the endpoint constants (`/signup`, `/login`) have a leading slash, producing a double-slash path (e.g., `/api/auth//signup`). Spring MVC normalizes this, but it is worth fixing the constants to be consistent.

### Request/Response Shapes

**POST /signup**
```json
// Request (SignUpDTO)
{ "username": "alice", "email": "alice@example.com", "password": "Passw0rd!" }

// Response (TokenDTO)
{ "tokenAccess": "<jwt>", "tokenType": "Bearer", "role": "USER" }
```

**POST /login**
```json
// Request (LogInDTO)
{ "username": "alice", "password": "Passw0rd!" }

// Response (TokenDTO)
{ "tokenAccess": "<jwt>", "tokenType": "Bearer", "role": "USER" }
```

**Error response (ErrorDTO)**
```json
{ "message": "...", "status": 400, "timestamp": "2024-..." }
```

---

## Environment Variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `JWT_SECRET` | **Yes** | — | Base64-encoded HMAC secret for JWT signing |
| `JWT_EXPIRATION` | No | `3600000` | Token TTL in milliseconds (default: 1 hour) |

Set these before running locally (e.g., in a `.env` file — already in `.gitignore`):
```bash
export JWT_SECRET=<base64-encoded-secret>
export JWT_EXPIRATION=3600000
```

---

## Configuration

### `application.yml` (primary)
- Port: `8002`
- App name: `crash2cost-api`
- Multipart limit: 10 MB
- CORS: allows `http://localhost:5173` with credentials
- Logging: DEBUG for `io.github.mrlevi1112` and Spring Security

### `application.properties`
- MongoDB URI: `mongodb://localhost:27017/crash2cost`
- Database: `crash2cost`

> `MongoConfig` also hardcodes the connection string via `AuthServiceConstants.Database.MONGO_CLIENT_CONNECTION`. If you change the MongoDB URI, update both `application.properties` and the constant.

---

## Key Conventions

### Constants
All string literals and magic numbers live in nested static classes inside `AuthServiceConstants`:
- `AuthServiceConstants.Validation` — field validation messages and limits
- `AuthServiceConstants.Database` — collection name, DB name, package, connection string
- `AuthServiceConstants.Security` — header names, endpoint patterns, prefixes, HTTP codes
- `AuthServiceConstants.AuthMessages` — business-logic error messages
- `AuthServiceConstants.Jwt` — Spring `@Value` placeholder strings

**Never add raw string literals or magic numbers to business logic.** Add them to the appropriate `AuthServiceConstants` nested class first.

### Lombok
All DTOs and the `User` model use:
- `@Data` — getters, setters, equals, hashCode, toString
- `@Builder` — builder pattern
- `@NoArgsConstructor` + `@AllArgsConstructor`

All Spring-managed components use `@RequiredArgsConstructor` for constructor injection (no `@Autowired`).

### Validation
- Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@Pattern`) is applied on both DTOs **and** the `User` model.
- `GlobalExceptionHandler` returns field-level validation errors as `Map<String, String>` with HTTP 400.
- Business rule violations (duplicate username/email, user not found) are thrown as `RuntimeException` and caught by `GlobalExceptionHandler` → `ErrorDTO`.

### Password Rules
- Length: 6–16 characters
- Must contain: at least one uppercase letter, one lowercase letter, one digit, one special character (`!@#$%^&*()_+-=[]{}...`)

### Username Rules
- Length: 2–32 characters

### User Roles
- New users always get `UserRole.USER` on signup.
- `UserRole.ADMIN` exists but is not assignable via the public API.
- Roles are stored as strings in MongoDB and as claims in JWT (`role` key).
- Spring authority format: `ROLE_USER` / `ROLE_ADMIN` (prefix added in `CustomUserDetailsService`).

### Security
- Sessions are **stateless** (`SessionCreationPolicy.STATELESS`).
- CSRF is disabled.
- All `/api/auth/**` requests are public; everything else requires a valid JWT.
- JWT filter (`JwtAuthenticationFilter`) runs before `UsernamePasswordAuthenticationFilter`.
- Passwords hashed with `BCryptPasswordEncoder`.

---

## Development Workflow

### Build
```bash
./mvnw clean package
```

### Run locally
```bash
# Requires a running MongoDB instance on localhost:27017
JWT_SECRET=<base64-secret> ./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

> The only test currently is a Spring context load check (`AuthServiceApplicationTests`). New features should include unit tests for service logic and integration tests for controllers.

### Adding a new endpoint
1. Define constants (path, messages) in `AuthServiceConstants`.
2. Create request/response DTOs in `dto/` with Lombok + Bean Validation annotations.
3. Add service method in `AuthService` (or a new `@Service`).
4. Add controller method in `AuthController` (or a new `@RestController`).
5. Update `SecurityConfig` if the endpoint has different auth requirements.

### Adding a new MongoDB model
1. Annotate with `@Document(collection = AuthServiceConstants.Database.<CONSTANT>)`.
2. Use `@Id` on the `String id` field.
3. Use `@Indexed(unique = true)` on unique fields.
4. Create a corresponding `MongoRepository` interface in `repository/`.

---

## Known Issues / Technical Debt

- **Double-slash API paths:** `AUTH_API_BASE = "/api/auth/"` + `SIGNUP_ENDPOINT = "/signup"` produces `/api/auth//signup`. Should remove the trailing slash from `AUTH_API_BASE` or the leading slash from the endpoint constants.
- **Hardcoded MongoDB URI in constants:** `AuthServiceConstants.Database.MONGO_CLIENT_CONNECTION` duplicates the value in `application.properties`. The `MongoConfig` should read from `application.properties` via `@Value` instead.
- **`AuthService.Jwt` dead field:** `public static final String Jwt = null;` in `AuthService` is unused and should be removed.
- **Minimal test coverage:** Only a context load test exists. Unit and integration tests are needed.
- **`DemoController` gitignored:** The `.gitignore` explicitly ignores `src/main/java/.../controller/DemoController.java`, suggesting a local-only scratch controller exists.
