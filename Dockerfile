# RK Traders backend — production image for Render
# Java 21.0.11 (Temurin build 10) pinned exactly, via eclipse-temurin.

# ---- Build stage -------------------------------------------------------
FROM eclipse-temurin:21.0.11_10-jdk-alpine AS build
WORKDIR /workspace

# Cache the Maven wrapper distribution and project dependencies first.
COPY .mvn/wrapper .mvn/wrapper
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q -DskipTests dependency:go-offline || true

# Build the Spring Boot fat JAR.
COPY src src
RUN chmod +x mvnw && ./mvnw -B -DskipTests package

# ---- Runtime stage -----------------------------------------------------
FROM eclipse-temurin:21.0.11_10-jre-alpine AS runtime
WORKDIR /app

# Render exposes the HTTP port as $PORT. Spring Boot reads server.port; the
# entrypoint maps $PORT to it, defaulting to 8080 for plain `docker run`.
COPY --from=build /workspace/target/web-0.0.1-SNAPSHOT.jar app.jar

# No credentials or secrets are baked into the image; DB_URL, DB_USERNAME,
# DB_PASSWORD, JWT_SECRET, OWNER_*, CORS_ALLOWED_ORIGINS, etc. are supplied
# by Render's environment and read from the existing ${VAR} placeholders in
# application.properties / application-postgres.properties.
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]
