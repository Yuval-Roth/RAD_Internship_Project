package com.arealcompany.SpringData.business.records;

import java.util.Map;

public record Team (
        int id,
        String name,
        String nickname,
        String code,
        String city,
        String logo,
        boolean allStar,
        boolean nbaFranchise,
        Map<String,League> leagues
) {
    public String prettyHtml() {
        return  "id = " + id +
                "</br>name = '" + name + '\'' +
                "</br>nickname = '" + nickname + '\'' +
                "</br>code = '" + code + '\'' +
                "</br>city = '" + city + '\'' +
                "</br>logo = '" + logo + '\'' +
                "</br>allStar = " + allStar +
                "</br>nbaFranchise = " + nbaFranchise +
                "</br>leagues = " + leagues;
    }
}
