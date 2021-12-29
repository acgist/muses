package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.acgist.boot.config.PortConfig;

@ComponentScan("com.acgist.oauth2")
@DubboComponentScan("com.acgist.oauth2")
@EnableDiscoveryClient
@SpringBootApplication
public class OAuth2Application {

	public static void main(String[] args) {
		PortConfig.setRestOAuth2Port();
		SpringApplication.run(OAuth2Application.class, args);
	}

}
