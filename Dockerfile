# --- Stage 1: Build the JAR ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# This creates the 'target' folder inside Docker
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the JAR from the build stage above
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# FORCE IPv4 to fix the Gmail Timeout
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "app.jar"]
