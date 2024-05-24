package com.arealcompany.ms_nba.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class APIFetcher {

    private String uri;
    private final List<Pair<String,String>> headers;
    private final List<Pair<String,String>> params;

    private APIFetcher() {
        headers = new LinkedList<>();
        params = new LinkedList<>();
    }

    public String fetch() {

        assert uri != null : "URI is required";

        // build full URI
        String fullUri = params.stream()
                .reduce(uri,
                        (acc, pair) -> "%s%s%s=%s".formatted(
                                acc,
                                (acc.contains("?") ? "&" : "?"),
                                pair.first(),
                                pair.second()),
                        (acc, _) -> acc);

        // build request
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(fullUri))
                .method("GET", HttpRequest.BodyPublishers.noBody());
        headers.forEach(pair -> builder.header(pair.first(), pair.second()));
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
        headers.add(Pair.of(key,value));
        return this;
    }

    public APIFetcher withParam(String key, String value) {
        params.add(Pair.of(key,value));
        return this;
    }

    public static APIFetcher create() {
        return new APIFetcher();
    }
}
