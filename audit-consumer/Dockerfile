# Multi-stage Dockerfile for audit-consumer
FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Copy POM files
COPY pom.xml .
COPY assurance-api/pom.xml assurance-api/
COPY audit-consumer/pom.xml audit-consumer/

# Copy sources
COPY assurance-api/src assurance-api/src
COPY audit-consumer/src audit-consumer/src

# Build audit-consumer module
RUN mvn clean package -DskipTests -pl audit-consumer -am

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /workspace/audit-consumer/target/audit-consumer-*.jar app.jar

ENV SERVER_PORT=8081 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/projet \
    SPRING_ARTEMIS_BROKER_URL=tcp://artemis:61616

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
