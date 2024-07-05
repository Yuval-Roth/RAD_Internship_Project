package com.arealcompany.ms_nba.business;

import com.arealcompany.ms_common.utils.APIFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Component("newsProxyController")
public class NewsProxyController {

    @Value("${ms_news_uri}")
    String ms_news_uri;

    public String getTopHeadlines(Map<String,String> params) {

        var fetcher = APIFetcher.create()
                .withUri(ms_news_uri + "/get/top-headlines")
                .withParams(params);
        try {
            return fetcher.fetch();
        } catch (IOException | InterruptedException e) {
            return "Failed to reach service";
        }
    }
}
