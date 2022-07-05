package com.acgist.boot.fallback;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		assertDoesNotThrow(() -> {
			this.fallbackService.execute();
		});
		assertThrows(ArithmeticException.class, () -> {
			this.fallbackService.execute("name");
		});
	}
	
}
