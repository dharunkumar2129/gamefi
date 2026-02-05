# Use a valid, modern image
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
# This ENTRYPOINT forces IPv4, which fixes the Gmail Timeout!
ENTRYPOINT ["java","-Djava.net.preferIPv4Stack=true","-jar","/app.jar"]
