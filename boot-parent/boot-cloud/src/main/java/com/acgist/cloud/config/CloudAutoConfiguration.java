package com.acgist.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.service.IdService;
import com.acgist.cloud.listener.ShutdownListener;
import com.acgist.cloud.service.impl.IdServiceImpl;
import com.alibaba.cloud.nacos.NacosConfigManager;

/**
 * Cloud服务自动配置
 * 
 * @author acgist
 */
@Configuration
public class CloudAutoConfiguration {

	@Value("${system.sn:-1}")
	private int sn;
	@Value("${server.port:0}")
	private int port;
	@Value("${spring.application.name:muses}")
	private String name;
	
	@Autowired
	private NacosConfigManager nacosConfigManager;
	
	@Bean
	@ConditionalOnMissingBean
	public CloudConfig cloudConfig() {
		return CloudConfigBuilder.builder(this.nacosConfigManager)
			.init()
			.buildSn(this.sn, this.name)
			.buildPid()
			.buildPort(this.port)
			.build();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public IdService idService() {
		return new IdServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ShutdownListener shutdownListener() {
		return new ShutdownListener();
	}
	
}
