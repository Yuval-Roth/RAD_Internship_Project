package com.arealcompany.client_vaadin.Business;

import com.arealcompany.client_vaadin.Business.dtos.Article;
import com.arealcompany.client_vaadin.Business.dtos.Player;
import com.arealcompany.client_vaadin.Business.dtos.PopulationStat;
import com.arealcompany.client_vaadin.Business.dtos.Team;

public enum Endpoints {
    GET_TEAMS("nba/get/teams", Team.class),
    GET_PLAYERS("nba/get/players", Player.class),
    GET_NEWS("news/get/top-headlines", Article.class),
    GET_POPULATION("population/get/all", PopulationStat.class),
    UPDATE_TEAM("nba/update/teams", Boolean.class),
    UPDATE_PLAYER("nba/update/players", Boolean.class),
    UPDATE_POPULATION("population/update", Boolean.class),
    DELETE_TEAM("nba/delete/teams", Boolean.class),
    DELETE_PLAYER("nba/delete/players", Boolean.class),
    DELETE_POPULATION("population/delete", Boolean.class),
    LOGIN("auth", Boolean.class),
    FETCH_NBA_DATA("nba/get/fetch", Boolean.class),
    FETCH_POPULATION_DATA("population/get/fetch", Boolean.class);

    private final String location;
    private final Class<?> clazz;

    <T> Endpoints(String location,Class<T> clazz) {
        this.location = location;
        this.clazz = clazz;
    }

    public String location() {
        return location;
    }
    
    public Class<?> clazz() {
        return clazz;
    }
}
