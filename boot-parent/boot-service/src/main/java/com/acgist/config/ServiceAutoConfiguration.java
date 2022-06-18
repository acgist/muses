package com.acgist.config;

import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
public class ServiceAutoConfiguration {

	@Bean
	@ConditionalOnBean(SqlSession.class)
	@ConditionalOnMissingBean
	public DatabaseService databaseService() {
		return new DatabaseServiceImpl();
	}
	
}
