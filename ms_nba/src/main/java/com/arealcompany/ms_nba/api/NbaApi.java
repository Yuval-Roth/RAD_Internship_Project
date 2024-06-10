package com.arealcompany.ms_nba.api;

import com.arealcompany.ms_nba.business.NbaController;
import org.springframework.web.bind.annotation.*;

@RestController
public class NbaApi {

    private final NbaController controller;

    public NbaApi(NbaController nbaController) {
        this.controller = nbaController;
    }

    @GetMapping("/get/teams")
    public String getTeams(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        return controller.getTeams(limit);
    }

    @GetMapping(value = "/get/players")
    public String getPlayers(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        return controller.getPlayers(limit);
    }

    @PostMapping("/update/teams")
    public String updateTeams(@RequestBody String body) {
        return controller.updateTeam(body);

    }

    @PostMapping("/update/players")
    public String updatePlayers(@RequestBody String body) {
        return controller.updatePlayer(body);
    }

}
