package com.arealcompany.SpringData.business.records;

import java.util.Map;

public record Game(
        Integer id,
        League league,
        Integer season,
        Date date,
        Integer stage,
        Map<String,String> status,
        Periods periods,
        Arena arena,
        Map<String,Team> teams,
        scores:
        officials:
        timesTied:5
        leadChanges:6
        nugget:null
) {

}
