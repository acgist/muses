package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.acgist.boot.config.PortConfig;

@ComponentScan("com.acgist.web")
@DubboComponentScan("com.acgist.web")
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
public class WebApplication {

	public static void main(String[] args) {
		PortConfig.setWebPort();
		SpringApplication.run(WebApplication.class, args);
	}

}
