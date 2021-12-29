package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.acgist.boot.config.PortConfig;

@ComponentScan("com.acgist.resources")
@DubboComponentScan("com.acgist.resources")
@EnableDiscoveryClient
@SpringBootApplication
public class ResourcesApplication {

	public static void main(String[] args) {
		PortConfig.setWebPort();
		SpringApplication.run(ResourcesApplication.class, args);
	}

}
