package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.*;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.arealcompany.ms_common.utils.APIFetcher;
import com.arealcompany.ms_common.utils.JsonUtils;
import com.arealcompany.ms_common.utils.Response;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppController {

    private static final String API_URI = "http://localhost:8080/";
    private final Map<String, String> TABLE_ENDPOINT_MAP = new HashMap<>();

    public AppController() {
        TABLE_ENDPOINT_MAP.put("players", "nba/players");
        TABLE_ENDPOINT_MAP.put("teams", "nba/teams");
        TABLE_ENDPOINT_MAP.put("population", "population/all");
        TABLE_ENDPOINT_MAP.put("news", "news/top-headlines");
        // Add more mappings as needed
    }

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }

    public List<Player> getNbaPlayers() throws ApplicationException {
        return fetch("nba/players", Player.class);
    }

    public List<Team> getNbaTeams() throws ApplicationException {
        return fetch("nba/teams", Team.class);
    }

    public List<Article> getArticles() throws ApplicationException {
        return fetch("news/top-headlines", Article.class);
    }

    public List<PopulationStat> getPopulationStats() throws ApplicationException {
        return fetch("population/all", PopulationStat.class);
    }

    public boolean login(String username, String password) {
        User.currentUser = new User(username, password);
        List<Boolean> fetched;
        try {
            fetched = fetch("auth", Boolean.class);
        } catch (ApplicationException e) {
            return false;
        }
        Boolean success = fetched.get(0);
        if (success) {
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
        if (response == null) {
            throw fetchFailed;
        }
        if (response.success()) {
            return response.payload(t);
        } else {
            throw new ApplicationException(response.message());
        }
    }

    public <T> List<T> fetchItemsByTableName(String tableName, Class<T> clazz) throws ApplicationException {
        String location = TABLE_ENDPOINT_MAP.get(tableName);
        if (location == null) {
            throw new ApplicationException("Invalid table name: " + tableName);
        }
        return fetch(location, clazz);
    }

    public <T> void updateEntity(String endpoint, T entity) throws ApplicationException {
        User user = User.currentUser;
        String credentialsString = "%s:%s".formatted(user.username(), user.password());
        String auth = "Basic " + new String(Base64.getEncoder().encode(credentialsString.getBytes()));

        try {
            String jsonInputString = JsonUtils.serialize(entity);

            String response = APIFetcher.create()
                    .withUri(API_URI + endpoint)
                    .withHeader("Authorization", auth)
                    .withHeader("Content-Type", "application/json")
                    .withBody(jsonInputString)
                    .withPost() // Assuming your API accepts POST for update. Use .withPut() if PUT is required
                    .fetch();

            if (response == null || response.isEmpty()) {
                throw new ApplicationException("Failed to update entity, empty response from server");
            }

            // Optional: Parse response to check for success, if needed
            Response parsedResponse = Response.fromJson(response);
            if (!parsedResponse.success()) {
                throw new ApplicationException("Failed to update entity: " + parsedResponse.message());
            }

        } catch (IOException | InterruptedException e) {
            throw new ApplicationException("Failed to update entity", e);
        }
    }

    public <T> void deleteEntity(String location, T entity) throws ApplicationException {
        ApplicationException deleteFailed = new ApplicationException("Failed to delete entity");

        User user = User.currentUser;
        String credentialsString = "%s:%s".formatted(user.username(), user.password());
        String auth = "Basic " + Base64.getEncoder().encodeToString(credentialsString.getBytes());
        String requestBody = JsonUtils.serialize(entity);
        try {
            APIFetcher.create()
                    .withUri(API_URI + location)
                    .withHeader("Authorization", auth)
                    .withBody(requestBody)
                    // .withDelete()
                    .fetch();
        } catch (IOException | InterruptedException e) {
            throw deleteFailed;
        }
    }

}
