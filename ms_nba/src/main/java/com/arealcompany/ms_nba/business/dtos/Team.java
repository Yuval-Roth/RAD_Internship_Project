package com.arealcompany.ms_nba.business.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(value = "teams")
public record Team (
        Integer id,
        String name,
        String nickname,
        String code,
        String city,
        String logo,
        Boolean allStar,
        Boolean nbaFranchise,
        Map<String,League> leagues) {

    private record League (
            String conference,
            String division
    ) {}
}
