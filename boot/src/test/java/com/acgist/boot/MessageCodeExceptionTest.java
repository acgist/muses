package com.acgist.boot;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MessageCodeExceptionTest {

	@Test
	public void testException() {
		assertTrue(MessageCodeException.root(new Throwable()) instanceof Throwable);
		assertTrue(MessageCodeException.root(new Throwable(MessageCodeException.of("测试"))) instanceof MessageCodeException);
		assertTrue(MessageCodeException.root(new Throwable(MessageCodeException.of(new Throwable()))) instanceof MessageCodeException);
	}
	
}
