package com.arealcompany.ms_nba.api;

import com.arealcompany.ms_nba.business.NbaController;
import com.arealcompany.ms_nba.business.dtos.Player;
import com.arealcompany.ms_nba.business.dtos.Team;
import com.arealcompany.ms_nba.utils.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NbaApi {

    private final NbaController controller;

    public NbaApi(NbaController nbaController) {
        this.controller = nbaController;
    }

    @GetMapping("/teams")
    public String getTeams(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        return controller.getTeams(limit);
    }

    @GetMapping(value = "/players")
    public String getPlayers(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        return controller.getPlayers(limit);
    }
}
