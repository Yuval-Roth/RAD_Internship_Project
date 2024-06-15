package com.arealcompany.client_vaadin.Business.dtos;

public record User(String username, String password) {

    public static User currentUser;
    public static boolean isUserLoggedIn = false;

    public User() {
        this("", "");
    }
}
