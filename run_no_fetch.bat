start "ms_service_discovery" java -jar compiled_jars\ms_service_discovery.jar
timeout /t 10 /nobreak
start "ms_api_gateway" java -jar compiled_jars\ms_api_gateway.jar
start "ms_nba" java -jar compiled_jars\ms_nba.jar
start "ms_news" java -jar compiled_jars\ms_news.jar
start "ms_population" java -jar compiled_jars\ms_population.jar
start "client_vaadin" java -jar compiled_jars\client_vaadin.jar
