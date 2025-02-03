# Use an OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/reportingAPI-1.0.0.jar application.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/battery_inventory
# Expose the port your application runs on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "application.jar"]
