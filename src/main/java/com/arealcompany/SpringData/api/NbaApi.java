package com.arealcompany.SpringData.api;

import com.arealcompany.SpringData.business.records.Game;
import com.arealcompany.SpringData.repository.GamesRepository;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.business.records.Team;
import com.arealcompany.SpringData.utils.JsonUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
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
        List<Team> teams = teamsRepo.findAll();
        StringBuilder output = new StringBuilder();
        int count = 0;
        for (Team team : teams) {
            if (maxCount >= 0 && count >= maxCount) {
                break;
            }
            output.append(JsonUtils.serialize(team)).append("\n");
            count++;
        }
        return output.toString();
    }

    @GetMapping("/games")
    public String getGames(@RequestParam(value = "maxcount", defaultValue = "-1") Integer maxCount) {
        List<Game> games = gamesRepo.findAll();
        StringBuilder output = new StringBuilder();
        int count = 0;
        for (Game game : games) {
            if (maxCount >= 0 && count >= maxCount) {
                break;
            }
            output.append(JsonUtils.serialize(game)).append("\n");
            count++;
        }
        return output.toString();
    }
}
