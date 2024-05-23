package com.arealcompany.SpringData;

import com.arealcompany.SpringData.business.NbaController;
import com.arealcompany.SpringData.business.records.Game;
import com.arealcompany.SpringData.business.records.NbaApiResponse;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.business.records.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.lang.reflect.Type;

@EnableMongoRepositories(basePackages = "com.arealcompany.SpringData")
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
