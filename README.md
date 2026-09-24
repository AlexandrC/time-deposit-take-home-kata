# XA Bank – Time Deposit Service

Solution to the [Time Deposit Refactoring Kata](docs/originalAssignment.md).
Kotlin · Spring Boot 4 · PostgreSQL · Flyway · Testcontainers · Hexagonal Architecture.

- Architecture and design decisions: [docs/architecture.md](docs/architecture.md)
- AI-assisted development setup: [AI-assisted development](#ai-assisted-development) and [CLAUDE.md](CLAUDE.md)

## Prerequisites

- JDK 25 (Gradle toolchain)
- Docker (for the local database and for Testcontainers in tests)

## Run the application

**Option A – local PostgreSQL via Docker Compose**

```bash
docker compose up -d                 # starts PostgreSQL on localhost:5432
./gradlew :bootstrap:bootRun         # starts the app on http://localhost:8080
```

Flyway creates the schema on startup. Connection settings can be overridden with
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD`.

**Option B – throwaway PostgreSQL via Testcontainers**

```bash
./gradlew :bootstrap:bootTestRun
```

Starts a fresh PostgreSQL container for the lifetime of the app. Nothing to set up, data is lost on stop.

### Load sample data (Option A)

The database starts empty. After the app has started once (so Flyway has created the tables):

```bash
docker exec -i time_deposit psql -U postgres -d time_deposit <<'SQL'
INSERT INTO time_deposits (id, plan_type, days, balance) VALUES
  (1, 'basic',    45, 1000.00),  -- +0.83
  (2, 'basic',    20, 1000.00),  -- first 30 days: no interest
  (3, 'student', 200, 2000.00),  -- +5.00
  (4, 'student', 400, 2000.00),  -- older than 1 year: no interest
  (5, 'premium',  40, 1200.00),  -- before day 45: no interest
  (6, 'premium',  60, 1200.00);  -- +5.00
INSERT INTO withdrawals (id, time_deposit_id, amount, date) VALUES
  (1, 1, 50.00, '2026-01-15');
SQL
```

## Trigger the endpoints via Swagger

Open **http://localhost:8080/swagger-ui.html** (OpenAPI JSON: `/v3/api-docs`).

| Endpoint | What it does | Response |
|---|---|---|
| `GET /time-deposits` | Returns all time deposits with `id`, `planType`, `balance`, `days`, `withdrawals` | `200` + JSON array |
| `POST /time-deposits/balance-updates` | Applies one month of interest to every deposit and saves the new balances | `204` |

In Swagger UI: expand an endpoint → **Try it out** → **Execute**.
A typical check: `GET` → `POST` → `GET` again and compare balances.

Or with curl:

```bash
curl http://localhost:8080/time-deposits
curl -X POST http://localhost:8080/time-deposits/balance-updates
```

> `POST` is not idempotent – every call applies another month of interest.

## Run the tests

```bash
./gradlew test
```

Docker must be running: persistence and end-to-end tests use a real PostgreSQL via Testcontainers.

| Module | Tests |
|---|---|
| `core` | Characterization tests of `TimeDepositCalculator.updateBalance`, unit tests per interest strategy |
| `entrypoints` | Controller tests (MockMvc, use cases mocked) |
| `output-adapters` | JPA adapters against PostgreSQL (`@DataJpaTest` + Testcontainers) |
| `bootstrap` | End-to-end API tests: HTTP → service → DB |

## Assumptions

- `days` (deposit age) is maintained outside this service; the update endpoint applies one month of interest and does not change `days`.
- Unknown plan types earn no interest (legacy behaviour kept).
- `TimeDepositCalculator` still calculates with `Double` to keep the exact legacy rounding; money is `BigDecimal` everywhere else.
- Withdrawals are read-only history; no endpoint creates or changes them.

## AI-assisted development

<!-- TODO: fill in / adjust -->

**Tools**
- Claude (Anthropic) in the Claude desktop app (Cowork mode) with access to the project folder – used as a reviewer and pair programmer.
- IntelliJ IDEA for writing, running and committing the code.

**Setup and rules**
- Repository instructions for the agent live in [CLAUDE.md](CLAUDE.md) (architecture rules, commands, conventions).
- Working mode: the AI proposes and explains; I write or accept each change myself and make every commit myself (atomic, conventional commit messages).
- Behaviour of `updateBalance` was locked with characterization tests *before* any refactoring, so AI-suggested changes could be verified mechanically.

**What was AI-assisted and why**
- Architecture discussion: module split, dependency rule, ports and use cases.
- Refactoring the calculator into per-plan strategies, checked against the characterization tests.
- Test scaffolding (Testcontainers config, MockMvc tests) and this documentation.
- Reviews of each step before committing.
