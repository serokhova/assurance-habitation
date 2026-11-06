# Multi-stage Dockerfile for assurance-api
FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Copy POM files
COPY pom.xml .
COPY assurance-api/pom.xml assurance-api/
COPY audit-consumer/pom.xml audit-consumer/

# Copy sources
COPY assurance-api/src assurance-api/src
COPY audit-consumer/src audit-consumer/src

# Build assurance-api module
RUN mvn clean package -DskipTests -pl assurance-api -am

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /workspace/assurance-api/target/assurance-api-*.jar app.jar

ENV SERVER_PORT=8080 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/projet \
    SPRING_ARTEMIS_BROKER_URL=tcp://artemis:61616

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
