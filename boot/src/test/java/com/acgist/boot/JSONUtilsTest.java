package com.acgist.boot;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.pojo.bean.User;

public class JSONUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtilsTest.class);
	
	@Test
	public void test() {
		final User user = new User();
		user.setId(1L);
		user.setName("acgist");
		LOGGER.info("{}", user);
		final String json = JSONUtils.toJSON(user);
		LOGGER.info("{}", json);
//		LOGGER.info("{}", JSONUtils.toJava(json));
		LOGGER.info("{}", JSONUtils.toJava(json, User.class));
//		final User jsonUser = JSONUtils.toJava(json);
//		LOGGER.info("{}", jsonUser);
	}
	
}
