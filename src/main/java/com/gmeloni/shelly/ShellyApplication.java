package com.gmeloni.shelly;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class ShellyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShellyApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(
			RestTemplateBuilder builder
	) {
		return builder.build();
	}

}
