package com.acgist.pay.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.acgist.pay.IPayService;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class PayService implements IPayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PayService.class);
	
	@Override
	public boolean pay() {
		return false;
	}

	/**
	 * 需要添加注解：@EnableRetry
	 */
	@Retryable(value = NumberFormatException.class, maxAttempts = 10, backoff = @Backoff(delay = 1000, maxDelay = 5000, multiplier = 2))
	public void retry() {
		LOGGER.info("开始计算");
		Integer.parseInt("acgist");
	}
	
}
