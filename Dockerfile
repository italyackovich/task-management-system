FROM openjdk:21-jdk-slim
LABEL authors="kstn"

WORKDIR /app

COPY target/TaskManagementSystem-0.0.1-SNAPSHOT.jar /app/TaskManagementSystem.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "TaskManagementSystem.jar"]