package com.arealcompany.SpringData.business.records;

import java.util.List;
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
        Map<String,Score> scores,
        List<String> officials,
        Integer timesTied,
        Integer leadChanges,
        String nugget
) {

}
