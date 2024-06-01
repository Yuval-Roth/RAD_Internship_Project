package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.*;
import com.arealcompany.ms_common.utils.APIFetcher;
import com.arealcompany.ms_common.utils.JsonUtils;
import com.arealcompany.ms_common.utils.Response;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppController {

    private static final String API_URI = "http://localhost:8080/";

    List<Player> players;
    List<Team> teams;
    List<Article> articles;
    List<PopulationStat> populationStats;

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }

    public List<Player> getNbaPlayers() {
        return players;
    }

    public List<Team> getNbaTeams() {
        return teams;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public List<PopulationStat> getPopulationStats() {
        return populationStats;
    }

    private void fetchData() {
        Type t = new TypeToken<Response<Player>>(){}.getType();
        players = JsonUtils
                .<Response<Player>>deserialize(fetch("nba/players"),t)
                .payload;

        t = new TypeToken<Response<Team>>(){}.getType();
        teams = JsonUtils
                .<Response<Team>>deserialize(fetch("nba/teams"),t)
                .payload;

        t = new TypeToken<Response<Article>>(){}.getType();
        articles = JsonUtils
                .<Response<Article>>deserialize(fetch("news"),t)
                .payload;

        t = new TypeToken<Response<PopulationStat>>(){}.getType();
        populationStats = JsonUtils
                .<Response<PopulationStat>>deserialize(fetch("population"),t)
                .payload;
    }

    private String fetch(String location){
        return APIFetcher.create()
                .withUri(API_URI + location)
                .fetch();
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        fetchData();
    }
}
