# How to Run and Test Locally

## 1) Prerequisites
- Java 21
- Maven 3.9+
- Docker + Docker Compose

## 2) Start infrastructure
```bash
docker compose up -d
```

This starts:
- PostgreSQL at `localhost:5432`
- MongoDB at `localhost:27017`

## 3) (Optional) Configure bootstrap admin via env vars
Defaults are already set in `application.yml`, but you can override:

```bash
export BOOTSTRAP_ADMIN_ENABLED=true
export BOOTSTRAP_ADMIN_EMAIL=admin@sovereign.gov
export BOOTSTRAP_ADMIN_PASSWORD='ChangeMeNow123!'
export BOOTSTRAP_ADMIN_AGENCY=FINANCE
export BOOTSTRAP_ADMIN_CLEARANCE=HIGH
```

## 4) Run the application
```bash
mvn spring-boot:run
```

App URL: `http://localhost:8080`

## 5) Login and get token
```bash
curl -s -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@sovereign.gov","password":"ChangeMeNow123!"}'
```

Copy `token` from the response.

## 6) Access protected resources
```bash
TOKEN='<paste-jwt-token>'

curl -s http://localhost:8080/finance/records -H "Authorization: Bearer $TOKEN"
curl -s http://localhost:8080/health/records  -H "Authorization: Bearer $TOKEN"
curl -s http://localhost:8080/land/records    -H "Authorization: Bearer $TOKEN"
```

## 7) Read audit logs (ADMIN or AUDITOR)
```bash
curl -s http://localhost:8080/audit/logs -H "Authorization: Bearer $TOKEN"
```

## 8) Suggested verification scenarios
1. Finance admin/officer accessing finance records -> allowed.
2. Finance officer accessing health records -> denied and logged.
3. Invalid login password -> denied and logged once.
4. Auditor account (if created) reading logs -> allowed.

## 9) Stop infrastructure
```bash
docker compose down
```
