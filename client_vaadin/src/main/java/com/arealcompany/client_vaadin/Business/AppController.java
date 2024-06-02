package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.*;
import com.arealcompany.ms_common.utils.APIFetcher;
import com.arealcompany.ms_common.utils.JsonUtils;
import com.arealcompany.ms_common.utils.Response;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AppController {

    private static final String API_URI = "http://localhost:8080/";

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }

    public List<Player> getNbaPlayers() {
        Type t = new TypeToken<Response<Player>>(){}.getType();
        return fetch("nba/players",t);
    }

    public List<Team> getNbaTeams() {
        Type t = new TypeToken<Response<Team>>(){}.getType();
        return fetch("nba/teams",t);
    }

    public List<Article> getArticles() {
        Type t = new TypeToken<Response<Article>>(){}.getType();
        List<Article> news = fetch("news/top-headlines", t);
        return news;
    }

    public List<PopulationStat> getPopulationStats() {
        Type t = new TypeToken<Response<PopulationStat>>(){}.getType();
        return fetch("population/all",t);
    }

    private <T> List<T> fetch(String location, Type t){
        String fetched = APIFetcher.create()
                .withUri(API_URI + location)
                .fetch();
        return JsonUtils.<Response<T>>deserialize(fetched,t).payload;
    }

}
