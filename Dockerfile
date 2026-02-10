FROM eclipse-temurin:18-jdk-jammy

WORKDIR /app

COPY target/beauty-booking-platform-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
