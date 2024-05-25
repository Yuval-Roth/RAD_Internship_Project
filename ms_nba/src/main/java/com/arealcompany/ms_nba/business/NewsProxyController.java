package com.arealcompany.ms_nba.business;

import com.arealcompany.ms_nba.utils.APIFetcher;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

@Component("newsProxyController")
public class NewsProxyController {

    private final EurekaDiscoveryClient discoveryClient;

    public NewsProxyController(EurekaDiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String getTopHeadlines(int limit) {
        ServiceInstance ms_news = discoveryClient.getInstances("ms_news").getFirst();
        return APIFetcher.create()
                .withUri(ms_news.getUri() + "/top-headlines")
                .withParam("limit", String.valueOf(limit))
                .fetch();
    }
}
