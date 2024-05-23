package com.arealcompany.SpringData.business.records;

import java.util.List;
import java.util.Map;

public record NbaApiResponse<T>(
        String get,
        Map<String,String> parameters,
        List<String> errors,
        Integer results,
        List<T> response
) {
}

