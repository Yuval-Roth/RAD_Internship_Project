package com.arealcompany.ms_api_gateway.business;

import com.arealcompany.ms_common.utils.APIFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Component("GatewayController")
public class GatewayController {

    private static final String SERVICE_NOT_FOUND = "Service not found";
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);
    private final EurekaDiscoveryClient discoveryClient;

    @Value("${ms.nba.hostname}")
    private String MS_NBA_HOSTNAME;
    @Value("${ms.news.hostname}")
    private String MS_NEWS_HOSTNAME;
    @Value("${ms.population.hostname}")
    private String MS_POPULATION_HOSTNAME;

    public GatewayController(EurekaDiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String forwardRequest(String service, String action, String endpoint, Map<String,String> params, String body) {

        log.debug("Forwarding request to service: {} endpoint: {} params: {}", service, endpoint, params);

        String actualService = switch(service){
            case "nba" -> MS_NBA_HOSTNAME;
            case "news" -> MS_NEWS_HOSTNAME;
            case "population" -> MS_POPULATION_HOSTNAME;
            default -> null;
        };

        if(actualService == null){
            log.trace("actualService is null");
            return SERVICE_NOT_FOUND;
        }

        log.trace("Actual service: {}", actualService);

        ServiceInstance serviceInstance;
        try{
            serviceInstance = discoveryClient.getInstances(actualService).getFirst();
        } catch (NoSuchElementException ignored){
            return SERVICE_NOT_FOUND;
        }

        log.trace("Forwarding to host: {}", serviceInstance.getHost());

        APIFetcher fetcher = APIFetcher.create()
                .withUri("%s/%s/%s".formatted(serviceInstance.getUri(),action,endpoint))
                .withParams(params);

        if(!body.isEmpty()){
            fetcher.withBody(body);
            fetcher.withPost();
        }

        try {
            return fetcher.fetch();
        } catch (IOException | InterruptedException e) {
            return "Failed to reach service";
        }
    }

    public String forwardRequest(String service, String update, String endpoint, Map<String, String> params) {
        return forwardRequest(service, update, endpoint, params, "");
    }
}
