# Stage 1: Build
FROM maven:3.8.4-openjdk-11-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

# Stage 2: Run
FROM openjdk:11-jre-slim
COPY --from=build /app/target/ContactMigrator-1.0-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/contacts.csv /app/contacts.csv
# Set working directory so relative paths in Java work
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]