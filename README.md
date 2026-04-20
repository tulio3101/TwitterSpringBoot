# Twitter-like Application with Microservices and Auth0

A secure Twitter-like web application that allows authenticated users to create and view short posts (maximum 140 characters) in a single public global stream. The project begins as a Spring Boot monolith with full Swagger/OpenAPI documentation and evolves into independent microservices (Posts, Feed, and User Authentication) communicating asynchronously via RabbitMQ events. The entire API is secured using Auth0 JWT tokens, and the frontend is built with React using the Auth0 React SDK.

## Architecture Overview

### Evolution: Monolith → Microservices

**Phase 1 – Monolith**


![alt text](docs/images/MonolithInitialzr.png)


![alt text](docs/images/Monolith.png)


**Phase 2 – Microservices**


![alt text](docs/images/Microservice.png)

### Security Flow

```
User → Auth0 Login → JWT Access Token
JWT Token → Spring Boot OAuth2 Resource Server
Resource Server → Validates issuer (https://YOUR_DOMAIN.auth0.com/)
                → Validates audience (https://twitter-api)
                → Grants/denies access
```

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- **Java 21** – [Download](https://adoptium.net/)
- **Maven 3.8+** – [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+ and npm** – [Download](https://nodejs.org/)
- **MongoDB** – Local installation or [MongoDB Atlas](https://www.mongodb.com/atlas) (free tier)
- **RabbitMQ** – [CloudAMQP](https://www.cloudamqp.com/) free tier (Little Lemur plan)
- **Auth0 account** – [Sign up free](https://auth0.com/)
- **Git** – [Download](https://git-scm.com/)

```bash
# Verify Java
java -version   # should show 21.x

# Verify Maven
mvn -version

# Verify Node
node -version   # should show 18.x or higher
```

### Auth0 Configuration

Before running the application, set up Auth0:

1. **Create a Single Page Application (SPA)** in your Auth0 dashboard.

   ![Auth0 Dashboard](docs/images/DashboardAuth0.png)

   - Note the **Domain** and **Client ID**.
   - Set **Allowed Callback URLs**: `http://localhost:3000`
   - Set **Allowed Logout URLs**: `http://localhost:3000`
   - Set **Allowed Web Origins**: `http://localhost:3000`

   ![Application Settings](docs/images/ApplicationAuth0.png)

   ![Callback URL Configuration](docs/images/URLCallback.png)

2. **Create an API** in Auth0.
   - Set the **Identifier (Audience)** to: `https://twitter-api`
   - Enable **RBAC** and add the following permissions/scopes: `read:posts`, `write:posts`, `read:profile`

### Installing

**1. Clone the repository**

```bash
git clone https://github.com/tulio3101/TwitterSpringBoot.git
cd TwitterSpringBoot
```

**2. Set up RabbitMQ via CloudAMQP**

This project uses [CloudAMQP](https://www.cloudamqp.com/) (free tier) as its AMQP broker — no local installation required.

1. Create a free account at [cloudamqp.com](https://www.cloudamqp.com/)
2. Create a new instance — select the **Little Lemur** plan (free)
3. Open the instance details and copy the **AMQP URL** (format: `amqps://user:password@host/vhost`)
4. Use the individual parts of that URL to populate the `RABBITMQ_*` environment variables in the next step

**3. Configure environment variables**

Each Spring Boot service reads configuration from environment variables. Set the following before running each service:

| Variable | Used By | Description |
|---|---|---|
| `MONGODB_URI` | All services | MongoDB connection string |
| `AUTH0_ISSUER_URI` | Twitter (Gateway) | `https://YOUR_DOMAIN.auth0.com/` |
| `CORS_ALLOWED_ORIGIN` | Twitter (Gateway) | Frontend origin, e.g. `http://localhost:3000` |
| `SERVER_PORT` | Twitter (Gateway) | Service port (default: `4040`) |
| `RABBITMQ_HOST` | PostsService, FeedService | CloudAMQP hostname (e.g. `your-instance.cloudamqp.com`) |
| `RABBITMQ_PORT` | PostsService, FeedService | AMQP port (`5672` for AMQP, `5671` for AMQPS) |
| `RABBITMQ_USERNAME` | PostsService, FeedService | CloudAMQP username |
| `RABBITMQ_PASSWORD` | PostsService, FeedService | CloudAMQP password |
| `RABBITMQ_VIRTUAL_HOST` | PostsService, FeedService | CloudAMQP virtual host (same as your username by default) |

Example (Linux/macOS):

```bash
export MONGODB_URI="mongodb+srv://user:password@cluster.mongodb.net/twitter"
export AUTH0_ISSUER_URI="https://YOUR_DOMAIN.auth0.com/"
export CORS_ALLOWED_ORIGIN="http://localhost:3000"
export RABBITMQ_HOST="your-instance.cloudamqp.com"
export RABBITMQ_PORT="5672"
export RABBITMQ_USERNAME="your-cloudamqp-user"
export RABBITMQ_PASSWORD="your-cloudamqp-password"
export RABBITMQ_VIRTUAL_HOST="your-cloudamqp-vhost"
```

**4. Build and run the Spring Boot services**

Run each service in a separate terminal:

```bash
# Terminal 1 – Twitter API Gateway (Monolith / Gateway)
cd Twitter
mvn spring-boot:run
# Runs on port 4040
```

```bash
# Terminal 2 – PostsService (Microservice)
cd PostsService
mvn spring-boot:run
# Runs on port 8081
```

```bash
# Terminal 3 – FeedService (Microservice)
cd FeedService
mvn spring-boot:run
# Runs on port 8082
```

```bash
# Terminal 4 – UserAuthentication (Microservice)
cd UserAuthentication
mvn spring-boot:run
# Runs on port 8082
```

**5. Configure and run the frontend**

Create a `.env` file inside the `twitter-frontend` directory:

```env
REACT_APP_AUTH0_DOMAIN=YOUR_DOMAIN.auth0.com
REACT_APP_AUTH0_CLIENT_ID=YOUR_CLIENT_ID
REACT_APP_API_BASE_URL=http://localhost:4040
```

Then install dependencies and start the dev server:

```bash
cd twitter-frontend
npm install
npm start
```

The frontend will be available at `http://localhost:3000`.

**6. Quick demo – create your first post**

After logging in via the frontend, you can also test the API directly. Obtain a JWT token from your Auth0 tenant and run:

```bash
curl -X POST http://localhost:4040/api/posts/create \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello from TwitterSpringBoot!"}'
```

Then view the public stream (no authentication needed):

```bash
curl http://localhost:4040/api/stream
```

## API Documentation

The monolith exposes a full **Swagger UI** powered by Springdoc OpenAPI 2.6.0:

- **Swagger UI**: `http://localhost:4040/swagger-ui.html`
- **OpenAPI JSON spec**: `http://localhost:4040/v3/api-docs`

### Endpoint Reference

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|---|
| `GET` | `/api/posts` | No | Retrieve all posts |
| `POST` | `/api/posts/create` | Yes | Create a new post (max 140 chars) |
| `PUT` | `/api/posts/update/{postId}` | Yes | Update an existing post |
| `DELETE` | `/api/posts/{postId}` | Yes | Delete a post |
| `GET` | `/api/stream` | No | Get the global public feed |
| `DELETE` | `/api/stream/{postId}` | Yes | Remove a post from the stream |
| `POST` | `/api/users/me` | Yes | Register the currently authenticated user |
| `GET` | `/api/users/me` | Yes | Get the current user's profile |
| `GET` | `/api/users/{id}` | No | Get a user profile by ID |

Protected endpoints require a valid **JWT Bearer token** issued by Auth0 with audience `https://twitter-api`.

## Running the Tests

Explain how to run the automated tests for this system.

### Backend context tests

Each microservice includes a Spring Boot application context test. Run from each service directory:

```bash
# Run from each service root directory (Twitter, PostsService, FeedService, UserAuthentication)
mvn test
```

These tests verify that the Spring Boot application context loads correctly with all beans, security configuration, and database connections.

### End-to-end tests

**Via Swagger UI:**

1. Open `http://localhost:4040/swagger-ui.html`
2. Click **Authorize** and paste your JWT token (obtained from Auth0 or the frontend after login)
3. Test each protected endpoint directly from the browser

For example, to test post creation:
- Expand `POST /api/posts/create`
- Click **Try it out**
- Enter `{"message": "Hello from Swagger!"}` as the request body
- Execute and verify a `201 Created` response

**Via curl:**

```bash
# Public endpoint – no token required
curl -X GET http://localhost:4040/api/posts

# Public endpoint – get global stream
curl -X GET http://localhost:4040/api/stream

# Protected endpoint – create a post
curl -X POST http://localhost:4040/api/posts/create \
  -H "Authorization: Bearer <JWT>" \
  -H "Content-Type: application/json" \
  -d '{"message": "Test post from curl"}'

# Protected endpoint – get current user profile (/api/me equivalent)
curl -X GET http://localhost:4040/api/users/me \
  -H "Authorization: Bearer <JWT>"
```

### Auth flow test

1. Open the frontend at `http://localhost:3000`
2. Click **Login** — you are redirected to Auth0
3. Authenticate with your credentials
4. You are redirected back with a valid JWT stored in localStorage
5. Create a post using the form — it should appear in the public feed
6. Log out and verify the feed is still publicly readable without authentication

## Deployment

### Frontend on Amazon S3 



### Microservices on AWS Lambda 



## Built With

- [Spring Boot 3.5.13](https://spring.io/projects/spring-boot) — Java backend framework
- [Maven](https://maven.apache.org/) — Dependency management and build tool
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) — MongoDB persistence
- [Spring AMQP / RabbitMQ](https://spring.io/projects/spring-amqp) — Asynchronous event messaging between microservices
- [Auth0](https://auth0.com/) — Identity and authorization provider
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html) — JWT validation
- [Springdoc OpenAPI 2.6.0](https://springdoc.org/) — Swagger / OpenAPI documentation
- [React 19](https://react.dev/) — Frontend framework
- [Auth0 React SDK](https://auth0.com/docs/libraries/auth0-react) — Frontend authentication and token management
- [Tailwind CSS 4](https://tailwindcss.com/) — Utility-first CSS styling
- [MapStruct 1.5.5](https://mapstruct.org/) — DTO / entity mapping
- [Lombok](https://projectlombok.org/) — Java boilerplate reduction



## Authors

- **Tulio Riaño Sánchez** — [tulio3101](https://github.com/tulio3101)
- **Juan Sebastián Puentes Julio**

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Auth0 Documentation](https://auth0.com/docs) for the OAuth2/JWT integration guides
- [Spring Security OAuth2 documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html) for resource server configuration
- [Springdoc OpenAPI](https://springdoc.org/) for Swagger integration with Spring Boot 3
