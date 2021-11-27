package com.acgist.common.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.common.service.IdService;

import ch.qos.logback.classic.LoggerContext;

@Configuration
@ConditionalOnClass(LoggerContext.class)
public class CommonAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public IdService idService() {
		return new IdService();
	}
	
	@PreDestroy
	public void destroy() {
		LOGGER.info("系统关闭");
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		if (context != null) {
			// 刷出日志缓存
			context.stop();
		}
	}

}