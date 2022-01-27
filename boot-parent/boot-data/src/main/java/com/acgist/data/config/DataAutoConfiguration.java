package com.acgist.data.config;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.service.IdService;
import com.acgist.data.entity.SnowflakeGenerator;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * 数据自动配置
 * 
 * @author acgist
 */
@Configuration
public class DataAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataAutoConfiguration.class);

	@Autowired
	private IdService idService;

	@PostConstruct
	public void init() {
		LOGGER.info("注入ID生成策略：雪花算法");
		SnowflakeGenerator.init(this.idService);
	}
	
	@Bean
	@ConditionalOnClass(IdentifierGenerator.class)
	@ConditionalOnMissingBean
	public IdentifierGenerator idGenerator() {
	    return new IdentifierGenerator() {
			@Override
			public Number nextId(Object entity) {
				return idService.id();
			}
		};
	}
	
	@Bean
	@ConditionalOnClass(MybatisPlusInterceptor.class)
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	@Bean
	@ConditionalOnClass(MetaObjectHandler.class)
	@ConditionalOnMissingBean
	public MetaObjectHandler metaObjectHandler() {
		return new MetaObjectHandler() {
			@Override
			public void insertFill(MetaObject metaObject) {
				final Date date = new Date();
				if (metaObject.hasSetter("createDate")) {
					this.setFieldValByName("createDate", date, metaObject);
				}
				if (metaObject.hasSetter("modifyDate")) {
					this.setFieldValByName("modifyDate", date, metaObject);
				}
			}
			@Override
			public void updateFill(MetaObject metaObject) {
				if (metaObject.hasSetter("modifyDate")) {
					this.setFieldValByName("modifyDate", new Date(), metaObject);
				}
			}
		};
	}
	
}
