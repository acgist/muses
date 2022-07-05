package com.acgist.boot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;

public class ExceptionUtilsTest {

	@Test
	public void testRoot() {
		assertEquals(Exception.class, ExceptionUtils.root(new Exception("测试")).getClass());
		assertEquals(Exception.class, ExceptionUtils.root(new Exception("测试", new RuntimeException(new IllegalArgumentException("测试")))).getClass());
		assertEquals(MessageCodeException.class, ExceptionUtils.root(new Exception("测试", new MessageCodeException(MessageCode.CODE_0000, new IllegalArgumentException("测试")))).getClass());
		assertNotEquals(RuntimeException.class, ExceptionUtils.root(new Exception("测试")).getClass());
	}
	
}
