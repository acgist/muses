package com.acgist.boot.fallback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.boot.BootApplication;

@SpringBootTest(classes = BootApplication.class)
public class FallbackTest {

	@Autowired
	private FallbackService fallbackService;

	@Test
	public void testFallback() {
		this.fallbackService.execute();
		this.fallbackService.execute("name");
	}
	
}
