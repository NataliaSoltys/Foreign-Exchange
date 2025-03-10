FROM openjdk:22

WORKDIR /ratesService

COPY target/ratesService-*.jar ratesService.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ratesService.jar"]
