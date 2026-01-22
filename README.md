## Java Service Starter Template

This project is a small but production-leaning Spring Boot service intended as a **starter template** for new backend services.
The business logic is simple (a single "data processor" endpoint), and the focus is on:

- **Clear structure and separation of concerns**
- **Operational readiness** (health checks, basic observability, Docker image)
- **Extensibility** so other engineers can easily add new endpoints and domains

Tech stack:

- **Language**: Java 17
- **Framework**: Spring Boot 4.x
- **Build**: Maven
- **Database**: H2 (local dev) with JPA
- **Security**: Spring Security (HTTP Basic) with a JWT utility scaffold

---

## How to run the service locally

### Prerequisites

- **Java 17+**
- **Maven 3.9+**
- **Docker**

### Run with Maven

From the project root:

```bash
mvn clean package
mvn spring-boot:run
```

The application will start on port **8080** by default.

### Run the built JAR

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Run with Docker

Build the image:

```bash
docker build -t java-demo-app .
```

Run the container:

```bash
docker run --rm -p 8081:8080 java-demo-app
```

---

## API Endpoints

### Authentication

For now, the service uses simple **HTTP Basic auth** with an in-memory user:

- **Username**: `user`
- **Password**: `password`

This is purely for demo / template purposes and is not meant for production as-is.

### Health Check

- **Method**: `GET`
- **Path**: `/health`

Example:

```bash
curl -u user:password http://localhost:8081/health
```

Sample response:

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

This endpoint is meant to be used by load balancers / orchestration systems to decide if an instance is healthy.

### Data Processor

The assignment asks for `POST /example`. In this template, the endpoint is versioned under `/api/v1`:

- **Method**: `POST`
- **Path**: `/api/v1/example`

#### Request body

Currently the controller accepts a `DataProcessor` object, which roughly corresponds to:

```json
{
  "userId": 123,
  "dpValue": 42
}
```

This is close to the requested shape:

```json
{
  "userId": "123",
  "value": 42
}
```

In a next iteration, this would be tightened to use a dedicated request DTO with validation.

#### Response body

Responses use a simple `ApiResponse` envelope:

```json
{
  "status": "SUCCESS",
  "response": "DataProcessor created successfully",
  "requestId": "b3c6d9e6-2a4e-4a0a-9f87-123456789abc",
  "statusCode": 200
}
```

- **status**: high-level outcome (`SUCCESS` / `FAILURE`)
- **response**: human-readable message
- **requestId**: per-request UUID to help trace activity across logs
- **statusCode**: numeric code aligned with HTTP status

Example call:

```bash
curl -u user:password \
  -H "Content-Type: application/json" \
  -d '{"userId":123,"dpValue":42}' \
  http://localhost:8080/api/v1/example
```

---

## Design decisions

### Package structure

The code is structured by **layers and feature areas**:

- `com.example.demo.common`  
  Cross-cutting types like `ApiResponse`, `GlobalExceptionHandler`, and `Constants`.
- `com.example.demo.config`  
  Framework configuration (`SecurityConfig`, `H2ServerConfig`, `JwtTokenUtil`).
- `com.example.demo.health`  
  Health controller that exposes `/health`.
- `com.example.demo.dataprocessor`  
  Feature module for the data processor example:
    - `controller` – REST layer (`DataProcessorController`)
    - `service` – business logic (`DataProcessorService`, `DataProcessorServiceImpl`)
    - `jpa.entity` – persistence model (`DataProcessor`)
    - `jpa.repo` – repository interface (`DataProcessorRepo`)

This structure makes it easy to:

- Add new domains under their own package makes the application easily scalable and maintainable (`user`, `orders`, etc.).
- Keep HTTP, business logic, and persistence clearly separated.

### Layering and DTOs

The template follows a simple layered approach:

- **Controller layer**: Handles HTTP concerns, validation, and maps between API models and service calls.
- **Service layer**: Encapsulates business logic
- **Repository layer**: Handles data access via Spring Data JPA.

Right now, the controller accepts a JPA entity directly as the request body. For a real production service, I would introduce:

- A dedicated **request DTO** (e.g. `ExampleRequest`) and **response DTO**.
- A mapping layer (manual or MapStruct) to decouple external contracts from internal persistence models.

### Error handling

- A global `@ControllerAdvice` (`GlobalExceptionHandler`) catches unexpected exceptions and returns a structured 500 response.
- The `ApiResponse` type is used as a common file for success and failure responses from the data processor layer.

This gives a single, predictable error surface for clients and avoids leaking raw stack traces.

### Security

- Spring Security is configured via `SecurityConfig`:
    - **HTTP Basic** auth using an in-memory user (`user/password`).
    - Protects the main endpoints while keeping the configuration easy to understand.
- A `JwtTokenUtil` helper is included as a starting point for moving towards JWT-based auth or integration with an identity provider.

For a real service, the expectation is that teams will:

- Replace in-memory auth with a proper user store or SSO.
- Use JWT or another token strategy instead of basic auth.

### Database and migrations

- Uses **H2** for local development, exposed via a small H2 server configuration for convenience.
- Database schema is managed via **migration scripts** under `src/main/resources/db/migration`.

This is intended to encourage:

- Schema-as-code and repeatable migrations.
- Easy swapping of H2 with a real RDBMS (e.g. MySQL, Postgres) via configuration.

### Observability and operations

- **Actuator health** is enabled and the custom `/health` endpoint provides a human-friendly summary.
- Logging is configured with sensible defaults and the response model includes a `requestId` which can be put into logs for tracing.
- A **multi-stage Dockerfile** builds a lean runtime image for deployment and is easy to plug into CI/CD.

---

## Assumptions and trade-offs

- The service is focused on **showing structure**, not business complexity.
- H2 is used as the default database for easy local setup; production would point to a managed RDBMS.
- Security is intentionally simple (HTTP Basic + in-memory user) to keep the focus on the overall template.
- Some patterns are sketched but not fully built out (e.g. JWT helper, Redis directory) to indicate how the template would evolve in a larger system.
- Input validation and error contracts are minimal at this stage to keep the sample small, but the structure is ready to host them.

---

## Future roadmap

If this were the base for a long-lived production service, here are the next steps I would take:

- **API & validation**
    - Introduce dedicated request/response DTOs for all endpoints.
    - Add bean validation annotations (`@NotNull`, `@Min`, etc.) and global handlers for validation errors.
    - Flesh out consistent error response models and error codes.

- **Security**
    - Replace basic auth with JWT-based auth or integration with a centralized identity provider.
    - Make security configuration environment-aware (dev vs. prod) and configurable via properties.

- **Observability**
    - Add metrics (latency, request counts, error rates) via Actuator and/or OpenTelemetry.
    - Add alerts via cloudwatch or datadog for easy monitoring
    - Use structured logging and include `requestId` (and correlation IDs) for distributed tracing.

- **Resilience & performance**
    - Add retries, timeouts, and circuit breakers for downstream calls (if/when they exist).
    - Leverage caching (e.g. Redis) where appropriate, especially for read-heavy endpoints.
    - Consider rate limiting and backpressure mechanisms for high-traffic scenarios.

- **Testing & quality**
    - Expand unit tests for controllers, services, and repositories.
    - Add integration tests (using Cucumber)
    - Integrate with a CI pipeline (e.g. GitHub Actions, Jenkins) to run tests and build Docker images on every commit.

- **Deployment**
    - Add Kubernetes manifests with sensible defaults (readiness/liveness probes using `/health`, resource requests/limits, etc.).
    - Wire in autoscaling (HPA), centralized logging, and monitoring dashboards.

GitHub configuration will be added to define how code is validated, reviewed, and merged, ensuring consistency, quality, and scalability as the microservice evolves.

This template is intentionally small, but the goal is that its **structure and conventions** make it a solid starting point for building robust microservices in a real environment.

