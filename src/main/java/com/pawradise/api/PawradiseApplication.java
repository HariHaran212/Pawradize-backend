package com.pawradise.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableAsync
@EnableScheduling
public class PawradiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PawradiseApplication.class, args);
	}

}
