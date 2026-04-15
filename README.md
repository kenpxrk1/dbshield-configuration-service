# 🧩 Service Overview

- Name: `dbshield-configuration-service`
- High-level purpose: Centralize and govern database connection configurations for the DB Shield ecosystem so other services can reliably discover *which* database to connect to in a given environment and with which credentials.
- Key responsibilities: Create/update/delete configurations, toggle availability (enable/disable), enforce uniqueness by environment, and expose a simple read API for downstream consumers.
- Problem it solves: Prevents hard-coded or scattered DB connection details across services, enabling controlled rotation, environment separation, and operational visibility.

# 🏗 Architecture

- Architectural style: Classic layered Spring Boot service (Controller → Service → Repository) with DTO mapping and a persistence layer managed via Liquibase.
- Main modules and their responsibilities:
- Controller layer: `DatabaseConfigurationController` exposes REST endpoints under `api/v1/configs` for CRUD + enable/disable actions.
- Service layer: `ConfigurationServiceImpl` implements transactional business logic and validation of existence, and orchestrates mapping + persistence.
- Repository layer: `DatabaseConfigurationRepository` is a Spring Data JPA repository backed by PostgreSQL.
- DTO / mapping layer: `DatabaseConfigurationCreateRequest`, `DatabaseConfigurationUpdateRequest`, and `DatabaseConfigurationResponse` are immutable records mapped via MapStruct (`DatabaseConfigurationMapper`).
- Configurations: `SwaggerConfig` registers OpenAPI metadata; `LogEndpointAspect` logs controller entry/exit; `GlobalExceptionHandler` standardizes error responses.

Key design decisions and why they were made:
- JPA + Liquibase: Enables controlled schema evolution and a strong entity model for a configuration domain that benefits from relational constraints.
- MapStruct: Keeps mapping logic explicit and compile-time safe while preventing accidental updates to immutable fields (name, type, environment).
- Enable/disable flag: Allows temporary removal from active use without deleting historical configuration data.

# 🔄 Business Logic

Core flows (what happens and why it exists):

- Create configuration
- Request is validated (host/port/database/user/password required, type + environment required).
- DTO is mapped to entity with `enabled=true` by default.
- Password is stored in the `encryptedPassword` field (currently assigned directly from the request, implying encryption is expected upstream or missing in this service).
- Entity is persisted and returned as a response without the password.

- Update configuration
- Fetch by ID or return 404 if missing.
- Only mutable fields are updated (host, port, databaseName, username). Name/type/environment are intentionally immutable to preserve identity and uniqueness guarantees.
- Password is updated only when provided.

- Enable/Disable configuration
- Fetch by ID; toggle the `enabled` flag to control whether downstream consumers should use the config.

- Delete configuration
- Requires existence; removes the configuration entirely.

Why this logic exists:
- Environments are explicit and immutable to prevent cross-environment drift (e.g., PROD config accidentally repurposed for DEV).
- Enable/disable supports safe operational changes without losing history or re-creating records.
- Unique constraint on `(name, environment)` ensures a stable, human-meaningful key in addition to UUIDs.

# 🔗 Integrations

- PostgreSQL (primary storage)
- Data written: database configs (host, port, database name, username, password, type, environment, enabled flag, timestamps).
- When: on create/update/enable/disable/delete; reads on get/getAll.
- Why: relational integrity and queryability for configuration governance.

- REST API (incoming)
- Consumers: Admin UI, platform operators, or other DB Shield services needing configuration lookup.
- Data received: configuration requests (create/update) and control commands (enable/disable).
- Why: provides a single source of truth for DB connection settings.

- OpenAPI/Swagger
- Exposes API documentation via springdoc defaults.
- Why: developer discoverability and contract visibility.

There are no message brokers (Kafka/RabbitMQ), external service clients, or authentication integrations defined in this codebase.

# 🗄 Data Model

Main entity: `database_configuration`
- `id` (UUID): primary key.
- `name`: human-readable configuration name.
- `db_type`: `POSTGRES` or `ORACLE`.
- `environment`: `PROD`, `TEST`, or `DEV`.
- `host`, `port`, `database_name`, `username`: connection details.
- `encrypted_password`: stored secret (currently assigned directly from request).
- `enabled`: operational toggle.
- `created_at`, `updated_at`: audit timestamps.

Relationships:
- Single table, no foreign keys. This is a configuration domain with a flat model to keep lookups simple and fast.

Schema design rationale and trade-offs:
- Unique constraint `(name, environment)` prevents conflicting configs for the same environment.
- Index on `enabled` improves filtering for active configs.
- Flat schema minimizes joins and is optimized for high read frequency.
- Secrets live in the same table, which is operationally simple but pushes encryption and access control requirements into the service or DB layer.

# ⚙️ Configuration

Key properties (from `application.yaml` and `application-local.yaml`):
- Service name: `dbshield-configuration-service`
- Active profile: `local`
- Server port: `8081`
- Data source: PostgreSQL `jdbc:postgresql://localhost:5432/db-shield`
- Schema: `configuration-service`
- JPA: `ddl-auto=none`, `open-in-view=false`, SQL logging enabled
- Liquibase: `classpath:db/changelog/db.changelog-master.yaml`

Environment variables you can override in production:
- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA`

Operational notes:
- The schema `configuration-service` must exist before startup; Liquibase creates tables but does not create the schema itself.
- Java toolchain is set to Java 25 in `build.gradle`.

Local run (example):
- `./gradlew bootRun`
- `./gradlew test`

# 🚀 How It Works End-to-End

Scenario: Onboarding a new production database for the DB Shield platform

- Operator creates a configuration via `POST /api/v1/configs` with `environment=PROD`, `dbType=POSTGRES`, and connection details.
- Service validates the request, persists it, and marks it as `enabled=true`.
- Downstream services (e.g., a DB Shield policy engine or proxy) call `GET /api/v1/configs` or `GET /api/v1/configs/{id}` to retrieve connection info for runtime access decisions.
- If the database must be temporarily removed from rotation, the operator calls `POST /api/v1/configs/{id}/disable`; downstream services can exclude disabled configs from use.

# 📈 Scalability & Performance Considerations

Current state:
- No caching layer; all reads go to PostgreSQL.
- `getAll()` returns the full table; no pagination or filtering is implemented.
- Index exists on `enabled` which helps active-only queries if implemented later.

Potential improvements:
- Add pagination and filtering endpoints to prevent full-table reads as the number of configs grows.
- Introduce read-through caching for frequently accessed active configurations.
- Encrypt `encrypted_password` at rest and decrypt only when explicitly requested by authorized clients.
- Consider eventing or change streams if other services need to react to updates in near real time.

# 🔐 Security

Observed behavior:
- No authentication or authorization is implemented in this service.
- `LogEndpointAspect` logs full controller arguments and results, which may include passwords in create/update requests.
- Passwords are stored in the `encrypted_password` column but are assigned directly from the request without encryption.

Recommendations for production hardening:
- Enforce authentication (JWT/OAuth2 or mTLS) and authorization per role (admin vs. read-only).
- Mask or omit sensitive fields in logs.
- Integrate with a secrets manager or encrypt values before persistence.
- Apply TLS between clients and the service, and between the service and PostgreSQL.

# 🧪 Testing

Current coverage:
- Unit tests for the mapper (`DatabaseConfigurationMapperTest`).
- Unit tests for the service layer using Mockito (`ConfigurationServiceImplTest`).

Gaps:
- No integration tests for controller endpoints, validation, or Liquibase migrations.
- No persistence tests for JPA mappings, auditing, or schema consistency.

# 🧠 Design Decisions & Trade-offs

- Layered architecture was chosen for clarity and maintainability; it keeps HTTP concerns, business logic, and persistence isolated.
- MapStruct ensures compile-time mapping correctness and prevents accidental mutation of immutable fields, but adds build-time code generation complexity.
- Storing credentials alongside metadata simplifies lookup but creates a high-security surface area; a secrets manager or vault would reduce blast radius.
- Enable/disable provides operational safety but requires consumers to respect the flag; enforcement is not built into this service.
- Auditing annotations exist (`createdAt`, `updatedAt`), but there is no visible `@EnableJpaAuditing` configuration in this codebase; auditing likely depends on external configuration or is currently non-functional.
- UUIDs are required at persistence time, but the entity does not declare generation strategy; this implies IDs are expected to be set by the application or database triggers.

# 📦 Role in Microservice Ecosystem

- Acts as the configuration registry for database connections used by other DB Shield services.
- Downstream services depend on it to discover database targets and determine which ones are active.
- If it is down, new configurations cannot be created or updated, and services without caching may fail to initialize or refresh connection metadata.

# 🎤 Presentation Summary (IMPORTANT)

- Central registry for database connection configurations across environments.
- Clean layered architecture with REST API, service logic, JPA repository, and Liquibase-managed schema.
- Enforced uniqueness per `(name, environment)` and operational toggles via enable/disable.
- Designed for discoverability with OpenAPI and controller-level request/response logging.
- Current build targets Java 25 and PostgreSQL with schema-based isolation.
- Security hardening needed: authentication, secret encryption, and log masking.
- Clear extension path for pagination, caching, and event-driven updates.
