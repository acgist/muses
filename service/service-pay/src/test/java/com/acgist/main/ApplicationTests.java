package com.acgist.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void testRedis() {
		assertEquals("acgist", this.redisTemplate.opsForValue().get("acgist"));
	}

}
