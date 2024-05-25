package com.arealcompany.ms_nba.business;

import com.arealcompany.ms_nba.business.dtos.NbaApiResponse;
import com.arealcompany.ms_nba.business.dtos.Player;
import com.arealcompany.ms_nba.business.dtos.Team;
import com.arealcompany.ms_nba.repository.PlayersRepository;
import com.arealcompany.ms_nba.repository.TeamsRepository;
import com.arealcompany.ms_nba.utils.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Component("nbaController")
public class NbaController {

    private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";
    private static final Type TEAM_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Team>>(){}.getType();
    private static final Type PLAYER_RESPONSE_TYPE = new TypeToken<NbaApiResponse<Player>>() {}.getType();
    private static final Logger log = LoggerFactory.getLogger(NbaController.class);
    private static final String PORT = "8081";

    private final String apiKey;
    private final TeamsRepository teamsRepo;
    private final PlayersRepository playersRepo;

    private static final String PLAYERS_SEASON = "2021";

    public NbaController(TeamsRepository teamsRepo,
                         PlayersRepository playersRepo) {
        this.teamsRepo = teamsRepo;
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
        this.playersRepo = playersRepo;
    }

    public String getTeams(int limit){
        log.debug("Finding teams from DB with limit: {}", limit);
        List<Team> teams = limit < 0 ? teamsRepo.findAll() : teamsRepo.findAllLimit(limit);
        return Response.get(teams);
    }

    public String getPlayers(int limit){
        log.debug("Finding players from DB with limit: {}", limit);
        List<Player> players = limit < 0 ? playersRepo.findAll() : playersRepo.findAllLimit(limit);
        return Response.get(players);
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        String[] args = event.getArgs();
        log.debug("Application started with arguments: {}", Arrays.toString(args));
        if(args.length > 0 && args[0].equalsIgnoreCase("-fetch")) {
            fetchData();
        }
        log.debug("\nNBA teams data ready to be queried via the REST API on port '{}'.",PORT);
    }

    private void fetchData(){
        log.debug("Fetching NBA data...");

        // fetch teams
        NbaApiResponse<Team> teamsResponse =
                JsonUtils.deserialize(fetch("teams"), TEAM_RESPONSE_TYPE);
        log.trace("Fetched teams data from API");
        teamsRepo.saveAll(teamsResponse.response());
        log.trace("Saved teams data to database");

        // fetch players
        teamsResponse.response().stream().limit(2).forEach(team -> {
            String fetched = fetch("players", Pair.of("season", PLAYERS_SEASON), Pair.of("team", team.id().toString()));
            log.trace("Fetched players data from API for team '{}'", team.id());
            NbaApiResponse<Player> playersResponse =
                    JsonUtils.deserialize(fetched, PLAYER_RESPONSE_TYPE);
            playersRepo.saveAll(playersResponse.response());
            log.trace("Saved players data to database");
        });
        log.debug("NBA teams data fetched successfully.");
    }

    @SafeVarargs
    private String fetch(String location, Pair<String,String>... params) {
        log.trace("Accessing API at location '{}' with params '{}'", location,Arrays.toString(params));
        var fetcher = APIFetcher.create()
                .withUri("https://api-nba-v1.p.rapidapi.com/"+location)
                .withHeader("X-RapidAPI-Key", apiKey)
                .withHeader("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com");
        Arrays.stream(params).forEach(pair -> fetcher.withParam(pair.first(), pair.second()));
        return fetcher.fetch();
    }

}
