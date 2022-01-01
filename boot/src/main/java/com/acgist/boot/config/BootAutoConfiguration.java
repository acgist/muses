package com.acgist.boot.config;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.acgist.boot.FileUtils;
import com.acgist.boot.JSONUtils;
import com.acgist.boot.listener.ShutdownListener;
import com.acgist.boot.service.IdService;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.LoggerContext;

/**
 * Boot自动配置
 * 
 * @author acgist
 */
@EnableAsync
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
	@Value("${system.thread.size:1000}")
	private int size;
	/**
	 * 线程存活事件
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
	private NacosConfigManager nacosConfigManager;

	@Bean
	@ConditionalOnMissingBean
	public MusesConfig musesConfig() {
		return MusesConfigBuilder.builder(this.nacosConfigManager)
			.buildSn(this.sn, this.name)
			.buildPid()
			.buildPort(this.port)
			.build();
	}
	
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

	@Bean
	@ConditionalOnMissingBean
	public TaskExecutor taskExecutor() {
		LOGGER.info("系统线程池配置：{}-{}-{}-{}", this.min, this.max, this.size, this.live);
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
	
	@Bean
	@ConditionalOnMissingBean
	public ShutdownListener shutdownListener() {
		return new ShutdownListener();
	}

	@PostConstruct
	public void init() {
		final var runtime = Runtime.getRuntime();
		final var bean = ManagementFactory.getRuntimeMXBean();
		final String freeMemory = FileUtils.formatSize(runtime.freeMemory());
		final String totalMemory = FileUtils.formatSize(runtime.totalMemory());
		final String maxMemory = FileUtils.formatSize(runtime.maxMemory());
		final String jvmArgs = bean.getInputArguments().stream().collect(Collectors.joining(" "));
		LOGGER.info("操作系统名称：{}", System.getProperty("os.name"));
		LOGGER.info("操作系统架构：{}", System.getProperty("os.arch"));
		LOGGER.info("操作系统版本：{}", System.getProperty("os.version"));
		LOGGER.info("操作系统可用处理器数量：{}", runtime.availableProcessors());
		LOGGER.info("Java版本：{}", System.getProperty("java.version"));
		LOGGER.info("Java主目录：{}", System.getProperty("java.home"));
		LOGGER.info("Java库目录：{}", System.getProperty("java.library.path"));
		LOGGER.info("虚拟机名称：{}", System.getProperty("java.vm.name"));
		LOGGER.info("虚拟机空闲内存：{}", freeMemory);
		LOGGER.info("虚拟机已用内存：{}", totalMemory);
		LOGGER.info("虚拟机最大内存：{}", maxMemory);
		LOGGER.info("用户目录：{}", System.getProperty("user.home"));
		LOGGER.info("工作目录：{}", System.getProperty("user.dir"));
		LOGGER.info("文件编码：{}", System.getProperty("file.encoding"));
		LOGGER.info("临时文件目录：{}", System.getProperty("java.io.tmpdir"));
		LOGGER.info("JVM启动参数：{}", jvmArgs);
	}
	
	@PreDestroy
	public void destroy() {
		LOGGER.info("系统关闭");
		// 刷出日志缓存
		// TODO：JDK17类型转换
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		if (context != null) {
			context.stop();
		}
	}

}