package com.example.PSP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PspApplication {

	public static void main(String[] args) {
		SpringApplication.run(PspApplication.class, args);
	}

}
