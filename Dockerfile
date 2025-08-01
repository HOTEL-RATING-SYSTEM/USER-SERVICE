# -------- Stage 1: Build using Maven --------
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only pom.xml and download dependencies (to leverage Docker cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy rest of the source code
COPY src ./src

# Package the app (skip tests to speed up)
RUN mvn clean package -DskipTests

# -------- Stage 2: Run with JDK image --------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/UserService-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 4010

# Run the application with the Docker profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
