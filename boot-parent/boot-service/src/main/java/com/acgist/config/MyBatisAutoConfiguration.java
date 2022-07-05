package com.acgist.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.acgist.boot.service.IdService;
import com.acgist.model.entity.BootEntity;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis自动配置
 * 
 * @author acgist
 */
@MapperScan("com.acgist.**.dao.mapper")
@EntityScan("com.acgist.**.model.entity")
@Configuration
@ConditionalOnClass(MybatisPlusAutoConfiguration.class)
@EnableTransactionManagement
public class MyBatisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public IdentifierGenerator idGenerator(@Autowired IdService idService) {
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
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public MetaObjectHandler metaObjectHandler(@Autowired IdService idService) {
		return new MetaObjectHandler() {
			@Override
			public void insertFill(MetaObject metaObject) {
				final LocalDateTime now = LocalDateTime.now();
				this.setFieldValByName(BootEntity.PROPERTY_CREATE_DATE, now, metaObject);
				this.setFieldValByName(BootEntity.PROPERTY_MODIFY_DATE, now, metaObject);
			}
			@Override
			public void updateFill(MetaObject metaObject) {
				final LocalDateTime now = LocalDateTime.now();
				this.setFieldValByName(BootEntity.PROPERTY_MODIFY_DATE, now, metaObject);
			}
		};
	}

}
