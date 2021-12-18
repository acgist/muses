package com.acgist.boot.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.service.IdService;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.LoggerContext;

/**
 * Boot自动配置
 * 
 * @author acgist
 */
@Configuration
public class BootAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(BootAutoConfiguration.class);

	/**
	 * 序列化类型
	 * 
	 * @author acgist
	 */
	public enum SerializerType {

		JDK, JACKSON;

	}

	/**
	 * 默认使用JDK序列化
	 * 
	 * Jackson不支持没有默认函数的对象：JWT Token授权信息
	 */
	@Value("${system.serializer.type:jdk}")
	private String serializerType;

	@Bean
	@ConditionalOnMissingBean
	public IdService idService() {
		return new IdService();
	}

	@Bean
	@ConditionalOnMissingBean
	public ObjectMapper objectMapper() {
		return JSONUtils.getMapper();
	}

	@Bean
	@ConditionalOnMissingBean
	public SerializerType serializerType() {
		LOGGER.info("系统序列化类型：{}", this.serializerType);
		if (SerializerType.JACKSON.name().equalsIgnoreCase(this.serializerType)) {
			return SerializerType.JACKSON;
		} else {
			return SerializerType.JDK;
		}
	}

	@PreDestroy
	public void destroy() {
		LOGGER.info("系统关闭");
		// 刷出日志缓存
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		if (context != null) {
			context.stop();
		}
	}

}