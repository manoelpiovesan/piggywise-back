FROM quay.io/quarkus/quarkus-distroless:2.11.0.Final-java17 as builder

COPY build/libs/*-runner.jar /app/application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
