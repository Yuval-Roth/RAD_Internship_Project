package com.arealcompany.client_vaadin.Business.dtos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.JavaBean;
import java.util.Map;
import java.util.Objects;

@JavaBean
public final class Player {

    private final Integer id;
    private final String firstname;
    private final String lastname;
    private final Map<String, String> birth;
    private final Map<String, Integer> nba;
    private final Map<String, String> height;
    private final String affiliation;
    private final Map<String, League> leagues;

    public Player(
            Integer id,
            String firstname,
            String lastname,
            Map<String, String> birth,
            Map<String, Integer> nba,
            Map<String, String> height,
            String affiliation,
            Map<String, League> leagues
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birth = birth;
        this.nba = nba;
        this.height = height;
        this.affiliation = affiliation;
        this.leagues = leagues;
    }

    public Player() {
        this(0, "", "", Map.of(), Map.of(), Map.of(), "", Map.of());
    }

    public Integer id() {
        return id;
    }

    public String firstname() {
        return firstname;
    }

    public String lastname() {
        return lastname;
    }

    public String birth() {
        return birth.get("date");
    }

    public Map<String, Integer> nba() {
        return nba;
    }

    public String height() {
        return height.get("meters");
    }

    public String affiliation() {
        return affiliation;
    }

    public String leagues() {
        return leagues.keySet().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Player) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstname, that.firstname) &&
                Objects.equals(this.lastname, that.lastname) &&
                Objects.equals(this.birth, that.birth) &&
                Objects.equals(this.nba, that.nba) &&
                Objects.equals(this.height, that.height) &&
                Objects.equals(this.affiliation, that.affiliation) &&
                Objects.equals(this.leagues, that.leagues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, birth, nba, height, affiliation, leagues);
    }

    @Override
    public String toString() {
        return "Player[" +
                "id=" + id + ", " +
                "firstname=" + firstname + ", " +
                "lastname=" + lastname + ", " +
                "birth=" + birth + ", " +
                "nba=" + nba + ", " +
                "height=" + height + ", " +
                "affiliation=" + affiliation + ", " +
                "leagues=" + leagues + ']';
    }


    public record League(
            Integer jersey,
            Boolean active,
            String pos
    ) {
    }
}
