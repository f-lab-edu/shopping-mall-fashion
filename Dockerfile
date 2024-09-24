FROM openjdk:17-jdk-slim

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /var/logs

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]

FROM openjdk:17-jdk-slim

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /var/logs /var/dumps

# Entry point with required JVM options
ENTRYPOINT ["java", \
    "-Xlog:gc=info:file=/var/logs/gc-%t.log:time", \
    "-XX:HeapDumpPath=/var/dumps", \
    "-XX:OnOutOfMemoryError=kill -9 %p", \
    "-XX:+HeapDumpOnOutOfMemoryError", \
    "-Dspring.profiles.active=prod", \
    "-jar", "/app.jar"]
