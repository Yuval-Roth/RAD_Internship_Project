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
import java.util.List;

@Service
public class AppController {

    private static final String API_URI = "http://localhost:8080/";

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }

    public <T> List<T> fetchByEndpoint(Endpoints endpoint) throws ApplicationException {
        return fetch(endpoint.location(), endpoint.clazz());
    }

    public void postByEndpoint(Endpoints endpoint, Object entity) throws ApplicationException {
        post(endpoint.location(), entity);
    }

    public boolean login(String username, String password) {
        User.currentUser = new User(username, password);
        List<Boolean> fetched;
        try {
            fetched = fetchByEndpoint(Endpoints.LOGIN);
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

        String auth = getAuth();
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

    private void post(String location, Object entity) throws ApplicationException {
        ApplicationException postFailed = new ApplicationException("Failed to send data");

        String auth = getAuth();
        Response response;
        try {
            String body = JsonUtils.serialize(entity);
            String fetched = APIFetcher.create()
                    .withUri(API_URI + location)
                    .withHeader("Authorization", auth)
                    .withBody(body)
                    .withPost()
                    .fetch();
            response = Response.fromJson(fetched);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw postFailed;
        }

        if (response == null) {
            throw postFailed;
        }
        if (!response.success()) {
            throw new ApplicationException(response.message());
        }
    }


    private String getAuth() {
        User user = User.currentUser;
        String credentialsString = "%s:%s".formatted(user.username(), user.password());
        return "Basic " + new String(Base64.getEncoder().encode(credentialsString.getBytes()));
    }
}
