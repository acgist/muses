package com.acgist.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.acgist.**.entity")
@SpringBootApplication
public class CreateTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateTableApplication.class, args);
	}

}
