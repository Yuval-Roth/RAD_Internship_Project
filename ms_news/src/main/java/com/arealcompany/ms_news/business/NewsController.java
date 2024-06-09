package com.arealcompany.ms_news.business;

import com.arealcompany.ms_common.utils.*;
import com.arealcompany.ms_news.business.dtos.GNewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component("newsController")
public class NewsController {

    private static final String ENV_KEY_NAME = "GNEWS_KEY";
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    private static final String PORT = "8083";
    private final String apiKey;

    public NewsController() {
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
    }

    public String fetchTopHeadlines(int limit){
        log.debug("Fetching top headlines");

        String response = fetch(
                "top-headlines",
                Pair.of("category","general"),
                Pair.of("lang","en"),
                Pair.of("country","israel"),
                Pair.of("max",String.valueOf(limit)),
                Pair.of("apikey",apiKey)
        );
        log.debug("Fetched successfully");
        GNewsResponse obj = JsonUtils.deserialize(response, GNewsResponse.class);
        return Response.get(obj.articles());
    }

    @SafeVarargs
    private String fetch(String location, Pair<String,String>... params) {
        var fetcher = APIFetcher.create()
                .withUri("https://gnews.io/api/v4/"+location);
        Arrays.stream(params).forEach(pair -> fetcher.withParam(pair.first(), pair.second()));
        try {
            return fetcher.fetch();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to fetch data from GNews API", e);
            return Response.get("Failed to fetch data from GNews API");
        }
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        log.debug("News REST API is ready on port "+ PORT);
    }
}
