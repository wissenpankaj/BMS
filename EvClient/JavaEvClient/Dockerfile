# Use an OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/*-1.0.0-exec.jar application.jar


# Command to run the application
ENTRYPOINT ["java", "-jar", "application.jar"]
