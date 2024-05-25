package com.arealcompany.ms_news.business;

import com.arealcompany.ms_news.business.dtos.GNewsResponse;
import com.arealcompany.ms_news.utils.APIFetcher;
import com.arealcompany.ms_news.utils.EnvUtils;
import com.arealcompany.ms_news.utils.JsonUtils;
import com.arealcompany.ms_news.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
        GNewsResponse obj = JsonUtils.deserialize(response, GNewsResponse.class);
        StringBuilder sb = new StringBuilder();
        obj.articles().forEach(article -> sb.append(JsonUtils.serialize(article)).append("\n"));
        sb.deleteCharAt(sb.length()-1);

        log.debug("Fetched successfully");
        return sb.toString();
    }



    @SafeVarargs
    private String fetch(String location, Pair<String,String>... params) {
        var fetcher = APIFetcher.create()
                .withUri("https://gnews.io/api/v4/"+location);
        Arrays.stream(params).forEach(pair -> fetcher.withParam(pair.first(), pair.second()));
        return fetcher.fetch();
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        log.debug("News REST API is ready on port "+ PORT);
    }
}
