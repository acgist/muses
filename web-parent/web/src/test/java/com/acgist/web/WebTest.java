package com.acgist.web;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.HTTPUtils;

public class WebTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebTest.class);
	
	@Test
	public void testException() {
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/404"));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/exception/know"));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/exception/unknow"));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/exception/post"));
		LOGGER.info("{}", HTTPUtils.post("http://localhost:8888/exception/post", ""));
		LOGGER.info("{}", HTTPUtils.post("http://localhost:8888/exception/know", ""));
		LOGGER.info("{}", HTTPUtils.post("http://localhost:8888/exception/know", Map.of()));
	}
	
}
