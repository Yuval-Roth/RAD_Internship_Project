package com.arealcompany.ms_nba.business;

import com.arealcompany.ms_common.utils.APIFetcher;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Component("newsProxyController")
public class NewsProxyController {

    private final EurekaDiscoveryClient discoveryClient;

    public NewsProxyController(EurekaDiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String getTopHeadlines(Map<String,String> params) {

        ServiceInstance ms_news;
        try{
             ms_news = discoveryClient.getInstances("ms_news").getFirst();
        } catch(NoSuchElementException ignored) {
            return "Service not found";
        }

        var fetcher = APIFetcher.create()
                .withUri(ms_news.getUri() + "/top-headlines")
                .withParams(params);
        try {
            return fetcher.fetch();
        } catch (IOException | InterruptedException e) {
            return "Failed to reach service";
        }
    }
}
