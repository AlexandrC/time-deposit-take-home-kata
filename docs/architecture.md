# Architecture

The service follows **Hexagonal Architecture (Ports & Adapters)**. Business logic sits in the
middle and knows nothing about HTTP or the database; everything technical plugs in from the outside.

## Hexagon

```mermaid
flowchart LR
    client([HTTP client / Swagger UI])
    db[(PostgreSQL)]

    subgraph entrypoints [entrypoints – driving adapter]
        controller[TimeDepositController<br/>DTOs · API mapper · OpenAPI]
    end

    subgraph hexagon [application core]
        direction TB
        subgraph inports [input ports]
            uc1[[GetTimeDepositsUseCase]]
            uc2[[UpdateBalancesUseCase]]
        end
        service[app: TimeDepositService<br/>transactions]
        subgraph domain [core: domain]
            calc[TimeDepositCalculator]
            strat[InterestStrategy<br/>Basic · Student · Premium]
            model[TimeDeposit · TimeDepositDetail<br/>Withdrawal · PlanType]
        end
        subgraph outports [output ports]
            p1[[TimeDepositRepositoryPort]]
            p2[[TimeDepositQueryPort]]
        end
    end

    subgraph adapters [output-adapters – driven adapter]
        jpa[TimeDepositRepositoryAdapter<br/>TimeDepositQueryAdapter<br/>JPA entities · Flyway]
    end

    client --> controller
    controller --> uc1 & uc2
    uc1 & uc2 -. implemented by .-> service
    service --> calc
    calc --> strat
    service --> p1 & p2
    jpa -. implements .-> p1 & p2
    jpa --> db
```

## Modules and dependency rule

```mermaid
flowchart LR
    bootstrap --> entrypoints
    bootstrap --> app
    bootstrap --> output-adapters
    bootstrap --> core
    entrypoints --> core
    app --> core
    output-adapters --> core
```

| Module | Contains | Depends on |
|---|---|---|
| `core` | `TimeDeposit`, `TimeDepositCalculator`, interest strategies, domain model, use case interfaces (input ports), repository ports (output ports) | nothing (plain Kotlin) |
| `app` | `TimeDepositService` – implements the use cases, owns transactions | `core` |
| `entrypoints` | REST controller, response DTOs, API mapper, OpenAPI annotations | `core` |
| `output-adapters` | JPA entities and repositories, port implementations, Flyway migrations | `core` |
| `bootstrap` | Spring Boot application, configuration, end-to-end tests | all |

Rules:
- `core` has no framework dependencies, so business rules are testable with plain unit tests.
- Adapters never depend on each other; they only meet in `bootstrap`.
- Gradle modules enforce the rule at compile time – a wrong import does not compile.

## Request flows

### `POST /time-deposits/balance-updates`

```mermaid
sequenceDiagram
    participant C as Client
    participant Ctl as TimeDepositController
    participant S as TimeDepositService
    participant R as TimeDepositRepositoryPort
    participant Calc as TimeDepositCalculator
    participant DB as PostgreSQL

    C->>Ctl: POST /time-deposits/balance-updates
    Ctl->>S: updateAllBalances()
    Note over S: @Transactional
    S->>R: findAll()
    R->>DB: SELECT time_deposits
    S->>Calc: updateBalance(deposits)
    Note over Calc: strategy per PlanType<br/>adds monthly interest
    S->>R: saveAll(deposits)
    R->>DB: UPDATE balance
    Ctl-->>C: 204 No Content
```

### `GET /time-deposits`

```mermaid
sequenceDiagram
    participant C as Client
    participant Ctl as TimeDepositController
    participant S as TimeDepositService
    participant Q as TimeDepositQueryPort
    participant DB as PostgreSQL

    C->>Ctl: GET /time-deposits
    Ctl->>S: getAllTimeDeposits()
    Note over S: @Transactional(readOnly)
    S->>Q: findAllWithWithdrawals()
    Q->>DB: SELECT … LEFT JOIN FETCH withdrawals
    Q-->>S: List<TimeDepositDetail>
    Ctl-->>C: 200 [ {id, planType, balance, days, withdrawals} ]
```

## Interest calculation – Strategy pattern

```mermaid
classDiagram
    class TimeDepositCalculator {
        +updateBalance(xs: List~TimeDeposit~)
    }
    class InterestStrategy {
        <<interface>>
        +planType: PlanType
        +calculateMonthlyInterest(deposit): Double
    }
    class BasicInterestStrategy
    class StudentInterestStrategy
    class PremiumInterestStrategy
    class PlanType {
        <<enum>>
        BASIC
        STUDENT
        PREMIUM
    }
    TimeDepositCalculator --> "*" InterestStrategy
    InterestStrategy <|.. BasicInterestStrategy
    InterestStrategy <|.. StudentInterestStrategy
    InterestStrategy <|.. PremiumInterestStrategy
    InterestStrategy --> PlanType
```

| Plan | Annual rate (1/12 per update) | Eligible when |
|---|---|---|
| basic | 1 % | `days > 30` |
| student | 3 % | `31 <= days <= 365` |
| premium | 5 % | `days > 45` |

- Each strategy owns its eligibility rules and rounding, so rules can change per plan independently.
- **Open/Closed:** a new plan = new `PlanType` value + new strategy; the calculator does not change.
- Strategies are injected through the calculator's constructor with a default list, so the public
  `updateBalance(List<TimeDeposit>)` signature and all existing callers stay unchanged.
- Unknown plan types get no interest, exactly like the legacy code.

## Persistence

```mermaid
erDiagram
    time_deposits ||--o{ withdrawals : has
    time_deposits {
        INTEGER id PK
        VARCHAR plan_type
        INTEGER days
        NUMERIC balance
    }
    withdrawals {
        INTEGER id PK
        INTEGER time_deposit_id FK
        NUMERIC amount
        DATE date
    }
```

- Schema is owned by Flyway (`V1__create_time_deposits_and_withdrawals.sql`); Hibernate only validates (`ddl-auto: validate`).
- Table and column names are snake_case (PostgreSQL convention) for the assignment's `timeDeposits` / `timeDepositId`.
- No `CHECK` constraint on `plan_type`, so adding a plan needs no migration.

## Key decisions

| Decision | Why |
|---|---|
| Calculator keeps `Double` | Switching to `BigDecimal` would change rounding; the assignment says behaviour must stay identical. Characterization tests guard it. |
| `BigDecimal` everywhere else | Money in DB (`NUMERIC(19,2)`) and API; the adapter converts at the boundary. |
| Separate write port and query port | Write side works with `TimeDeposit` (what the calculator needs); read side returns `TimeDepositDetail` with withdrawals. Each stays simple. |
| Unidirectional `@OneToMany` for withdrawals, no cascade | Withdrawals are read-only history; the domain `Withdrawal` has no back-reference. |
| Fetch-join for GET | Loads deposits and withdrawals in one query (no N+1). |
| `days` not incremented by the update | Assumed to be maintained by another system (see `UpdateBalancesUseCase` KDoc). |
| Code-first OpenAPI (springdoc) | Contract is generated from the controller annotations and served at `/v3/api-docs`, so it cannot drift from the code. |
| Testcontainers PostgreSQL in tests | Tests run against the same database engine as production. |
