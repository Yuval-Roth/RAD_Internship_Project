package com.arealcompany.ms_nba.business.dtos;

import java.util.List;
import java.util.Map;

public record NbaApiResponse<T>(
        String get,
        Map<String,String> parameters,
        Map<String,String> errors,
        Integer results,
        List<T> response
) {
}

