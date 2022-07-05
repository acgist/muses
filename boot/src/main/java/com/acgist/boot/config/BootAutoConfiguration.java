package com.acgist.boot.config;

import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;

import com.acgist.boot.service.FreemarkerService;
import com.acgist.boot.service.RsaService;
import com.acgist.boot.service.impl.FreemarkerServiceImpl;
import com.acgist.boot.service.impl.RsaServiceImpl;
import com.acgist.boot.utils.FileUtils;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.boot.utils.SpringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局自动配置
 * 
 * @author acgist
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableAsync
@Configuration
@Import({
	TaskExecutionAutoConfiguration.class,
	TaskSchedulingAutoConfiguration.class
})
public class BootAutoConfiguration {

	/**
	 * 序列化类型
	 * 
	 * @author acgist
	 */
	public enum SerializerType {
		
		// JDK
		JDK,
		// JACKSON
		JACKSON;
		
	}

	/**
	 * 服务名称
	 */
	@Value("${spring.application.name:muses}")
	private String name;
	/**
	 * 序列化类型：默认使用JDK序列化
	 * 
	 * Jackson不支持没有默认函数的对象：JWT Token授权信息
	 */
	@Value("${system.serializer.type:jdk}")
	private String serializerType;
	
	@Autowired
	private ApplicationContext context;
	
	@Bean
	@ConditionalOnMissingBean
	public RsaService rsaService() {
		return new RsaServiceImpl();
	}
	
	@Bean
	@Primary
	@ConditionalOnMissingBean
	public ObjectMapper objectMapper() {
		return JSONUtils.buildWebMapper();
	}
	
	@Bean
	@ConditionalOnClass(freemarker.template.Configuration.class)
	@ConditionalOnMissingBean
	public FreemarkerService freemarkerService() {
		return new FreemarkerServiceImpl();
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
	public TaskExecutor taskExecutor(TaskExecutorBuilder builder) {
		return builder.build();
	}
	
	@Bean
	@Primary
	@ConditionalOnMissingBean
	public TaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
		return builder.build();
	}

	@PostConstruct
	public void init() {
		SpringUtils.setContext(this.context);
		final var runtime = Runtime.getRuntime();
		final var bean = ManagementFactory.getRuntimeMXBean();
		final String freeMemory = FileUtils.formatSize(runtime.freeMemory());
		final String totalMemory = FileUtils.formatSize(runtime.totalMemory());
		final String maxMemory = FileUtils.formatSize(runtime.maxMemory());
		log.info("操作系统名称：{}", System.getProperty("os.name"));
		log.info("操作系统架构：{}", System.getProperty("os.arch"));
		log.info("操作系统版本：{}", System.getProperty("os.version"));
		log.info("操作系统可用处理器数量：{}", runtime.availableProcessors());
		log.info("Java版本：{}", System.getProperty("java.version"));
		log.info("Java主目录：{}", System.getProperty("java.home"));
		log.info("Java库目录：{}", System.getProperty("java.library.path"));
		log.info("ClassPath：{}", System.getProperty("java.class.path"));
		log.info("虚拟机名称：{}", System.getProperty("java.vm.name"));
		log.info("虚拟机参数：{}", bean.getInputArguments().stream().collect(Collectors.joining(" ")));
		log.info("虚拟机空闲内存：{}", freeMemory);
		log.info("虚拟机已用内存：{}", totalMemory);
		log.info("虚拟机最大内存：{}", maxMemory);
		log.info("工作目录：{}", System.getProperty("user.dir"));
		log.info("用户目录：{}", System.getProperty("user.home"));
		log.info("临时目录：{}", System.getProperty("java.io.tmpdir"));
		log.info("文件编码：{}", System.getProperty("file.encoding"));
		SpringUtils.getBeanByType(TaskExecutor.class).forEach((k, v) -> {
			log.info("系统任务线程池：{}-{}", k, v);
		});
		SpringUtils.getBeanByType(TaskScheduler.class).forEach((k, v) -> {
			log.info("系统定时任务线程池：{}-{}", k, v);
		});
	}
	
	@PreDestroy
	public void destroy() {
		log.info("系统关闭：{}", this.name);
		// 刷出日志缓存
		final ILoggerFactory factory = LoggerFactory.getILoggerFactory();
		if (factory instanceof LoggerContext context) {
			context.stop();
		}
		// 定时强制关机
		final Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 强制关机：无效
//				System.exit(0);
				// 强制关机
				Runtime.getRuntime().halt(0);
			}
		}, 5000);
	}

}