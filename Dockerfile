# Base image
FROM adoptopenjdk:17-jdk-hotspot

# Set working directory
WORKDIR /app

# Copy the JAR file built from your Spring Boot application
COPY ./build/libs/heyyo-0.0.1-SNAPSHOT.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/heyyo
ENV SPRING_DATASOURCE_USERNAME=heyyo
ENV SPRING_DATASOURCE_PASSWORD=password

# Expose port if necessary
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
