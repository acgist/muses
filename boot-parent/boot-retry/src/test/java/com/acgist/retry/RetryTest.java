package com.acgist.retry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.retry.RetryApplication.Service;

@SpringBootTest(classes = RetryApplication.class)
public class RetryTest {

	@Autowired
	private Service service;
	
	@Test
	public void testRetry() {
		this.service.retry();
	}
	
}
