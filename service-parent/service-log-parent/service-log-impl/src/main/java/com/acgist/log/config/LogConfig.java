package com.acgist.log.config;

import java.io.IOException;

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
import org.springframework.util.ClassUtils;

import com.baomidou.mybatisplus.annotation.TableName;

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
		this.loadTableMapping();
	}

	/**
	 * 加载实体
	 */
	private void loadTableMapping() throws ClassNotFoundException, IOException {
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
