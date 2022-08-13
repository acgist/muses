package com.acgist.test.mssql;

import org.neo4j.driver.Driver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置事务
 * 
 * @author acgist
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@Import({DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
public class TransactionConfiguration {
	
	/**
	 * 配置PrimaryBean
	 * 
	 * @author acgist
	 */
	private static class PrimaryBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		}

		@Override
		public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
			final BeanDefinition transactionManager = registry.getBeanDefinition("transactionManager");
			transactionManager.setPrimary(true);
			log.info("设置primary事务：{}", transactionManager.getFactoryBeanName());
		}

	}
	
	@Bean(name = "primaryBeanPostProcessor")
	public BeanDefinitionRegistryPostProcessor primaryBeanPostProcessor() {
		return new PrimaryBeanPostProcessor();
	}

	@Bean("neo4jTransactionManager")
	public Neo4jTransactionManager neo4jTransactionManager(
		Driver driver,
		DatabaseSelectionProvider databaseNameProvider,
		ObjectProvider<TransactionManagerCustomizers> optionalCustomizers
	) {
		final Neo4jTransactionManager transactionManager = new Neo4jTransactionManager(driver, databaseNameProvider);
		optionalCustomizers.ifAvailable((customizer) -> customizer.customize(transactionManager));
		return transactionManager;
	}
	
}
