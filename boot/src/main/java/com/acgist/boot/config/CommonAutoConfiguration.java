package com.acgist.boot.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.service.IdService;

import ch.qos.logback.classic.LoggerContext;

@Configuration
public class CommonAutoConfiguration {

	/**
	 * 序列化类型
	 * 
	 * @author acgist
	 */
	public enum SerializerType {
		
		JDK,
		JACKSON;
		
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonAutoConfiguration.class);

	@Value("${system.serializer.type:jdk}")
	private String serializerType;
	
	@Bean
	@ConditionalOnMissingBean
	public IdService idService() {
		return new IdService();
	}
	
	@Bean
	public SerializerType serializerType() {
		if(SerializerType.JACKSON.name().equalsIgnoreCase(this.serializerType)) {
			return SerializerType.JACKSON;
		} else {
			return SerializerType.JDK;
		}
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