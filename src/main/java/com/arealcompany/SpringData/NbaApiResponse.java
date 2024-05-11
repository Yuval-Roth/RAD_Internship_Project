package com.arealcompany.SpringData;

import java.util.List;

public record NbaApiResponse(
        String get,
        List<String> parameters,
        List<String> errors,
        int results,
        List<Team> response
) {
}

