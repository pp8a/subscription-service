FROM openjdk:17-jdk-slim

RUN apt update && apt install -y curl

WORKDIR /app

COPY target/subscription-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
