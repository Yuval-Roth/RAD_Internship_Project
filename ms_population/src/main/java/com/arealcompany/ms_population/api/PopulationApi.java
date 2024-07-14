package com.arealcompany.ms_population.api;

import com.arealcompany.ms_common.utils.Response;
import com.arealcompany.ms_population.business.PopulationController;
import org.springframework.web.bind.annotation.*;

@RestController
public class PopulationApi {

    private final PopulationController controller;

    public PopulationApi(PopulationController populationController) {
        this.controller = populationController;
    }

    @GetMapping(value = "/get/{country}")
    public String getCountry(@PathVariable String country){
        return controller.getCountry(country);
    }

    @PostMapping("/update")
    public String updateCountry(@RequestBody String body) {
        return controller.updateCountry(body);
    }

    @GetMapping("/get/fetch")
    public String fetch() {
        controller.fetchData();
        return Response.get(true);
    }
}
