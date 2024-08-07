package com.arealcompany.ms_population.business;

import com.arealcompany.ms_common.utils.*;
import com.arealcompany.ms_population.business.dtos.PopulationStat;
import com.arealcompany.ms_population.repository.PopulationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component("populationController")
public class PopulationController {

    private static final Logger log = LoggerFactory.getLogger(PopulationController.class);
    private static final String[] COUNTRIES = {
            "France", "Germany", "Italy", "Israel", "Russia",
            "Turkey", "Kazakhstan", "Egypt", "Jordan", "India","Us","China"
    };

    private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";
    private static final String PORT = "8082";
    private final String apiKey;
    private final PopulationRepository repo;


    public PopulationController(PopulationRepository repo) {
        apiKey = EnvUtils.getEnvField(ENV_KEY_NAME);
        this.repo = repo;
    }

    public String getCountry(String country){
        if(country.equalsIgnoreCase("all")){
            log.debug("Finding all population data");
            return Response.get(repo.findAll());
        } else {
            log.debug("Finding population data for country '{}'",country);
            PopulationStat stat = repo.findByCountry(country);
            log.debug(stat == null ? "Country not found" : "Country found");
            return stat == null ? "Country not found" : Response.get(stat);
        }
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        String[] args = event.getArgs();
        log.debug("Application started with arguments: {}", Arrays.toString(args));
        if(args.length > 0 && args[0].equalsIgnoreCase("-fetch")) {
            fetchData();
        }
        log.debug("\nPopulation data ready to be queried via the REST API on port '{}'.",PORT);
    }

    public void fetchData(){
        log.debug("Fetching population data from the API...");

        List<PopulationStat> stats = new LinkedList<>();

        // countries
        Arrays.stream(COUNTRIES).forEach(country -> {
            String fetched = fetch("population/country", Pair.of("country", country));
                log.trace("Fetched population data for country '{}'", country);
                PopulationStat stat = JsonUtils.deserialize(fetched, PopulationStat.class);
                stats.add(stat);
        });

        // global
        String fetched = fetch("population");
        log.trace("Fetched global population data");
        PopulationStat stat = JsonUtils.deserialize(fetched, PopulationStat.class);
        stats.add(stat);

        repo.saveAll(stats.stream().filter(Objects::nonNull).toList());
        log.debug("Population data saved to the database.");
    }

    @SafeVarargs
    private String fetch(String location, Pair<String,String>... params) {
        log.trace("Accessing API at location '{}' with params '{}'", location,Arrays.toString(params));
        var fetcher = APIFetcher.create()
                .withUri("https://get-population.p.rapidapi.com/"+location)
                .withHeader("X-RapidAPI-Key", apiKey)
                .withHeader("X-RapidAPI-Host", "get-population.p.rapidapi.com")
                .withParams(params);
        try {
            return fetcher.fetch();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to fetch data from API", e);
            return Response.get("Failed to fetch data from API");
        }
    }

    public String updateCountry(String json) {
        PopulationStat stat;
        try{
            stat = JsonUtils.deserialize(json, PopulationStat.class);
        } catch(Exception e){
            return Response.get("Invalid json format for population data");
        }
        repo.save(stat);
        return Response.get(true);
    }
}
