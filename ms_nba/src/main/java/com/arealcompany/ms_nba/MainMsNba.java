package com.arealcompany.ms_nba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.arealcompany.ms_nba")
@SpringBootApplication
@EnableDiscoveryClient
public class MainMsNba {
    public static void main(String[] args) {
		SpringApplication.run(MainMsNba.class, args);
	}
}
