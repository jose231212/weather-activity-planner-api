[English](README.md) | [Español](README.es.md)

# Weather Activity Planner API

A REST API for managing planned activities and evaluating weather conditions that may affect them.

I built this project to practice developing a Java backend with Spring Boot. It brings together event management, external weather data, a decision-making service, database persistence, API documentation, and automated tests.

The application uses Open-Meteo to retrieve location and forecast information. Its evaluation logic checks weather conditions against the limits defined for an activity and determines whether the activity can continue as planned, should be rescheduled, or should be cancelled.

## Features

- Create, retrieve, update, and delete events.
- Store event details and weather-related thresholds in MySQL.
- Retrieve city location information through the Open-Meteo geocoding service.
- Retrieve hourly forecast data through the Open-Meteo forecast service.
- Evaluate temperature, precipitation probability, and wind speed against activity thresholds.
- Produce one of three evaluation decisions: `MAINTAIN`, `RESCHEDULE`, or `CANCEL`.
- Support paginated and sorted event queries.
- Validate incoming data and event temperature ranges.
- Handle application errors through a centralized exception handler.
- Document the REST API with Swagger UI and OpenAPI.
- Include unit, persistence, and controller integration tests.

## Tech stack

- **Java 21** — programming language.
- **Spring Boot 4.1.2** — application framework.
- **Spring Web MVC** — REST endpoints and HTTP handling.
- **Spring Data JPA** — data access and persistence.
- **Hibernate** — object-relational mapping.
- **MySQL** — relational database.
- **Maven** — dependency and build management.
- **Lombok** — reduction of boilerplate code.
- **Open-Meteo** — geocoding and weather forecast data.
- **JUnit 5 and Mockito** — automated testing.
- **Springdoc OpenAPI / Swagger UI** — API documentation.
- **Docker**
- **Docker Compose**

## Requirements

Before running the application, make sure you have:

- JDK 21.
- MySQL running locally or a reachable MySQL server.
- Git, if you want to clone the repository.

The project includes the Maven Wrapper, so you can build and run it without installing Maven separately.

## Configuration

Create a MySQL database named `apiweather`:

```sql
CREATE DATABASE apiweather;
```

The application reads the database password from the `DB_PASSWORD` environment variable. Set it before starting the application. Do not commit real credentials to the repository.

In PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

The default local database configuration is:

- **Database:** `apiweather`
- **URL:** `jdbc:mysql://localhost:3306/apiweather`
- **Username:** `root`

Review `src/main/resources/application.properties` if your MySQL host, port, database name, or username is different.

The Open-Meteo service base URLs are also configured in `application.properties`. The application uses the public geocoding and forecast APIs provided by Open-Meteo.

## Run the application

From the project root, start the application with the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

You can also run the main application class from your IDE.

Once the application has started, the default local base URL is:

`http://localhost:8080`

### Running with Docker

The project also supports Docker Compose, allowing the API and MySQL database to run in separate containers without requiring a local Java or MySQL installation.

**Requirements:** Docker Desktop with Docker Compose.

1. Clone the repository.
2. Create a `.env` file in the project root, in the same directory as `docker-compose.yml`.
3. Configure the required environment variables:

```dotenv
MYSQL_ROOT_PASSWORD=your_mysql_password
MYSQL_DATABASE=apiweather
DB_PASSWORD=your_mysql_password
```

Use the same password for `MYSQL_ROOT_PASSWORD` and `DB_PASSWORD`. Replace the example values with your own credentials. Keep the `.env` file private; it is excluded from Git.

4. Build the Docker image:

```bash
docker build -t weather-activity-planner:1.0 .
```

5. Start the API and database:

```bash
docker compose up -d
```

Docker Compose starts the MySQL database and waits for its health check to succeed before starting the API. The database uses a named volume to preserve its data when containers are removed or recreated.

To check the running containers:

```bash
docker compose ps
```

To view the API logs:

```bash
docker compose logs api
```

To stop the containers:

```bash
docker compose down
```

The named volume is preserved when using `docker compose down`. To remove the containers and the database volume as well, use `docker compose down -v`. Be aware that this permanently deletes the persisted database data.

## API documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI document:

`http://localhost:8080/v3/api-docs`

Use Swagger UI to inspect the available operations, request parameters, response formats, and documented error responses.

## Main routes

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/events` | Create an event |
| `GET` | `/api/events` | Retrieve events using pagination and sorting |
| `GET` | `/api/events/{id}` | Retrieve an event by its ID |
| `PUT` | `/api/events/{id}` | Update an existing event |
| `DELETE` | `/api/events/{id}` | Delete an event |

## Weather evaluation logic

The evaluation service compares hourly forecast data with the thresholds configured for an activity.

It considers:

- Minimum and maximum temperature.
- Maximum precipitation probability.
- Maximum wind speed.
- Whether the activity can be rescheduled.

The service returns one of the following decisions:

| Decision | Meaning |
|---|---|
| `MAINTAIN` | The evaluated weather conditions meet the activity's configured limits. |
| `RESCHEDULE` | At least one condition is outside the configured limits and the activity can be rescheduled. |
| `CANCEL` | At least one condition is outside the configured limits and the activity cannot be rescheduled. |

When conditions are unfavorable, the evaluation result can also include an indoor alternative suggestion.

## Error handling

The application uses centralized exception handling and returns structured error responses. Depending on the situation, responses can include:

- `400 Bad Request` — invalid input or business rule violation.
- `404 Not Found` — requested resource does not exist.
- `503 Service Unavailable` — the external weather provider cannot be reached or returns an error.
- `500 Internal Server Error` — unexpected application error.

Validation errors can include details identifying the fields that need attention.

## Tests

The test suite was run successfully during project validation:

```text
Tests run: 48, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

To run the tests yourself:

```powershell
.\mvnw.cmd clean test
```

The project includes unit tests for the weather clients and evaluation logic, persistence tests for the event repository, service tests, and controller integration tests that exercise HTTP requests against the application and database.

## Project status

The event management API, Open-Meteo integration, weather evaluation logic, persistence layer, Swagger documentation, and automated tests have been implemented and validated.

This project is part of my ongoing practice building Java backend applications. It gave me the opportunity to work with external APIs, apply business rules to real forecast data, persist application data, and verify the behavior of a Spring Boot application through automated tests.
