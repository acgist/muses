package com.acgist.concurrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConcurrentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrentApplication.class, args);
	}

}
