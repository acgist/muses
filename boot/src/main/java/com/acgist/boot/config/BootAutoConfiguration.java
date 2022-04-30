package com.acgist.boot.config;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.acgist.boot.service.impl.FreemarkerService;
import com.acgist.boot.utils.FileUtils;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.boot.utils.SpringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Boot自动配置
 * 
 * @author acgist
 */
@Slf4j
@EnableAsync
@Configuration
public class BootAutoConfiguration {

	/**
	 * 序列化类型
	 * 
	 * @author acgist
	 */
	public enum SerializerType {
		
		JDK, JACKSON;
		
	}

	/**
	 * 服务名称
	 */
	@Value("${spring.application.name:}")
	private String name;
	/**
	 * 最小线程数量
	 */
	@Value("${system.thread.min:2}")
	private int min;
	/**
	 * 最大线程数量
	 */
	@Value("${system.thread.max:10}")
	private int max;
	/**
	 * 线程队列长度
	 */
	@Value("${system.thread.size:100000}")
	private int size;
	/**
	 * 线程存活时间
	 */
	@Value("${system.thread.live:30}")
	private int live;
	/**
	 * 默认使用JDK序列化
	 * 
	 * Jackson不支持没有默认函数的对象：JWT Token授权信息
	 */
	@Value("${system.serializer.type:jdk}")
	private String serializerType;
	
	@Autowired
	private ApplicationContext context;
	
	@Bean
	@ConditionalOnClass(freemarker.template.Configuration.class)
	@ConditionalOnMissingBean
	public FreemarkerService freemarkerService() {
		return new FreemarkerService();
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean
	public ObjectMapper objectMapper() {
		// Jackson2ObjectMapperBuilder
		return JSONUtils.buildWebMapper();
	}

	@Bean
	@ConditionalOnMissingBean
	public SerializerType serializerType() {
		log.info("系统序列化类型：{}", this.serializerType);
		if (SerializerType.JACKSON.name().equalsIgnoreCase(this.serializerType)) {
			return SerializerType.JACKSON;
		} else {
			return SerializerType.JDK;
		}
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean
	public TaskExecutor taskExecutor() {
		log.info("系统线程池配置：{}-{}-{}-{}", this.min, this.max, this.size, this.live);
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setDaemon(true);
		executor.setCorePoolSize(this.min);
		executor.setMaxPoolSize(this.max);
		executor.setQueueCapacity(this.size);
		executor.setKeepAliveSeconds(this.live);
		executor.setThreadNamePrefix(this.name + "-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}
	
	@PostConstruct
	public void init() {
		SpringUtils.setContext(this.context);
		final var runtime = Runtime.getRuntime();
		final var bean = ManagementFactory.getRuntimeMXBean();
		final String freeMemory = FileUtils.formatSize(runtime.freeMemory());
		final String totalMemory = FileUtils.formatSize(runtime.totalMemory());
		final String maxMemory = FileUtils.formatSize(runtime.maxMemory());
		final String jvmArgs = bean.getInputArguments().stream().collect(Collectors.joining(" "));
		log.info("操作系统名称：{}", System.getProperty("os.name"));
		log.info("操作系统架构：{}", System.getProperty("os.arch"));
		log.info("操作系统版本：{}", System.getProperty("os.version"));
		log.info("操作系统可用处理器数量：{}", runtime.availableProcessors());
		log.info("Java版本：{}", System.getProperty("java.version"));
		log.info("Java主目录：{}", System.getProperty("java.home"));
		log.info("Java库目录：{}", System.getProperty("java.library.path"));
		log.info("ClassPath：{}", System.getProperty("java.class.path"));
		log.info("虚拟机名称：{}", System.getProperty("java.vm.name"));
		log.info("虚拟机空闲内存：{}", freeMemory);
		log.info("虚拟机已用内存：{}", totalMemory);
		log.info("虚拟机最大内存：{}", maxMemory);
		log.info("用户目录：{}", System.getProperty("user.home"));
		log.info("工作目录：{}", System.getProperty("user.dir"));
		log.info("文件编码：{}", System.getProperty("file.encoding"));
		log.info("临时文件目录：{}", System.getProperty("java.io.tmpdir"));
		log.info("JVM启动参数：{}", jvmArgs);
	}
	
	@PreDestroy
	public void destroy() {
		log.info("系统关闭");
		// 刷出日志缓存
		final ILoggerFactory factory = LoggerFactory.getILoggerFactory();
		if (factory != null && factory instanceof LoggerContext) {
			// TODO：JDK17
			((LoggerContext) factory).stop();
		}
	}

}