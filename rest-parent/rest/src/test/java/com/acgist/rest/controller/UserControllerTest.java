package com.acgist.rest.controller;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.HTTPUtils;
import com.acgist.boot.model.User;

public class UserControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);
	
	@Test
	public void testUser() {
		LOGGER.info("{}", HTTPUtils.get(
			"http://localhost:19298/user",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
	}
	
	@Test
	public void testException() {
		LOGGER.info("{}", HTTPUtils.get("http://localhost:9090/404"));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:9090/user"));
		LOGGER.info("{}", HTTPUtils.post("http://localhost:9090/user", ""));
		LOGGER.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		LOGGER.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(),
			1000
		));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/rest/404"));
		LOGGER.info("{}", HTTPUtils.get("http://localhost:8888/rest/user"));
		LOGGER.info("{}", HTTPUtils.post("http://localhost:8888/rest/user", ""));
		LOGGER.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		LOGGER.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(),
			1000
		));
	}
	
}
