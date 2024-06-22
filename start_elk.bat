start "Elastic Search" elk\elasticsearch\bin\elasticsearch.bat 
start "Kibana" elk\kibana\bin\kibana.bat
start "Logstash" elk\logstash\bin\logstash.bat -f config/logstash.conf