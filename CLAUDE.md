# CLAUDE.md

Instructions for AI coding agents working in this repository.

## Project
Time deposit service for the XA Bank kata (see `docs/originalAssignment.md`).
Kotlin, Spring Boot 4, Gradle (version catalog in `gradle/libs.versions.toml`), JDK 25, PostgreSQL, Flyway, Testcontainers.

## Commands
- Build and test everything: `./gradlew build` (Docker must be running)
- Tests only: `./gradlew test` or `./gradlew :<module>:test`
- Run locally: `docker compose up -d && ./gradlew :bootstrap:bootRun`
- Run with a throwaway DB: `./gradlew :bootstrap:bootTestRun`
- Swagger UI: http://localhost:8080/swagger-ui.html

## Architecture (hexagonal) – see docs/architecture.md
Modules: `core`, `app`, `entrypoints`, `output-adapters`, `bootstrap`.
- `core` depends on nothing: no Spring, JPA or HTTP types.
- `app`, `entrypoints`, `output-adapters` depend only on `core`; adapters never depend on each other.
- Only `bootstrap` depends on everything (wiring, config, end-to-end tests).

Where does new code go?
- Business rule true without DB or web → `core`
- Coordinates steps (load → calculate → save, transactions) → `app`
- HTTP ↔ use case translation → `entrypoints`
- Talks to the DB or external systems → `output-adapters`
- Wiring, runtime config, whole-app tests → `bootstrap`

## Hard constraints (from the assignment)
- Do not change the public shape of `TimeDeposit` or the signature of `TimeDepositCalculator.updateBalance`.
- `updateBalance` behaviour must stay identical; `TimeDepositCalculatorTest` (characterization tests) must stay green.
- Exactly two endpoints: `GET /time-deposits` and `POST /time-deposits/balance-updates`. Do not add more.
- The calculator works with `Double` on purpose (legacy rounding). Use `BigDecimal` for money everywhere else.

## Adding a new interest plan
1. Add a value to `PlanType`.
2. Add an `InterestStrategy` implementation in `core/.../strategy` with its own eligibility rules.
3. Register it in `TimeDepositCalculator`'s default strategy list and add a unit test.

## Conventions
- Schema changes only through new Flyway migrations in `output-adapters/src/main/resources/db/migration`; never edit an applied migration.
- Tests that touch the DB use Testcontainers PostgreSQL, not H2.
- Document assumptions in KDoc where the code makes them.

## Working rules for the agent
- Advise by default; change files only when explicitly asked.
- Never commit or push. Leave changes uncommitted for review.
- Commits (made by the developer) are atomic with short conventional messages (`feat(core): …`, `test(bootstrap): …`).
