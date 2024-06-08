package com.arealcompany.ms_api_gateway.business.security;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(value = "users")
public final class User implements UserDetails {

    @MongoId
    private final String username;
    private final String password;

    User(
            String username,
            String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @MongoId
    @Override
    public String getUsername() {
        return username;
    }
}
