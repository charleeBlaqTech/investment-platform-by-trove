## Running the Application

### Prerequisites

Before running the application, ensure you have the following installed:

- Java 21
- Maven 3.9+
- Docker Desktop (optional, for running with Docker)

---

## Running with Docker

Clone the repository:

```bash
git clone <repository-url>
cd investment-platform-By-Trove
```

Build and start the application:

```bash
docker compose up --build
```

The application will be available at:

```
http://localhost:8080
```

```bash
mvn test
```

To stop the application:

```bash
docker compose down
```

---

## Running Locally

Clone the repository:

```bash
git clone <repository-url>
cd investment-platform-By-Trove
```

Build the project:

```bash
mvn clean install
```

run build to confirm successful build
```bash
mvn clean package -DskipTests
```

Run the application:

```bash
mvn spring-boot:run
```


run tests
```bash
mvn test
```

Alternatively, if using IntelliJ IDEA:

1. Open the project.
2. Wait for Maven dependencies to download.
3. Run the `Main` class.

The application will start on:

```
http://localhost:8080
```

---

## Data Storage

This project currently uses an **in-memory repository** for persistence.

- No PostgreSQL installation is required.
- No database configuration is needed.
- All data is stored in memory while the application is running.
- Restarting the application clears all stored data since I used In memory.

Database persistence (PostgreSQL) will be introduced in a future version of the project.