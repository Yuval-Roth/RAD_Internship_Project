package com.arealcompany.SpringData.api;

import com.arealcompany.SpringData.business.dtos.Player;
import com.arealcompany.SpringData.repository.GamesRepository;
import com.arealcompany.SpringData.repository.PlayersRepository;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.business.dtos.Team;
import com.arealcompany.SpringData.utils.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NbaApi {

    private final GamesRepository gamesRepo;
    private final TeamsRepository teamsRepo;
    private final PlayersRepository playersRepo;

    public NbaApi(GamesRepository games, TeamsRepository teams, PlayersRepository playersRepo) {
        this.gamesRepo = games;
        this.teamsRepo = teams;
        this.playersRepo = playersRepo;
    }

    @GetMapping("/teams")
    public String getTeams(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        List<Team> teams = limit < 0 ? teamsRepo.findAll() : teamsRepo.findAllLimit(limit);
        StringBuilder output = new StringBuilder();
        teams.forEach(team -> output.append(JsonUtils.serialize(team)).append("\n"));
        output.deleteCharAt(output.length() - 1);
        return output.toString();
    }

    @GetMapping(value = "/players",
    produces={"application/json","application/xml"})
    public String getPlayers(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        List<Player> games = limit < 0 ? playersRepo.findAll() : playersRepo.findAllLimit(limit);
        StringBuilder output = new StringBuilder();
        games.forEach(game -> output.append(JsonUtils.serialize(game)).append("\n"));
        output.deleteCharAt(output.length() - 1);
        return output.toString();
    }

//    @GetMapping("/games")
//    public String getGames(@RequestParam(value = "maxcount", defaultValue = "-1") Integer maxCount,
//                           @RequestParam(value = "season", defaultValue = "-1") Integer season) {
//
//        List<Game> games;
//        if (season > 0) {
//            games = maxCount >= 0 ?
//                    gamesRepo.findBySeasonLimit(season, maxCount) : gamesRepo.findBySeason(season);
//        } else {
//            games = maxCount >= 0 ?
//                    gamesRepo.findAllLimit(maxCount) : gamesRepo.findAll();
//        }
//
//        StringBuilder output = new StringBuilder();
//        games.forEach(game -> output.append(JsonUtils.serialize(game)).append("\n"));
//        return output.toString();
//    }
}
