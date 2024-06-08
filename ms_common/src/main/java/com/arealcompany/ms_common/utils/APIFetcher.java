package com.arealcompany.ms_common.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class APIFetcher {

    private String uri;
    private final Map<String,String> headers;
    private final Map<String,String> params;

    private APIFetcher() {
        headers = new HashMap<>();
        params = new HashMap<>();
    }

    public String fetch() {

        assert uri != null : "URI is required";

        // build full URI
        String fullUri = params.entrySet().stream()
                .reduce(uri,
                        (acc, entry) -> "%s%s%s=%s".formatted(
                                acc,
                                (acc.contains("?") ? "&" : "?"),
                                entry.getKey(),
                                entry.getValue()),
                        (acc, _) -> acc);

        // build request
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(fullUri))
                .method("GET", HttpRequest.BodyPublishers.noBody());
        headers.forEach(builder::header);
        HttpRequest request = builder.build();

        // send request
        HttpResponse<String> response;
        try(HttpClient client = HttpClient.newHttpClient() ) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }

    public APIFetcher withUri(String uri) {
        this.uri = uri;
        return this;
    }
    public APIFetcher withHeader(String key, String value) {
        headers.put(key,value);
        return this;
    }

    public APIFetcher withParam(String key, String value) {
        value = value.replaceAll(" ", "%20");
        params.put(key,value);
        return this;
    }

    public static APIFetcher create() {
        return new APIFetcher();
    }
}
