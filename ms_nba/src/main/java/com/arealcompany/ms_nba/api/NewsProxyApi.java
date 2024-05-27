package com.arealcompany.ms_nba.api;

import com.arealcompany.ms_nba.business.NewsProxyController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class NewsProxyApi {

    private final NewsProxyController controller;

    public NewsProxyApi(NewsProxyController newsProxyController) {
        this.controller = newsProxyController;
    }

    @GetMapping("/top-headlines")
    String getTopHeadlines(@RequestParam Map<String,String> params){
        return controller.getTopHeadlines(params);
    }
}
