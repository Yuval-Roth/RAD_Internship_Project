package com.arealcompany.ms_nba.utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class Response<T> {

    private final boolean success;
    private final String error;
    private final List<T> payload;

    private Response(boolean success, String error, List<T> payload) {
        this.success = success;
        this.error = error;
        this.payload = payload;
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }

    public static <T> String get(T payload) {
        return get(List.of(payload));
    }

    public static <T> String get(List<T> payload) {
        return new Response<>(true, "", payload).toJson();
    }

    public static String get(String error) {
        return new Response<>(false, error, new ArrayList<>()).toJson();
    }
}
