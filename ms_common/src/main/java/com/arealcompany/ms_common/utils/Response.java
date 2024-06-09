package com.arealcompany.ms_common.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Response {

    private final String message;
    private final boolean success;
    private final List<String> payload;

    /**
     * The data parameter will be serialized using {@link JsonUtils#serialize(Object)}
     */
    private <T> Response(String message, boolean success, List<T> payload) {
        this.message = message;
        this.success = success;
        this.payload = payload.stream().map(JsonUtils::serialize).toList();
    }

    /**
     * this constructor takes a string as data and does not serialize it
     */
    private Response(String message, boolean success, String payload) {
        this.message = message;
        this.success = success;
        this.payload = List.of(payload);
    }

    /**
     * This constructor is used when there is no data to be sent. The data field will be an empty string.
     */
    public Response(String message, boolean success) {
        this(message, success, "");
    }

    /**
     * @param success If the request was successful or not
     */
    public Response(boolean success) {
        this("", success, "");
    }

    public <T> Response(boolean success, List<T> payload) {
        this("", success, payload);
    }

    public String message() {
        return message;
    }

    public boolean success() {
        return success;
    }

    /**
     * @param typeOfT Type of the object for deserialization
     * @return Deserialized object of type T
     * @apiNote If you want to get the raw data as a string, use {@link #payload()} instead
     * <br/><br/><br/>examples for a type definition:<br/><br/><code>1) Type type = new TypeToken&lt;LinkedList&lt;SomeClass&gt;&gt;(){}.getType();</code>
     * <br/><br/><code>2) Type type = SomeClass.class;</code>
     */
    public <T> List<T> payload(Type typeOfT) {
        if(payload == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        payload.forEach(p -> list.add(JsonUtils.deserialize(p, typeOfT)));
        return list;
    }

    /**
     * @return Raw data as a string. Object must be deserialized manually
     * @apiNote If you want to get a deserialized object Use {@link #payload(Type)} instead
     */
    public List<String> payload(){
        return payload;
    }

    /**
     * This method will serialize the response object using {@link JsonUtils#serialize(Object)}
     */
    public String toJson(){
        return JsonUtils.serialize(this);
    }

    /**
     * This method will deserialize the json string using {@link JsonUtils#deserialize(String, Type)}
     */
    public static Response fromJson(String json){
        return JsonUtils.deserialize(json, Response.class);
    }

    /**
     * This method will return a response object with the message as the exception message and success as false.
     * @apiNote if the exception has a cause, the cause message will be added to the response object in the data field as a string.
     * Otherwise, the data field will be an empty string
     */
    public static Response get(Exception e){

        String cause = "";
        if(e.getCause() != null){
            cause = e.getCause().getMessage();
        }
        return new Response(e.getMessage(), false, cause);
    }

    public static <T> String get(T payload) {
        return get(List.of(payload));
    }

    public static <T> String get(List<T> payload) {
        return new Response("",true, payload).toJson();
    }

    public static String get(String error) {
        return new Response(error,false, new ArrayList<>()).toJson();
    }
}
