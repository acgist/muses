package com.acgist.data.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.service.IdService;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis自动配置
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnClass(value = {
	MetaObjectHandler.class,
	IdentifierGenerator.class,
	MybatisPlusInterceptor.class
})
public class MyBatisAutoConfiguration {

	@Autowired
	private IdService idService;
	
	@Bean
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
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	@Bean
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
