package com.arealcompany.client_vaadin.Business.dtos;

import java.util.Map;
import java.util.Objects;

public final class Team {
    private final Integer id;
    private final String name;
    private final String nickname;
    private final String code;
    private final String city;
    private final String logo;
    private final Boolean allStar;
    private final Boolean nbaFranchise;
    private final Map<String, League> leagues;

    public Team(
            Integer id,
            String name,
            String nickname,
            String code,
            String city,
            String logo,
            Boolean allStar,
            Boolean nbaFranchise,
            Map<String, League> leagues) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.code = code;
        this.city = city;
        this.logo = logo;
        this.allStar = allStar;
        this.nbaFranchise = nbaFranchise;
        this.leagues = leagues;
    }

    public Integer id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String nickname() {
        return nickname;
    }

    public String code() {
        return code;
    }

    public String city() {
        return city;
    }

    public String logo() {
        return logo;
    }

    public String allStar() {
        return allStar ? "Yes" : "No";
    }

    public String nbaFranchise() {
        return nbaFranchise? "Yes" : "No";
    }

    public Map<String, League> leagues() {
        return leagues;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Team) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.nickname, that.nickname) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.city, that.city) &&
                Objects.equals(this.logo, that.logo) &&
                Objects.equals(this.allStar, that.allStar) &&
                Objects.equals(this.nbaFranchise, that.nbaFranchise) &&
                Objects.equals(this.leagues, that.leagues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nickname, code, city, logo, allStar, nbaFranchise, leagues);
    }

    @Override
    public String toString() {
        return "Team[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "nickname=" + nickname + ", " +
                "code=" + code + ", " +
                "city=" + city + ", " +
                "logo=" + logo + ", " +
                "allStar=" + allStar + ", " +
                "nbaFranchise=" + nbaFranchise + ", " +
                "leagues=" + leagues + ']';
    }


    private record League(
            String conference,
            String division
    ) {
    }
}
