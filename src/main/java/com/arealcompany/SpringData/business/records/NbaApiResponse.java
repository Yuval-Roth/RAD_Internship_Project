package com.arealcompany.SpringData.business.records;

import java.util.List;

public record NbaApiResponse<T>(
        String get,
        List<String> parameters,
        List<String> errors,
        Integer results,
        List<T> response
) {
}

