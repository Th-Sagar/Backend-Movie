package com.movieapi.Movie_Api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieApiApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("MONGO_URI",dotenv.get("MONGO_URI"));
		System.setProperty("MONGO_DATABASE",dotenv.get("MONGO_DATABASE"));
		System.setProperty("MONGO_INDEX",dotenv.get("MONGO_INDEX"));

		SpringApplication.run(MovieApiApplication.class, args);
	}

}
