package com.arealcompany.SpringData;

import com.arealcompany.SpringData.business.NbaController;
import com.arealcompany.SpringData.repository.TeamsRepository;
import com.arealcompany.SpringData.business.records.Team;
import com.google.gson.Gson;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.arealcompany.SpringData")
@SpringBootApplication
public class Main implements ApplicationContextAware {


	private static ApplicationContext context;

    public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		System.out.println("Would you like to download teams NBA data? (y/n)");
		String response = System.console().readLine();

		// download the data if the user wants to
		if (response.equals("y")) {
			System.out.println("Downloading NBA teams data...");
			context.getBean("nbaController", NbaController.class).fetchData();
			System.out.println("Done!");
		} else {
			System.out.println("Skipping download.");
		}

		System.out.println("\nNBA teams data ready to be queried via the REST API on port 80.");
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
}
