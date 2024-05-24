package com.arealcompany.SpringData.business;

import com.arealcompany.SpringData.business.dtos.Game;
import com.arealcompany.SpringData.business.dtos.NbaApiResponse;
import com.arealcompany.SpringData.business.dtos.Player;
import com.arealcompany.SpringData.business.dtos.Team;
import com.arealcompany.SpringData.repository.GamesRepository;
import com.arealcompany.SpringData.repository.PlayersRepository;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.utils.APIFetcher;
import com.arealcompany.SpringData.utils.EnvUtils;
import com.arealcompany.SpringData.utils.JsonUtils;
import com.arealcompany.SpringData.utils.Pair;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Arrays;

@Component("nbaController")
public class NbaController {

    private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";
    private static final Type TEAM_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Team>>(){}.getType();
    private static final Type GAME_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Game>>() {}.getType();
    private static final Type PLAYER_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Player>>() {}.getType();

    private final String apiKey;
    private final TeamsRepository teamsRepo;
    private final GamesRepository gamesRepo;
    private final PlayersRepository playersRepo;

    private static final String PLAYERS_SEASON = "2021";

    public NbaController(TeamsRepository teamsRepo,
                         GamesRepository gamesRepo,
                         PlayersRepository playersRepo) {
        this.teamsRepo = teamsRepo;
        this.gamesRepo = gamesRepo;
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
        this.playersRepo = playersRepo;
    }

    public void fetchData(){

        // fetch teams
        NbaApiResponse<Team> teamsResponse =
                JsonUtils.deserialize(fetch("teams"), TEAM_RESPONSE_TYPE);
        teamsRepo.saveAll(teamsResponse.response());

        // fetch players
        teamsResponse.response().stream().limit(2).forEach(team -> {
            String fetch = fetch("players", Pair.of("season", PLAYERS_SEASON), Pair.of("team", team.id().toString()));
            NbaApiResponse<Player> playersResponse =
                    JsonUtils.deserialize(fetch, PLAYER_RESPONSE_TYPE);
            playersRepo.saveAll(playersResponse.response());
        });


//        // fetch seasons
//        NbaApiResponse<Integer> seasonsResponse =
//                JsonUtils.deserialize(fetch("seasons"), SEASONS_RESPONSE_TYPE);
//
//        // fetch games for each season
//        seasonsResponse.response().forEach(season -> {
//            String response = fetch("games",Pair.of("season", season.toString()));
//            NbaApiResponse<Game> gamesResponse = JsonUtils.deserialize(
//                    response, GAME_RESPONSE_TYPE);
//            gamesRepo.saveAll(gamesResponse.response());
//        });
    }

    @SafeVarargs
    private String fetch(String location, Pair<String,String>... params) {
        var fetcher = APIFetcher.create()
                .withUri("https://api-nba-v1.p.rapidapi.com/"+location)
                .withHeader("X-RapidAPI-Key", apiKey)
                .withHeader("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com");
        Arrays.stream(params).forEach(pair -> fetcher.withParam(pair.first(), pair.second()));
        return fetcher.fetch();
    }


    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        System.out.println("Would you like to download teams NBA data? (y/n)");
        String response = System.console().readLine();

        // download the data if the user wants to
        if (response.equals("y")) {
            System.out.println("Downloading NBA teams data...");
            fetchData();
            System.out.println("Done!");
        } else {
            System.out.println("Skipping download.");
        }

        System.out.println("\nNBA teams data ready to be queried via the REST API on port 8080.");
    }
}
