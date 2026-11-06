# ===============================================================
# Stage 1: Build both Spring Boot applications with Maven & Java 25
# ===============================================================
FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Copy POM files for dependency caching
COPY pom.xml .
COPY assurance-api/pom.xml assurance-api/
COPY audit-consumer/pom.xml audit-consumer/

# Copy module source trees
COPY assurance-api/src assurance-api/src
COPY audit-consumer/src audit-consumer/src

# Build production JARs (tests are verified in CI/dev)
RUN mvn clean package -DskipTests

# ===============================================================
# Stage 2: assurance-api (Producer & Core Business Domain)
# ===============================================================
FROM eclipse-temurin:25-jre AS assurance-api
WORKDIR /app
COPY --from=builder /workspace/assurance-api/target/assurance-api-*.jar app.jar

ENV SERVER_PORT=8080 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/projet \
    SPRING_ARTEMIS_BROKER_URL=tcp://artemis:61616

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# ===============================================================
# Stage 3: audit-consumer (Consumer & Audit Trail)
# ===============================================================
FROM eclipse-temurin:25-jre AS audit-consumer
WORKDIR /app
COPY --from=builder /workspace/audit-consumer/target/audit-consumer-*.jar app.jar

ENV SERVER_PORT=8081 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/projet \
    SPRING_ARTEMIS_BROKER_URL=tcp://artemis:61616

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
