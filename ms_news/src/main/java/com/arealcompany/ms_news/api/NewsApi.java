package com.arealcompany.ms_news.api;

import com.arealcompany.ms_news.business.NewsController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsApi {


    private final NewsController newsController;

    public NewsApi(NewsController newsController) {
        this.newsController = newsController;
    }

    @GetMapping("/top-headlines")
    String fetchTopHeadlines(@RequestParam(value = "limit", defaultValue = "10") int limit){
        return newsController.fetchTopHeadlines(limit);
    }
}
