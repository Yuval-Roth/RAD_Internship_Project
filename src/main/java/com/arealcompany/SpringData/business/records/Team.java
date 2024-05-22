package com.arealcompany.SpringData.business.records;

import java.util.Map;

public record Team (
        Integer id,
        String name,
        String nickname,
        String code,
        String city,
        String logo,
        Boolean allStar,
        Boolean nbaFranchise,
        Map<String,League> leagues) { }
