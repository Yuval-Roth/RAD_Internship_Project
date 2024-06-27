docker stop client_vaadin
docker rm client_vaadin
docker build -f client_vaadin/Dockerfile -t client_vaadin client_vaadin/.
docker create -p 80:80 --name client_vaadin client_vaadin

docker stop ms_news
docker rm ms_news
docker build -f ms_news/Dockerfile -t ms_news ms_news/.
docker create -p 8083:8083 -e GNEWS_KEY=%GNEWS_KEY% --name ms_news ms_news

docker stop ms_population
docker rm ms_population
docker build -f ms_population/Dockerfile -t ms_population ms_population/.
docker create -p 8082:8082 -e RAPIDAPI_KEY=%RAPIDAPI_KEY% --name ms_population ms_population

docker stop ms_nba
docker rm ms_nba
docker build -f ms_nba/Dockerfile -t ms_nba ms_nba/.
docker create -p 8081:8081 -e RAPIDAPI_KEY=%RAPIDAPI_KEY% --name ms_nba ms_nba

docker stop ms_service_discovery
docker rm ms_service_discovery
docker build -f ms_service_discovery/Dockerfile -t ms_service_discovery ms_service_discovery/.
docker create -p 8761:8761 --name ms_service_discovery ms_service_discovery

docker stop ms_api_gateway
docker rm ms_api_gateway
docker build -f ms_api_gateway/Dockerfile -t ms_api_gateway ms_api_gateway/.
docker create -p 8080:8080 --name ms_api_gateway ms_api_gateway
