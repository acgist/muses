package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.acgist.boot.config.PortConfig;

@ComponentScan("com.acgist.gateway")
@DubboComponentScan("com.acgist.gateway")
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		PortConfig.setGatewayPort();
		SpringApplication.run(GatewayApplication.class, args);
	}

}
