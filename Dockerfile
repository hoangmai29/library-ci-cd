FROM openjdk:17-jdk-slim
COPY target/library-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
