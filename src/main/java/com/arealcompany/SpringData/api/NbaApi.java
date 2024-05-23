package com.arealcompany.SpringData.api;

import com.arealcompany.SpringData.business.records.Game;
import com.arealcompany.SpringData.repository.GamesRepository;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.business.records.Team;
import com.arealcompany.SpringData.utils.JsonUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NbaApi {

    private final GamesRepository gamesRepo;
    private final TeamsRepository teamsRepo;

    public NbaApi(GamesRepository games, TeamsRepository teams) {
        this.gamesRepo = games;
        this.teamsRepo = teams;
    }

    @GetMapping("/teams")
    public String getTeams(@RequestParam(value = "maxcount", defaultValue = "-1") Integer maxCount) {
        List<Team> teams = maxCount < 0 ? teamsRepo.findAll() : teamsRepo.findAllLimit(maxCount);
        StringBuilder output = new StringBuilder();
        teams.forEach(team -> output.append(JsonUtils.serialize(team)).append("\n"));
        return output.toString();
    }

    @GetMapping("/games")
    public String getGames(@RequestParam(value = "maxcount", defaultValue = "-1") Integer maxCount,
                           @RequestParam(value = "season", defaultValue = "-1") Integer season) {

        List<Game> games;
        if (season > 0) {
            games = maxCount >= 0 ?
                    gamesRepo.findBySeasonLimit(season, maxCount) : gamesRepo.findBySeason(season);
        } else {
            games = maxCount >= 0 ?
                    gamesRepo.findAllLimit(maxCount) : gamesRepo.findAll();
        }

        StringBuilder output = new StringBuilder();
        games.forEach(game -> output.append(JsonUtils.serialize(game)).append("\n"));
        return output.toString();
    }
}
