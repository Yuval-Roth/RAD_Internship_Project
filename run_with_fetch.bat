start "ms_service_discovery" java -jar ms_service_discovery.jar
timeout /t 10 /nobreak
start "ms_api_gateway" java -jar ms_api_gateway.jar
start "ms_nba" java -jar ms_nba.jar -fetch
start "ms_news" java -jar ms_news.jar
start "ms_population" java -jar ms_population.jar -fetch
start "client_vaadin" java -jar client_vaadin.jar
