# Use OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar (replace with your real jar name or use target/*.jar)
COPY target/*.jar app.jar

# Expose port (adjust if different)
EXPOSE 9020

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
