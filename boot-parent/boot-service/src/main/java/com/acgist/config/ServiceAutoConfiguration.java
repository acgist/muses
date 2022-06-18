package com.acgist.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.service.DatabaseService;
import com.acgist.service.impl.DatabaseServiceImpl;

/**
 * Service自动配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureAfter(MyBatisAutoConfiguration.class)
public class ServiceAutoConfiguration {

	@Bean
	@ConditionalOnClass(DataSource.class)
	@ConditionalOnMissingBean
	public DatabaseService databaseService() {
		return new DatabaseServiceImpl();
	}
	
}
