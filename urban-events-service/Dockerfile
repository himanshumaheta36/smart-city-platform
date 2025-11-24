# Stage 1: Build avec Maven inclus
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder /app/target/urban-events-service-1.0.0.jar app.jar

# Exposer le port
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

ENV SPRING_H2_CONSOLE_SETTINGS_WEB_ALLOW_OTHERS=true