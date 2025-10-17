FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "notification-service.jar"]