package com.arealcompany.ms_population;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.arealcompany.ms_population")
@SpringBootApplication
public class MainMsPopulation {
    public static void main(String[] args) {
		SpringApplication.run(MainMsPopulation.class, args);
	}
}
