package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.*;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.arealcompany.ms_common.utils.APIFetcher;
import com.arealcompany.ms_common.utils.Response;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
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
        return fetch("nba/get/teams",Team.class);
    }

    public List<Article> getArticles() throws ApplicationException {
        return fetch("news/get/top-headlines", Article.class);
    }

    public List<PopulationStat> getPopulationStats() throws ApplicationException {
        return fetch("population/get/all",PopulationStat.class);
    }

    public boolean login(String username, String password) {
        User.currentUser = new User(username, password);
        List<Boolean> fetched;
        try {
            fetched = fetch("auth",Boolean.class);
        } catch (ApplicationException e) {
            return false;
        }
        Boolean success = fetched.getFirst();
        if(success) {
            User.isUserLoggedIn = true;
        } else {
            User.currentUser = null;
        }
        return success;
    }

    private <T> List<T> fetch(String location, Type t) throws ApplicationException {
        ApplicationException fetchFailed = new ApplicationException("Failed to fetch data");

        User user = User.currentUser;
        String credentialsString = "%s:%s".formatted(user.username(), user.password());
        String auth = "Basic " + new String(Base64.getEncoder().encode(credentialsString.getBytes()));
        Response response;
        try {
            String fetched = APIFetcher.create()
                    .withUri(API_URI + location)
                    .withHeader("Authorization", auth)
                    .fetch();
            response = Response.fromJson(fetched);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw fetchFailed;
        }
        if(response == null){
            throw fetchFailed;
        }
        if(response.success()) {
            return response.payload(t);
        } else {
            throw new ApplicationException(response.message());
        }
    }

    //a method for updating an object and sending it to the backend
    public <T> void updateObject(T object, String location) throws ApplicationException {

    }

}
