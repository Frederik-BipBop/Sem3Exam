# README

## Setup

1. Fork project
2. Create a database
3. Update `config.properties` variable `DB_NAME` to match your database name
4. Open Maven tab (right-side)
5. Navigate to Lifecycle -> verify
6. Right-click and 'Run Maven Build'

## API Documentation

### Example
| Method | URL | Request Body (JSON) | Response (JSON) | Error (e) |
| --- | --- | --- | --- | --- |
| GET | `/api/users` | | [user, user, …] (1) | |
| GET | `/api/users/{id}` | | user (1) | (e1) |
| POST | `/api/users`	| user(1) without id | | (e2) |
| UPDATE | `/api/users/{id}` | user(1) without id | user (1) | |

<details>
<summary>User (1)</summary>

> Do not provide ID for POST

```json
{
  "id": Integer,
  "username": String,
  "email": String (email)
}
```

</details>

<details>
<summary>Error (e1)</summary>

```json
{ status : 404, "msg": "No content found for this request" }
```

</details>

<details>
<summary>Error (e2)</summary>

```json
{ status : 400, "msg": "Field ‘xxx’ is required" } (for example, no name provided)
```

</details>

---

### Auth

| Method | URL                     | Request Body (JSON) | Response (JSON) | Error (e)  |
|--------|-------------------------|---------------------|-----------------|------------|
| GET    | `/api/auth/healthcheck` |                     | message (1)     |            |
| GET    | `/api/auth/test`        |                     | message (1)     |            |
| POST   | `/api/auth/register`    | user (2)            | token (3)       | (e1)       |
| POST   | `/api/auth/login`	      | user (2)            | token (3)       | (e2), (e3) |
| POST   | `/api/auth/user/role`   | role (4)            | message (1)     | (e4        |


<details>
<summary>Message (1)</summary>

```json
{
  "msg": String
}
```

</details>

<details>
<summary>User (2)</summary>

```json
{
  "username": String,
  "password": String
}
```

</details>

<details>
<summary>Token (3)</summary>

```json
{
  "token": String,
  "username": String
}
```

</details>

</details>

<details>
<summary>Role (4)</summary>

```json
{
  "role": String
}
```

</details>

<details>
<summary>Error (1)</summary>

```json
{
  "warning": "User with username: {username} already exist",
  "status": "400 Bad Request"
}
```

</details>

<details>
<summary>Error (2)</summary>

```json
{
  "msg": "No user found with username: {username}"
}
```

</details>

<details>
<summary>Error (3)</summary>

```json
{
  "msg": "Wrong password"
}
```

</details>

<details>
<summary>Error (4)</summary>

```json
{
  "msg": "No user found with username: {username}"
}
```

</details>

---