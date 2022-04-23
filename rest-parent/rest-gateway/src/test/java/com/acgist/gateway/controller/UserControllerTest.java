package com.acgist.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.utils.CostUtils;
import com.acgist.boot.utils.HTTPUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserControllerTest {

	@Test
	public void testGetMemo() throws IOException {
		final String response = HTTPUtils.get(
			"http://localhost:19710/user/memo",
			"{\"reqTime\":\"12312312312312\"}",
//			"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}",
			Map.of("current-user", "{\"name\":\"acgist\"}"),
			10000
		);
		log.info("响应：{}", response);
	}
	
	@Test
	public void testSetMemo() throws IOException {
		final String response = HTTPUtils.post(
			"http://localhost:19977/user/memo",
			"{\"reqTime\":\"12312312312312\"}",
//			"{\"memo\":\"acgist\",\"reqTime\":\"12312312312312\"}",
			Map.of("current-user", "{\"name\":\"acgist\"}"),
			10000
		);
		log.info("响应：{}", response);
	}
	
	@Test
	public void testCosted() {
//		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//		context.stop();
		CostUtils.costed(10000, () -> {
			HTTPUtils.get(
				"http://localhost:9091/user/memo",
				"{\"reqTime\":\"12312312312312\"}",
//				"{\"name\":\"acgist\",\"reqTime\":\"12312312312312\"}",
				Map.of("current-user", "acgist"),
				10000
			);
//			HTTPUtils.post(
//				"http://localhost:9091/user/memo",
//				"{\"memo\":\"acgist\",\"reqTime\":\"12312312312312\"}",
//				Map.of("current-user", "acgist"),
//				10000
//			);
		});
	}
	
}
