# ----------------------------
# Stage 1: Build the Spring Boot JAR
# ----------------------------
FROM gradle:9.3-jdk21-corretto AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files first (for caching)
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (cached if unchanged)
RUN ./gradlew build --no-daemon --dry-run || true

# Copy the rest of the source code
COPY src src

# Build the executable jar
RUN ./gradlew clean bootJar --no-daemon

# ----------------------------
# Stage 2: Runtime Image
# ----------------------------
FROM eclipse-temurin:21-jdk AS runtime

# Create app directory
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]