package com.acgist.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonTest.class);
	
	@Test
	public void testLogger() {
		LOGGER.info("测试");
		assertNotNull(LOGGER);
	}
	
}
