FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/orchestration-service-1.0.0.jar app.jar

# Configuration Gemini
ENV GEMINI_API_KEY=""
ENV GEMINI_MODEL="gemini-2.5-flash-lite"
ENV GEMINI_ENABLED="true"

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]