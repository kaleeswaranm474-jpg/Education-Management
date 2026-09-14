# Education Management System

## 1. Project Overview

Education Management System is a Spring Boot REST API application developed to manage students, teachers, courses, enrollments, exams, and grades.

The application provides REST APIs for managing education-related data and includes authentication, authorization, validation, exception handling, auditing, logging, file management, pagination, searching, filtering, scheduling, API documentation, testing, and database migration.

---

## 2. Technologies Used

- Java 17
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT Authentication
- MySQL
- Flyway
- Hibernate
- Jakarta Validation
- Lombok
- Spring AOP
- Spring Actuator
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Maven

---

## 3. Main Modules

The application contains the following main modules:

### Student Module

Used to create, view, update, search, filter, and delete student records.

### Teacher Module

Used to manage teacher information.

### Course Module

Used to create and manage courses and associate courses with teachers.

### Enrollment Module

Used to manage student course enrollments.

### Exam Module

Used to manage examinations related to courses.

### Grade Module

Used to manage grades related to student enrollments and exams.

### Authentication Module

Used for user login and JWT token generation.

### File Module

Used for file upload and file download operations.

---

## 4. REST API Base URLs

The application runs on:

http://localhost:8082

API base path:

/api

Main endpoints:

- /api/auth
- /api/students
- /api/teachers
- /api/courses
- /api/enrollments
- /api/exams
- /api/grades
- /api/files

---

## 5. Authentication

The application uses JWT-based authentication.

Users must authenticate through the authentication API and receive a JWT token.

The token is then sent in the Authorization header.

Example:

Authorization: Bearer <JWT_TOKEN>

---

## 6. User Roles

The application supports the following roles:

- ADMIN
- TEACHER
- STUDENT

Role-based authorization is implemented using Spring Security.

Method-level authorization is implemented using `@PreAuthorize`.

---

## 7. Security Features

The application includes:

- JWT Authentication
- Role-Based Authorization
- Method-Level Security
- BCrypt Password Encoding
- Strong Password Policy
- Stateless Session Management
- CORS Configuration
- CSRF Protection Configuration
- JWT Expiration Configuration
- Protected REST Endpoints

---

## 8. Validation

Jakarta Bean Validation is used for validating request data.

The application uses validation annotations such as:

- @NotBlank
- @NotNull
- @Email
- @Min
- @Positive
- @Valid

Invalid request data is handled through the global exception handling mechanism.

---

## 9. Exception Handling

The application contains centralized exception handling.

Custom exceptions include:

- ResourceNotFoundException
- BadRequestException

Global exception handling is implemented using:

`@RestControllerAdvice`

The application returns a standard error response instead of exposing internal errors directly.

---

## 10. Database

Database:

education_management_db

Database technology:

MySQL

The application uses Spring Data JPA and Hibernate for database operations.

JPA relationships are implemented between the major entities.

---

## 11. JPA Relationships

The application contains relationships such as:

- Course → Teacher
- Enrollment → Student
- Enrollment → Course
- Exam → Course
- Grade → Enrollment
- Grade → Exam

These relationships are managed using JPA annotations.

---

## 12. Transaction Management

Transaction management is implemented using `@Transactional`.

Read-only operations use:

`@Transactional(readOnly = true)`

This helps maintain data consistency during database operations.

---

## 13. Pagination and Sorting

Pagination is implemented using Spring Data `Pageable`.

The API supports:

- Page number
- Page size
- Sorting

Pagination helps avoid loading large amounts of data at once.

---

## 14. Search and Filtering

The application supports searching and filtering for relevant modules.

Search and filtering are handled through repository and service layer methods.

---

## 15. Mapper

Mapper classes are used to convert between DTO objects and Entity objects.

The project contains mapper classes for:

- Student
- Teacher
- Course
- Enrollment
- Exam
- Grade

This keeps DTO and entity conversion separate from business logic.

---

## 16. DTO Layer

The application uses DTOs for request and response data.

DTOs help prevent direct exposure of entity objects through REST APIs.

Main DTO areas include:

- Student
- Teacher
- Course
- Enrollment
- Exam
- Grade
- Login

---

## 17. Logging

Spring AOP is used for application logging.

The project contains a LoggingAspect.

The aspect logs:

- Method start
- Method completion
- Method execution time

Example log:

Started method: createStudent

Completed method: createStudent in 41 ms

---

## 18. Auditing

JPA Auditing is implemented in the application.

Auditing is used to maintain information such as:

- Created date
- Last modified date

Auditing configuration is enabled using Spring Data JPA auditing.

---

## 19. Scheduler

The application contains a scheduled task for exam-related processing.

The scheduler checks upcoming examinations and processes exams scheduled within the configured period.

Scheduling is enabled using:

`@EnableScheduling`

---

## 20. File Upload and Download

The application provides file management APIs.

Base endpoint:

`/api/files`

The application supports:

- File Upload
- File Download

File path traversal protection is also implemented.

Maximum upload size is configured through the application configuration.

---

## 21. Swagger / OpenAPI

Swagger UI is used to document and test REST APIs.

Swagger URL:

http://localhost:8082/swagger-ui/index.html

Swagger provides:

- API endpoint documentation
- Request body documentation
- Response documentation
- Authentication testing
- API testing through browser

---

## 22. Actuator

Spring Boot Actuator is used for application monitoring.

Configured endpoints include:

- health
- info
- metrics

Actuator base path:

`/actuator`

Health endpoint:

`/actuator/health`

---

## 23. Profiles

The application supports different Spring profiles:

- Development
- Test
- Production

Configuration files include:

- application.yml
- application-dev.yml
- application-test.yml
- application-prod.yml

Profiles allow environment-specific configuration.

---

## 24. Environment Variables

Sensitive configuration values are externalized using environment variables.

Examples:

- DB_USERNAME
- DB_PASSWORD
- JWT_SECRET
- JWT_EXPIRATION_MS
- ADMIN_PASSWORD
- TEACHER_PASSWORD
- STUDENT_PASSWORD

This avoids hardcoding sensitive values directly into the main configuration.

---

## 25. Database Migration

Flyway is used for database migration.

Flyway manages database schema changes through versioned migration scripts.

The application uses Flyway together with MySQL.

---

## 26. Performance Optimization

Performance improvements include:

- Lazy loading where appropriate
- JOIN FETCH for required relationships
- Pagination
- Search and filtering
- Open Session in View disabled

Configuration:

`spring.jpa.open-in-view: false`

---

## 27. CORS

Cross-Origin Resource Sharing is configured in Spring Security.

The application supports configured frontend origins such as:

- http://localhost:3000
- http://localhost:4200

---

## 28. Testing

The project uses:

- JUnit 5
- Mockito
- Spring Boot Test
- Spring Security Test
- MockMvc

Testing includes:

- Unit Testing
- Service Layer Testing
- Integration Testing
- Security Integration Testing
- JWT Testing
- Password Policy Testing

---

## 29. Test Result

The complete Maven test execution currently passes successfully.

Test result:

Tests run: 83

Failures: 0

Errors: 0

Skipped: 0

Build result:

BUILD SUCCESS

Security integration tests:

Tests run: 4

Failures: 0

Errors: 0

---

## 30. Project Structure

The project follows a layered architecture.

```text
src
├── main
│   ├── java
│   │   └── com.example
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── mapper
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       ├── aspect
│   │       ├── scheduler
│   │       └── config
│   │
│   └── resources
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-test.yml
│       ├── application-prod.yml
│       └── db
│           └── migration
│
└── test
    └── java