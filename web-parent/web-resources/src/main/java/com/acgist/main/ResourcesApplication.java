package com.acgist.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.acgist.resources")
@SpringBootApplication
public class ResourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourcesApplication.class, args);
	}

}
