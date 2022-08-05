package com.acgist.boot.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanUtilsTest {

	@Test
	public void testCopy() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", "1234");
		map.put("name", 4321);
		map.put("enabled", false);
		map.put("date", "2012-12-12 12:12:12");
		final User user = new User();
		BeanUtils.copy(user, map, "id", "name", "date", "enabled");
		log.info("{}", JSONUtils.toJSON(user));
	}
	
}
