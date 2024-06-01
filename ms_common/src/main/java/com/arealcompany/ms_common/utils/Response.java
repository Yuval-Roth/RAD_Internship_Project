package com.arealcompany.ms_common.utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class Response<T> {

    public final boolean success;
    public final String error;
    public final List<T> payload;

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
