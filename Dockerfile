# Use the official OpenJDK image as a parent image
FROM openjdk:23-ea-17-jdk-bullseye

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/PruebaTecnica-0.0.1-SNAPSHOT.jar /app

# Run the JAR file when the container launches
CMD ["java", "-jar", "PruebaTecnica-0.0.1-SNAPSHOT.jar"]

