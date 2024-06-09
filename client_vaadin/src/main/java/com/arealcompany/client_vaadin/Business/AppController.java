package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.*;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.arealcompany.ms_common.utils.APIFetcher;
import com.arealcompany.ms_common.utils.JsonUtils;
import com.arealcompany.ms_common.utils.Response;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class AppController {

    private static final String API_URI = "http://localhost:8080/";

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }

    public List<Player> getNbaPlayers() throws ApplicationException {
        return fetch("nba/players",Player.class);
    }

    public List<Team> getNbaTeams() throws ApplicationException {
        return fetch("nba/teams",Team.class);
    }

    public List<Article> getArticles() throws ApplicationException {
        return fetch("news/top-headlines", Article.class);
    }

    public List<PopulationStat> getPopulationStats() throws ApplicationException {
        return fetch("population/all",PopulationStat.class);
    }

    private <T> List<T> fetch(String location, Type t) throws ApplicationException {
//        String auth = "Basic " + new String(Base64.getEncoder().encode("%s:%s".formatted(username,password).getBytes()));
        String auth = "auth";
        String fetched;
        try {
            fetched = APIFetcher.create()
                    .withUri(API_URI + location)
                    .withHeader("Authorization", auth)
                    .fetch();
        } catch (IOException | InterruptedException e) {
            throw new ApplicationException("Failed to fetch data");
        }
        Response response = Response.fromJson(fetched);
        if(response.success()) {
            return response.payload(t);
        } else {
            throw new ApplicationException(response.message());
        }
    }
}
