FROM openjdk:17-jdk-slim

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /var/logs

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]