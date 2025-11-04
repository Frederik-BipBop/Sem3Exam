# README
Name: Frederik Bastiansen
How many User-stories made: 5 / 8

# Setup
Opret en PostgreSQL database (f.eks. candidate)

Opdater config.properties med dine DB-loginoplysninger:

DB_NAME=candidate
DB_USERNAME=postgres
DB_PASSWORD=postgres

Kør Main.java – server starter på:
 http://localhost:7070/api

Populator kører igennem main



# Candidate API Documentation
| Method   | Endpoint                                         | Role   | Description                                    |
| -------- | ------------------------------------------------ | ------ | ---------------------------------------------- |
| `GET`    | `/api/candidates`                                | ANYONE | Get all candidates                             |
| `GET`    | `/api/candidates/{id}`                           | ANYONE | Get one candidate (includes skill market data) |
| `POST`   | `/api/candidates`                                | ADMIN  | Create a new candidate                         |
| `PUT`    | `/api/candidates/{id}`                           | ADMIN  | Update a candidate                             |
| `DELETE` | `/api/candidates/{id}`                           | ADMIN  | Delete a candidate                             |
| `PUT`    | `/api/candidates/{candidateId}/skills/{skillId}` | ADMIN  | Link existing skill to candidate               |
| `DELETE` | `/api/candidates/{candidateId}/skills/{skillId}` | ADMIN  | Remove skill from candidate                    |
| `GET`    | `/api/candidates/filter?category={category}`     | ANYONE | Filter candidates by skill category            |


# Skills API Documentation

| Method   | Endpoint           | Role   | Description        |
| -------- | ------------------ | ------ | ------------------ |
| `GET`    | `/api/skills`      | ANYONE | Get all skills     |
| `GET`    | `/api/skills/{id}` | ANYONE | Get one skill      |
| `POST`   | `/api/skills`      | ADMIN  | Create a new skill |
| `PUT`    | `/api/skills/{id}` | ADMIN  | Update a skill     |
| `DELETE` | `/api/skills/{id}` | ADMIN  | Delete a skill     |



# Auth API Documentation
| Method | URL                 | Body                                 | Role   | Description           |
| ------ | ------------------- | ------------------------------------ | ------ | --------------------- |
| GET    | `/auth/healthcheck` |                                      | ANYONE | Simple healthcheck    |
| POST   | `/auth/register`    | `{ "username": "", "password": "" }` | ANYONE | Register new user     |
| POST   | `/auth/login`       | `{ "username": "", "password": "" }` | ANYONE | Login and receive JWT |
| POST   | `/auth/user/role`   | `{ "role": "admin" }`                | USER   | Promote user to admin |



# Error codes
| Status | Description                          | Example                                               |
| ------ | ------------------------------------ | ----------------------------------------------------- |
| 400    | Bad Request (validation failed)      | `{ "status": 400, "message": "name is required" }`    |
| 401    | Unauthorized (missing/invalid token) | `{ "status": 401, "message": "Unauthorized" }`        |
| 404    | Not Found                            | `{ "status": 404, "message": "Candidate not found" }` |
| 500    | Server Error                         | `{ "status": 500, "message": "Database error" }`      |

# Tech used

Java 17

Maven – Dependency Management

Javalin – REST Framework

Hibernate (JPA) – ORM

PostgreSQL – Database

Lombok – Code simplification

JWT Security – Authentication and roles

FetchTools + SkillService – External API integration

DTO Layer – Input/Output separation

Populator – Seed test data
