package com.acgist.boot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.acgist.boot.model.MessageCode;

public class ErrorUtilsTest {

	@Test
	public void testException() {
		ErrorUtils.register(MessageCode.CODE_1000, IllegalArgumentException.class);
		ErrorUtils.register(MessageCode.CODE_0000, Exception.class);
		assertEquals(MessageCode.CODE_0000, ErrorUtils.messageCode(100, new Exception()));
		assertEquals(MessageCode.CODE_1000, ErrorUtils.messageCode(100, new IllegalArgumentException()));
		assertNotEquals(MessageCode.CODE_0000, ErrorUtils.messageCode(100, new IllegalArgumentException()));
		assertEquals(MessageCode.CODE_0000, ErrorUtils.messageCode(100, new IllegalAccessException()));
	}
	
}
