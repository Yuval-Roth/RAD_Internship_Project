FROM openjdk:22

COPY target/ms_service_discovery-0.0.1-SNAPSHOT.jar /~/ms_service_discovery.jar
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/~/ms_service_discovery.jar"]