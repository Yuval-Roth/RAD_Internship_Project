package com.arealcompany.ms_population.api;

import com.arealcompany.ms_population.business.PopulationController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PopulationApi {

    private final PopulationController controller;

    public PopulationApi(PopulationController populationController) {
        this.controller = populationController;
    }

    @GetMapping("/")
    public String getGlobal(@RequestParam(value = "limit", defaultValue = "-1") Integer limit) {
        return controller.getGlobal();
    }

    @GetMapping(value = "/{country}")
    public String getCountry(@PathVariable String country){
        return controller.getCountry(country);
    }
}
