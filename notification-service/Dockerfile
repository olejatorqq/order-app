FROM openjdk:21-slim

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar

ENTRYPOINT ["java","-jar","notification-service.jar"]