package com.arealcompany.SpringData.business.records;

public record Periods(
        Integer current,
        Integer total,
        Boolean endOfPeriod
) {
}
