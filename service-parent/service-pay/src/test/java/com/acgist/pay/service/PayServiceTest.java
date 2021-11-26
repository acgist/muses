package com.acgist.pay.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;

import com.acgist.main.PayApplication;
import com.acgist.pay.impl.PayService;

@EnableRetry
@SpringBootTest(classes = PayApplication.class)
public class PayServiceTest {

	@Autowired
	private PayService payService;
	
	@Test
	public void testRetry() {
		this.payService.retry();
	}
	
}
