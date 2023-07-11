package com.example.jdbc.springboot.assignmnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableKafka
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
