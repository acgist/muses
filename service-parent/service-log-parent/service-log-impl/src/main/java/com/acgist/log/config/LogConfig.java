package com.acgist.log.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ClassUtils;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.log.canal.json.CanalBooleanDeserializer;
import com.acgist.log.canal.json.CanalLocalDateTimeDeserializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MappingConfig.class)
public class LogConfig {

	@Value("${system.log.base.package:com.acgist.**.entity}")
	private String basePackage;

	@Autowired
	private MappingConfig mappingConfig;

	@PostConstruct
	public void init() throws ClassNotFoundException, IOException {
		this.loadJSONCanalModule();
		this.loadTableMapping();
	}
	
	@Scheduled(cron =  "0 0 2 * * ?")
	public void scheduled() throws ClassNotFoundException, IOException {
		// 存在长时间运行对象被回收情况：错开零点高峰
		log.info("每日任务加载日志配置");
		this.init();
	}

	/**
	 * 加载JSON模块
	 */
	private void loadJSONCanalModule() {
		final SimpleModule module = new SimpleModule("CanalModule");
		module.addDeserializer(Boolean.class, new CanalBooleanDeserializer());
		module.addDeserializer(LocalDateTime.class, new CanalLocalDateTimeDeserializer());
		JSONUtils.registerModule(module);
	}

	/**
	 * 加载数据库表
	 */
	private void loadTableMapping() throws ClassNotFoundException, IOException {
		if(this.mappingConfig.getTables() == null) {
			this.mappingConfig.setTables(new ArrayList<>());
		}
		if(this.mappingConfig.getMapping() == null) {
			this.mappingConfig.setMapping(new HashMap<>());
		}
		final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		final String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
			ClassUtils.convertClassNameToResourcePath(this.basePackage) +
			"/**/*.class";
		final Resource[] resources = resourcePatternResolver.getResources(pattern);
		final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		for (Resource resource : resources) {
			final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
			final Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
			final TableName tableName = clazz.getAnnotation(TableName.class);
			if (tableName != null && this.mappingConfig.logTable(tableName.value())) {
				log.info("加载数据库表：{}", tableName);
				this.mappingConfig.loadEntity(tableName, clazz);
			} else {
				log.info("忽略数据库表：{}", tableName);
			}
		}
		log.info("加载实体数量：{}", this.mappingConfig.getMapping().size());
	}

}
