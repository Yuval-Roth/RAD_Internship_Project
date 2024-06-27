FROM openjdk:22

COPY target/ms_api_gateway-0.0.1-SNAPSHOT.jar /~/ms_api_gateway.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/~/ms_api_gateway.jar"]