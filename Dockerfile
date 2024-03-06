# Use a official base image
FROM openjdk:17

# Copy the jar file into the image
COPY target/techchallenge2-0.0.1-SNAPSHOT.jar techchallenge2-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/techchallenge2-0.0.1-SNAPSHOT.jar"]