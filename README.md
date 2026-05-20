# Cloud Kitchen

Microservices food-ordering platform. An **API Gateway** authenticates requests via JWT and forwards verified identity to downstream services. Currently ships two modules: `api-gateway` and `user-service`. More domain services are planned.

## Architecture

```
Client ──► api-gateway (:8080) ──► user-service (:8001)
              │ validates JWT          │ owns auth + users (MySQL)
              │ injects X-User-Id      │ issues access + refresh tokens
              │ and X-User-Role
              └──► (planned) restaurant / order / payment / delivery / notification / review
```

Clients only reach the gateway. Internal services trust `X-User-Id` / `X-User-Role` headers — they must not be exposed to the public network.

## Tech used

- **Java 17**, **Spring Boot 3.5**, **Maven**
- **Spring Cloud Gateway 2025.0.1** (reactive) — edge routing + global JWT filter
- **Spring Web MVC** — user-service REST endpoints
- **Spring Data JPA** + **MySQL** — persistence
- **Spring Security** — `BCryptPasswordEncoder` for password hashing
- **JJWT 0.12.6** — JWT signing and verification (HMAC-SHA)
- **Jakarta Bean Validation** — DTO validation
- **Lombok** — boilerplate reduction

## Modules

| Module | Port | Role |
|---|---|---|
| [`api-gateway`](api-gateway/) | 8080 | Validates JWT, injects identity headers, routes to downstream services |
| [`user-service`](user-service/) | 8001 | Signup, login, user identity, role assignment, BCrypt password hashing |

## Run locally

Prereqs: JDK 17, MySQL 8.

```sql
CREATE DATABASE cloud_kitchen CHARACTER SET utf8mb4;
```

Set `spring.datasource.*` and `jwt.secret` (≥256 bits) in [`user-service/src/main/resources/application.yaml`](user-service/src/main/resources/application.yaml), then:

```bash
cd user-service && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
```

The api-gateway needs its own `application.yml` with the same `jwt.secret` and a route pointing `/api/auth/**` to `http://localhost:8001`.

## API (current)

| Method | Path | Auth | Purpose |
|---|---|---|---|
| POST | `/api/auth/signup` | public | Register a user |
| POST | `/api/auth/login` | public | Returns access + refresh token |

Roles: `CUSTOMER`, `ADMIN`, `DELIVERY_PARTNER`, `RESTAURANT_OWNER`.

## Roadmap

- Logout + refresh-token rotation with reuse detection (in progress)
- Service discovery (Eureka / Consul)
- Domain services: restaurant, order, payment, delivery, notification, review
- Centralized config, observability, CI/CD
