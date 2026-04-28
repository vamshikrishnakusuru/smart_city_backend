# Smart City Backend

Production-ready Spring Boot backend for the Smart City Management System frontend deployed at `https://smart-city-management-one.vercel.app`.

## Tech Stack

- Spring Boot 3
- Spring Security with JWT
- Spring Data JPA / Hibernate
- MySQL
- ModelMapper
- Swagger / OpenAPI

## Features

- User and admin registration/login with JWT
- Role-based access control using `USER` and `ADMIN`
- Issue reporting and issue history for users
- City-based issue management for admins
- Automatic issue assignment to an admin from the same city when available
- Global exception handling with structured API responses
- Pagination on issue listing endpoints
- CORS configured for the deployed frontend

## Run Locally

1. Create MySQL access for database `smart_city_db`.
2. Update [`application.yml`](/c:/smartcity/smart-city-backend/src/main/resources/application.yml) if your local credentials differ.
3. Start the app with `mvn spring-boot:run`.

## Swagger

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`
