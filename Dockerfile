FROM openjdk:17-jdk-slim

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /var/logs /var/dumps

ENTRYPOINT ["java", \
    "-Xlog:gc=info:file=/var/logs/gc-%t.log:time", \
    "-XX:+HeapDumpOnOutOfMemoryError", \
    "-XX:HeapDumpPath=/var/dumps", \
    "-XX:OnOutOfMemoryError=kill -9 %p", \
    "-Dspring.profiles.active=prod", \
    "-jar", "/app.jar"]
