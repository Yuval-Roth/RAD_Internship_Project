package com.arealcompany.client_vaadin.service;

import org.springframework.stereotype.Service;

@Service
public class AppService {

    public String getWelcomeMessage() {
        return "Welcome to my app!";
    }
}
