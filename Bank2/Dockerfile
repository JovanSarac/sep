# 1. Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the app
RUN mvn clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy SSL keystore and truststore
COPY src/main/resources/bank2.p12 ./bank2.p12
COPY src/main/resources/truststore.jks ./truststore.jks

# Expose port
EXPOSE 8092

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
