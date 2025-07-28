FROM openjdk:17
VOLUME /tmp
COPY target/library-ci-cd-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
