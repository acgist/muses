package com.acgist.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
@SpringBootApplication
public class CacheTest {

	@Autowired
	private CacheManager cacheManager;
	
	@Test
	public void testCacheManager() {
		this.cacheManager.getCache("acgist").put("acgist", "acgistww");
	}
	
}
