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
 * Cloud服务配置
 * 
 * @author acgist
 */
@Configuration
public class CloudAutoConfiguration {

	/**
	 * 系统编号：01~99
	 * 
	 * 可以配置负数：自动生成
	 */
	@Value("${system.sn:-1}")
	private int sn;
	/**
	 * 服务名称
	 */
	@Value("${spring.application.name:}")
	private String name;
	/**
	 * 服务端口
	 */
	@Value("${server.port:0}")
	private int port;
	
	@Autowired
	private NacosConfigManager nacosConfigManager;
	
	@Bean
	@ConditionalOnMissingBean
	public CloudConfig cloudConfig() {
		return CloudConfigBuilder.builder(this.nacosConfigManager)
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
