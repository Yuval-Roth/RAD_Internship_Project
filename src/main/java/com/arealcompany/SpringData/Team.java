package com.arealcompany.SpringData;

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
}
