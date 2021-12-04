package com.acgist.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.CostUtils;
import com.acgist.boot.HTTPUtils;

public class UserControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);
	
	@Test
	public void testGetMemo() throws IOException {
		final String response = HTTPUtils.get(
			"http://localhost:8080/user/memo",
			"{\"reqTime\":\"12312312312312\"}",
//			"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}",
			Map.of("current-user", "acgist"),
			10000
		);
		LOGGER.info("响应：{}", response);
	}
	
	@Test
	public void testSetMemo() throws IOException {
		final String response = HTTPUtils.post(
			"http://localhost:8080/user/memo",
//			"{\"reqTime\":\"12312312312312\"}",
			"{\"memo\":\"acgist\",\"reqTime\":\"12312312312312\"}",
			Map.of("current-user", "acgist"),
			10000
		);
		LOGGER.info("响应：{}", response);
	}
	
	@Test
	public void testCost() {
//		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//		context.stop();
		CostUtils.costed(10000, () -> {
			HTTPUtils.get(
				"http://localhost:8080/user/memo",
				"{\"reqTime\":\"12312312312312\"}",
//				"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}",
				Map.of("current-user", "acgist"),
				10000
			);
//			HTTPUtils.post(
//				"http://localhost:8080/user/memo",
//				"{\"memo\":\"acgist\",\"reqTime\":\"12312312312312\"}",
//				Map.of("current-user", "acgist"),
//				10000
//			);
		});
	}
	
}
