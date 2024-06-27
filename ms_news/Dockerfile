FROM openjdk:22

COPY target/ms_news-0.0.1-SNAPSHOT.jar /~/ms_news.jar
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/~/ms_news.jar"]