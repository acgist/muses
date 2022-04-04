package com.acgist.rest.controller;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.HTTPUtils;
import com.acgist.boot.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserControllerTest {

	@Test
	public void testUser() {
		log.info("{}", HTTPUtils.get(
			"http://localhost:19298/user",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
	}
	
	@Test
	public void testException() {
		log.info("{}", HTTPUtils.get("http://localhost:9090/404"));
		log.info("{}", HTTPUtils.get("http://localhost:9090/user"));
		log.info("{}", HTTPUtils.post("http://localhost:9090/user", ""));
		log.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		log.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(),
			1000
		));
		log.info("{}", HTTPUtils.get("http://localhost:8888/rest/404"));
		log.info("{}", HTTPUtils.get("http://localhost:8888/rest/user"));
		log.info("{}", HTTPUtils.post("http://localhost:8888/rest/user", ""));
		log.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		log.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(),
			1000
		));
	}
	
}
