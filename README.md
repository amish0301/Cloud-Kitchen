# Cloud Kitchen

A small microservice demo composed of an API Gateway and a user service.

Summary
- Purpose: demo routing and basic authentication flow between an API gateway and a user service.
- Current scope: authentication (signup/login) with JWTs, gateway-side JWT verification and forwarding of user info, basic request validation and test scaffolding.

Features implemented
- User service (user-service)
  - Signup and login endpoints that validate input and issue JWTs.
  - JWT utilities for token creation and verification.
  - DTOs and validation for authentication requests.
  - Configuration placeholders for JWT secret and expiration.
- API Gateway (api-gateway)
  - Gateway routing and a global filter that verifies JWTs on incoming requests.
  - The gateway injects authenticated user information into forwarded requests as headers.
- Tests
  - Basic Spring Boot test scaffolding present for both modules.

What has been implemented so far
- End-to-end authentication flow: clients can sign up / log in via the user service and receive JWTs; the API Gateway validates those JWTs and propagates user info downstream.
- Infrastructure ready for persistence (JPA + MySQL driver included), though data model details are not part of the recent commit.
- Project includes validation and test dependencies to support further development.

Dependencies
- Spring Boot (core)
  - What: application framework and auto-configuration.
  - Where: both modules (api-gateway, user-service); used as the runtime foundation and for application entry points.
- spring-boot-starter-web
  - What: REST controllers and web layer.
  - Where: user-service; used by signup/login controllers and request handling.
- spring-boot-starter-security
  - What: security primitives available to secure endpoints.
  - Where: user-service (security support for authentication features).
- spring-boot-starter-validation
  - What: bean/request validation.
  - Where: user-service; used by authentication DTOs to validate signup/login input.
- spring-boot-starter-data-jpa
  - What: JPA support for persistence.
  - Where: user-service; included to support future persistence layers (entities/repositories not added in this commit).
- Spring Cloud Gateway (spring-cloud-starter-gateway)
  - What: API Gateway and routing.
  - Where: api-gateway; used for routing and the global JWT verification filter.
- JJWT (io.jsonwebtoken: jjwt-api, jjwt-impl, jjwt-jackson)
  - What: JWT creation, signing, parsing and validation.
  - Where: user-service (token generation/validation) and api-gateway (token parsing/validation in gateway filter).
- mysql-connector-j
  - What: MySQL JDBC driver.
  - Where: user-service; runtime driver for database connectivity when persistence is enabled.
- Lombok
  - What: reduces boilerplate for DTOs and models.
  - Where: user-service; used in request/response DTOs.
- Testing libraries (spring-boot-starter-test, spring-security-test)
  - What: testing utilities and helpers.
  - Where: both modules; used for basic test scaffolding and security-related tests.