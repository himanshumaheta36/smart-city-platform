# Utiliser une image Maven avec protoc préinstallé
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .

# Installer protoc pour Linux
RUN apt-get update && apt-get install -y protobuf-compiler

# Compiler avec la classification Linux explicitement
RUN mvn clean package -DskipTests -Dos.detected.classifier=linux-x86_64

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/emergency-service-1.0.0.jar app.jar

# Installer les outils réseau pour le debug
RUN apk add --no-cache curl

EXPOSE 8083 9090

ENTRYPOINT ["java", "-jar", "app.jar"]