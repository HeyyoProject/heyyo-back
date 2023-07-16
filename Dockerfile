# Base image
FROM eclipse-temurin:17

# Set working directory
WORKDIR /app

# Copy the JAR file built from your Spring Boot application
COPY ./build/libs/heyyo-0.0.1-SNAPSHOT.jar app.jar

# Install MySQL client

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port if necessary
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
