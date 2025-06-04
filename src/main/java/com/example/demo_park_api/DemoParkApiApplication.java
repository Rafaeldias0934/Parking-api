package com.example.demo_park_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
public class DemoParkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoParkApiApplication.class, args);

	}

}
