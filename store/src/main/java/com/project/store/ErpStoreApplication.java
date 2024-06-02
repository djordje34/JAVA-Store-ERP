package com.project.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ErpStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpStoreApplication.class, args);
	}

}
