===============================================================================
       GAMIFIED TRADING PLATFORM — DOCKER & APPLICATION LIFECYCLE COMMANDS
===============================================================================

-------------------------------------------------------------------------------
1. STOPPING EVERYTHING (BEFORE LEAVING)
-------------------------------------------------------------------------------
# Step A: Stop the Spring Boot App
# Press Ctrl + C in the terminal window running the app.

# Step B: Stop the PostgreSQL Docker container
docker stop trove_postgres

-------------------------------------------------------------------------------
2. STARTING EVERYTHING (WHEN YOU RETURN)
-------------------------------------------------------------------------------
# Step A: Start the PostgreSQL Docker container
docker start trove_postgres

# Note: If the container was deleted/removed, recreate it with:
# docker run --name trove_postgres -e POSTGRES_DB=investment_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16

# Step B: Navigate to project root
cd ~/IdeaProjects/investment-platform-By-Trove

# Step C: Run the Spring Boot application
mvn spring-boot:run

-------------------------------------------------------------------------------
3. USEFUL VERIFICATION LINKS ONCE RUNNING
-------------------------------------------------------------------------------
- API Base URL:   http://localhost:8080
- Swagger UI:     http://localhost:8080/swagger-ui/index.html
- Health Check:   http://localhost:8080/actuator/health
===============================================================================



===============TO DROP THE DB ON DOCKER======
docker stop trove_postgres && docker rm trove_postgres && docker run --name trove_postgres -e POSTGRES_DB=investment_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16

====TO BUILD UP THE SPRING SERVER AND DATABASE WITH DOCKER when code changes====
docker compose up --build

===start the docker server when no code change====
docker compose up or docker compose up -d

to stop server========
docker compose logs -f