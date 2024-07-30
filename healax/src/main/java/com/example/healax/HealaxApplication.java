package com.example.healax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealaxApplication.class, args);
	}

}
