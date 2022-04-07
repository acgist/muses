package com.acgist.retry.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

import lombok.extern.slf4j.Slf4j;

/**
 * 重试配置
 * 
 * @author acgist
 */
@Slf4j
@EnableRetry
@Configuration
public class RetryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RetryListener retryListener() {
		return new RetryListener() {
			
			@Override
			public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
				log.info("执行方法：{}-{}", context.getRetryCount(), callback);
				return true;
			}
			
			@Override
			public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				if(throwable != null) {
					log.error("重试异常：{}-{}", context.getRetryCount(), callback, throwable);
				} else {
					log.info("重试异常：{}-{}", context.getRetryCount(), callback);
				}
			}
			
			@Override
			public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				if(throwable != null) {
					log.error("重试完成：{}-{}", context.getRetryCount(), callback, throwable);
				} else {
					log.info("重试完成：{}-{}", context.getRetryCount(), callback);
				}
			}
			
		};
	}
	
}
