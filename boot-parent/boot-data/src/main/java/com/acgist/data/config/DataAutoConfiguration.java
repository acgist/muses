package com.acgist.data.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.service.IdService;
import com.acgist.data.entity.SnowflakeGenerator;

/**
 * 数据自动配置
 * 
 * @author acgist
 */
@Configuration
public class DataAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataAutoConfiguration.class);

	@Autowired
	private IdService idService;

	@PostConstruct
	public void init() {
		LOGGER.info("注入ID生成策略：雪花算法");
		SnowflakeGenerator.init(this.idService);
	}
	
}
