docker start ms_service_discovery
timeout /t 10 /nobreak
docker start client_vaadin
docker start ms_news
docker start ms_population
docker start ms_nba
docker start ms_api_gateway

