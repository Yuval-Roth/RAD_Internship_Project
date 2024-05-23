package com.arealcompany.SpringData.business.records;

import java.util.List;

public record Score(
        Integer win,
        Integer loss,
        Series series,
        List<String> linescore,
        Integer points
) {
}
