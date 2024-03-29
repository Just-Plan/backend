package com.jyp.justplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JustPlanApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JustPlanApplication.class);
		app.setAdditionalProfiles("prod");
		app.run(args);
	}
}