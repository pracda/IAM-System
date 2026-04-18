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

## Conflict-resolution note
This branch keeps the second-pass behavior:
- optional bootstrap admin creation at startup,
- single-event login failure auditing,
- safe principal extraction in resource authorization flow.

## Run and test guide
See [`HOW_TO_RUN.md`](./HOW_TO_RUN.md) for full setup and verification instructions.
