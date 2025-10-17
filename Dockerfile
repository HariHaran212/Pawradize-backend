# --- Stage 1: Build the application ---
# Use a Maven image with Java 17 to build the project
FROM maven:3.9-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file first to leverage Docker's layer caching
COPY pom.xml .

# Copy the rest of your source code
COPY src ./src

# Build the project and create the executable JAR file
# -DskipTests skips running tests during the build
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, lightweight image ---
# Use a slim Java 17 runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy only the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 2025

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]