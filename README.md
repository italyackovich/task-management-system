# Task Management System

This project is a Spring Boot-based Task Management System that supports user authentication, task management, commenting, and more. The application is containerized using Docker and is configured for local development using environment variables defined in a `.env` file.

## Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

## Setup

### 1. Clone the Repository

Open your terminal and run:

```shell
git clone https://github.com/yourusername/task-management-system.git
cd task-management-system
```


### 2. Create the .env File

In the root directory of the project, create a file named .env and add the following content (modify the values as needed):

```dotenv
# Database
DATASOURCE_URL=your_datasource_url
POSTGRES_USER=your_db_username
POSTGRES_PASSWORD=your_db_password
POSTGRES_DB=task_management_system_db

# JWT and Security
JWT_SECRET=your_jwt_secret
JWT_ACCESS_EXPIRATION=your_access_expiration
JWT_REFRESH_EXPIRATION=your_refresh_expiration

# Redis
REDIS_HOST=your_host
REDIS_PORT=your_port

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
```

### 3. Build the Project

You can build the project using Maven. If you have Maven installed globally, run:

```shell
mvn clean package -Dmaven.test.skip=true 
```
Alternatively, you can use the Maven Wrapper:

```shell
./mvnw clean package -Dmaven.test.skip=true 
```

The built JAR file will be located in the target directory (e.g., `TaskManagementSystem-0.0.1-SNAPSHOT.jar`).

## Running the Project with Docker

The project uses Docker Compose to orchestrate the application, PostgreSQL, and Redis containers.

### 1. Ensure Docker is Running

Make sure Docker and Docker Compose are installed and running on your machine.

### 2. Start the Containers

Run the following command to build and start all containers:

```shell
docker-compose -f docker-compose.dev.yml up --build
```

This command will:

- Build the Docker image for the application.

- Start containers for the application, PostgreSQL, and Redis.

- Mount volumes (e.g., for persisting database data or for hot reloading in development).

### 3. Access the Application

- API Endpoints:
  The application is available on port `8080`via:
```shell
http://localhost:8080/api/v1/...
```
- Swagger UI:
  View and interact with the API documentation at:
```shell
http://localhost:8080/swagger-ui/index.html
```

## Development Notes
- *Hot Reload*:
  Spring DevTools is enabled for local development. Ensure your IDE is configured to compile on save (e.g., in IntelliJ IDEA, enable "Build project automatically" and "Allow auto-make to start even if developed application is currently running").
- *Environment Variables*:
  The application reads configuration (database credentials, JWT secrets, etc.) from the `.env` file. Ensure the values are correctly set before starting the containers.
- *Docker Volumes*:
  The Docker Compose file mounts a volume for PostgreSQL data so that your database persists between container restarts.
