package com.acgist.config;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.utils.ErrorUtils;
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

	@PostConstruct
	public void init() {
		this.registerException();
	}
	
	/**
	 * 注册异常
	 */
	public void registerException() {
		ErrorUtils.register(MessageCode.CODE_4001, DuplicateKeyException.class);
		ErrorUtils.register(MessageCode.CODE_4001, SQLIntegrityConstraintViolationException.class);
		ErrorUtils.register(MessageCode.CODE_4000, SQLException.class);
	}
	
	@Bean
	@ConditionalOnClass(DataSource.class)
	@ConditionalOnMissingBean
	public DatabaseService databaseService() {
		return new DatabaseServiceImpl();
	}
	
}
