# Mini Sovereign IAM System

Spring Boot prototype for centralized IAM with JWT authentication, RBAC + ABAC authorization, and audit logging.

## Tech stack
- Java 21 + Spring Boot
- PostgreSQL for identities/roles/agencies
- MongoDB for append-only audit logs
- Docker Compose for local infra

## Modules
- `auth`: login + JWT issuance/validation
- `user`: user registration and management
- `role` / `agency`: role and agency entities
- `policy`: centralized RBAC + ABAC decision logic
- `resource`: protected agency APIs (`/finance/records`, `/health/records`, `/land/records`)
- `audit`: logs for login/access outcomes

## Run locally
```bash
docker compose up -d
./mvnw spring-boot:run
```
(If Maven wrapper is not present in your environment, use `mvn spring-boot:run`.)

## Quick flow
1. Ensure bootstrap admin is enabled (see `application.yml`) or pre-seed an admin.
2. Login: `POST /auth/login`
3. Call protected APIs with `Authorization: Bearer <jwt>`
4. View logs: `GET /audit/logs` (ADMIN or AUDITOR)

## Policy highlights
- Deny-by-default
- ADMIN can access all resources
- AUDITOR is read-only
- Non-admin users cannot access cross-agency resources
- Clearance level must be >= resource classification

## Run and test guide
See [`HOW_TO_RUN.md`](./HOW_TO_RUN.md) for full setup and verification instructions.
