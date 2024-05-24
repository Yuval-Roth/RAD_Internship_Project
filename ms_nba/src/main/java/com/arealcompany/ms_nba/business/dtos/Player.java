package com.arealcompany.ms_nba.business.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(value = "players")
public record Player(
        Integer id,
        String firstname,
        String lastname,
        Map<String,String> birth,
        Map<String,Integer> nba,
        Map<String,String> height,
        String affiliation,
        Map<String,League> leagues
) {
    private record League (
            Integer jersey,
            Boolean active,
            String pos
    ){}
}
