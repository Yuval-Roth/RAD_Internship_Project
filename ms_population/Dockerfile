FROM openjdk:22

COPY target/ms_population-0.0.1-SNAPSHOT.jar /~/ms_population.jar
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/~/ms_population.jar"]