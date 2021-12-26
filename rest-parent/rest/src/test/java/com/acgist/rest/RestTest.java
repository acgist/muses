package com.acgist.rest;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.HTTPUtils;
import com.acgist.boot.pojo.bean.User;

public class RestTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTest.class);
	
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
