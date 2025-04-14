package com.rypsk.weeklymenucreator.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WeeklymenucreatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeeklymenucreatorApiApplication.class, args);
	}

}
