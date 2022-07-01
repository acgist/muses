package com.acgist.test.mssql;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

/**
 * 配置事务
 * 
 * @author acgist
 */
@Configuration(proxyBeanMethods = false)
@Import({DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
//@Import({DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
public class TransactionConfiguration {
	
	/**
	 * 注意：如果在相同模块进行数据源注入不要添加注解`@ConditionalOnMissingBean`
	 */
	@Bean
	@Primary
	@ConditionalOnMissingBean(TransactionManager.class)
	public DataSourceTransactionManager transactionManager(Environment environment, DataSource dataSource, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		final DataSourceTransactionManager transactionManager = this.createTransactionManager(environment, dataSource);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}
	
	private DataSourceTransactionManager createTransactionManager(Environment environment, DataSource dataSource) {
		return
			environment.getProperty("spring.dao.exceptiontranslation.enable", Boolean.class, Boolean.TRUE)
			?
			new JdbcTransactionManager(dataSource)
			:
			new DataSourceTransactionManager(dataSource);
	}
	
}
