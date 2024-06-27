FROM openjdk:22

COPY target/client_vaadin-0.0.1-SNAPSHOT.jar /~/client_vaadin.jar
EXPOSE 80

ENTRYPOINT ["java", "-jar", "/~/client_vaadin.jar"]