package com.arealcompany.ms_api_gateway.business;

import com.arealcompany.ms_api_gateway.utils.APIFetcher;
import com.arealcompany.ms_api_gateway.utils.Pair;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

@Component("GatewayController")
public class GatewayController {

    private static final String SERVICE_NOT_FOUND = "Service not found";
    private final EurekaDiscoveryClient discoveryClient;

    public GatewayController(EurekaDiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String forwardRequest(String service, String endpoint, Map<String,String> params) {

        //TODO: find a better way to do this
        String actualService = switch(service){
            case "nba", "news" -> "ms_nba";
            case "population" -> "ms_population";
            default -> null;
        };

        if(actualService == null){
            return SERVICE_NOT_FOUND;
        }

        ServiceInstance serviceInstance;
        try{
            serviceInstance = discoveryClient.getInstances(actualService).getFirst();
        } catch (NoSuchElementException ignored){
            return SERVICE_NOT_FOUND;
        }

        APIFetcher fetcher = APIFetcher.create()
                .withUri(serviceInstance.getUri()+"/"+ endpoint);
        params.forEach(fetcher::withParam);

        return fetcher.fetch();
    }
}
