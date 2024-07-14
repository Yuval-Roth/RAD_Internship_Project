package com.arealcompany.ms_api_gateway.business;

import com.arealcompany.ms_common.utils.APIFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component("GatewayController")
public class GatewayController {

    private static final String SERVICE_NOT_FOUND = "Service not found";
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    @Value("${ms.nba.uri}")
    private String MS_NBA_URI;
    @Value("${ms.news.uri}")
    private String MS_NEWS_URI;
    @Value("${ms.population.uri}")
    private String MS_POPULATION_URI;

    public String forwardRequest(String service, String action, String endpoint, Map<String,String> params, String body) {

        log.debug("Forwarding request to service: {} endpoint: {} params: {}", service, endpoint, params);

        String actualService = switch(service){
            case "nba" -> MS_NBA_URI;
            case "news" -> MS_NEWS_URI;
            case "population" -> MS_POPULATION_URI;
            default -> null;
        };

        if(actualService == null){
            log.trace("actualService is null");
            return SERVICE_NOT_FOUND;
        }

        log.trace("Actual service: {}", actualService);

        APIFetcher fetcher = APIFetcher.create()
                .withUri("%s/%s/%s".formatted(actualService,action,endpoint))
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

    public String forwardRequest(String service, String action, String endpoint, Map<String, String> params) {
        return forwardRequest(service, action, endpoint, params, "");
    }
}
