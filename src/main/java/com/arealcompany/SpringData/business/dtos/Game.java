package com.arealcompany.SpringData.business.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(value = "games")
public record Game(
        Integer id,
        String league,
        Integer season,
        Map<String,String> date,
        Integer stage,
        Map<String,String> status,
        Periods periods,
        Map<String,String> arena,
        Map<String,Team> teams,
        Map<String,Score> scores,
        List<String> officials,
        Integer timesTied,
        Integer leadChanges,
        String nugget
) {

    private record Periods(
            Integer current,
            Integer total,
            Boolean endOfPeriod
    ) {
    }

    private record Score(
            Integer win,
            Integer loss,
            Map<String,Integer> series,
            List<String> linescore,
            Integer points
    ) {
    }
}
