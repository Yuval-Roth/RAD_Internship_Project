package com.arealcompany.ms_population;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.arealcompany.ms_population")
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
