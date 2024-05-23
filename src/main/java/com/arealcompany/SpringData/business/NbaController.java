package com.arealcompany.SpringData.business;

import com.arealcompany.SpringData.business.records.Game;
import com.arealcompany.SpringData.business.records.NbaApiResponse;
import com.arealcompany.SpringData.business.records.Team;
import com.arealcompany.SpringData.repository.GamesRepository;
import com.arealcompany.SpringData.utils.APIFetcher;
import com.arealcompany.SpringData.utils.EnvUtils;
import com.arealcompany.SpringData.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component("nbaController")
public class NbaController {

    private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";
    private static final Type TEAM_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Team>>(){}.getType();
    private static final Type GAME_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Game>>() {
    }.getType();

    private final String apiKey;
    private final MongoRepository<Team, String> teamsRepo;
    private final GamesRepository gamesRepo;

    public NbaController(MongoRepository<Team, String> teamsRepo, GamesRepository gamesRepository) {
        this.teamsRepo = teamsRepo;
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
        this.gamesRepo = gamesRepository;

    }

    public void fetchData(){
        String response = fetch("teams");
        NbaApiResponse<Team> teamsResponse = JsonUtils.deserialize(response, TEAM_RESPONSE_TYPE);
        teamsRepo.saveAll(teamsResponse.response());

        response = fetch("games");
        NbaApiResponse<Game> gamesResponse = JsonUtils.deserialize(response, GAME_RESPONSE_TYPE);
        gamesRepo.saveAll(gamesResponse.response());
    }

    private String fetch(String location) {
        return APIFetcher.create()
                .withUri("https://api-nba-v1.p.rapidapi.com/"+location)
                .withHeader("X-RapidAPI-Key", apiKey)
                .withHeader("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com")
                .fetch();
    }

}
