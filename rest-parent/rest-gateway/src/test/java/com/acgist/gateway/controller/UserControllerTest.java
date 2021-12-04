package com.acgist.gateway.controller;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.CostUtils;
import com.acgist.boot.HTTPUtils;

public class UserControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);
	
	@Test
	public void testGetName() throws IOException {
		final String response = HTTPUtils.get(
			"http://localhost:8080/user/name",
			"{\"reqTime\":\"12312312312312\"}"
//			"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}"
		);
		LOGGER.info("响应：{}", response);
	}
	
	@Test
	public void testPostName() throws IOException {
		final String response = HTTPUtils.post(
			"http://localhost:8080/user/name",
//			"{\"reqTime\":\"12312312312312\"}"
			"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}"
			);
		LOGGER.info("响应：{}", response);
	}
	
	@Test
	public void testCost() {
		CostUtils.costed(10000, () -> {
			HTTPUtils.post(
				"http://localhost:8080/user/name",
				"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}"
			);
		});
	}
	
}
