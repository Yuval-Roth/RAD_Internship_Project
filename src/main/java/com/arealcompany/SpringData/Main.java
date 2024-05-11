package com.arealcompany.SpringData;

import com.google.gson.Gson;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@EnableMongoRepositories(basePackages = "com.arealcompany.SpringData")
@SpringBootApplication
public class Main implements ApplicationContextAware {

	private static final String ENV_KEY_NAME = "RAPIDAPI_KEY";
	private static ApplicationContext context;
	private static MongoRepository<Team, String> repo;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		System.out.println("Would you like to download teams NBA data? (y/n)");
		String response = System.console().readLine();

		if (response.equals("y")) {
			System.out.println("Downloading NBA teams data...");
			downloadNbaData();
			System.out.println("Done!");
		} else {
			System.out.println("Skipping download.");
		}

		System.out.println("\nNBA teams data ready to be queried via the REST API on port 80.");
	}

	private static void downloadNbaData() {
		repo = context.getBean(NbaTeamsData.class);
		repo.deleteAll();
		Gson gson = new Gson();

		String response = callAPI();
		NbaApiResponse nbaData = gson.fromJson(response, NbaApiResponse.class);
		repo.saveAll(nbaData.response());
	}

	private static String callAPI() {

		// get the API key from the environment
		String apiKey = System.getenv(ENV_KEY_NAME);
		if (apiKey == null || apiKey.isEmpty()) {
			throw new RuntimeException(ENV_KEY_NAME+" environment variable not set.");
		}

		// build the request
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api-nba-v1.p.rapidapi.com/teams"))
				.header("X-RapidAPI-Key", apiKey)
				.header("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();

		// send api request
		HttpResponse<String> response;
		try(HttpClient client = HttpClient.newHttpClient() ) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
}
