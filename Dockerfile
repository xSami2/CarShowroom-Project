# Build stage
FROM maven:3.9-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (this layer gets cached!)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code (changes often, but deps already cached)
COPY src ./src

# Build application (fast because deps already downloaded)
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM openjdk:17-jre-slim
WORKDIR /app

# Copy JAR file
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 9091

# Run with basic optimizations
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]