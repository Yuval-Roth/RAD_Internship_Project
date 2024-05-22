package com.arealcompany.SpringData.business;

import com.arealcompany.SpringData.business.records.Team;
import com.arealcompany.SpringData.utils.APIFetcher;
import com.arealcompany.SpringData.utils.EnvUtils;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component("nbaController")
public class NbaController {

    private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";

    private final String apiKey;
    private final MongoRepository<Team, String> teamsRepo;

    public NbaController(MongoRepository<Team, String> teamsRepo) {
        this.teamsRepo = teamsRepo;
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
    }

    private String fetchTeams() {
        return APIFetcher.create()
                .withUri("https://api-nba-v1.p.rapidapi.com/teams")
                .withHeader("X-RapidAPI-Key", apiKey)
                .withHeader("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com")
                .fetch();
    }
}
