package com.acgist.www.transfer;

import java.util.Map;
import java.util.WeakHashMap;

import org.junit.jupiter.api.Test;

import com.acgist.boot.utils.JSONUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferTest {

	@Getter
	@Setter
	public static class User {
		
		@Transfer(group = "name")
		private String age;
		@Transfer(group = "name")
		private String name;
		
	}
	
	@Test
	public void testTransfer() {
		final User user = new User();
		user.setAge("11");
		user.setName("acgist");
		log.info("{}", JSONUtils.toJSON(user));
	}
	
	final Map<String, Object> map = new WeakHashMap<>();
	
	@Test
	public void testWeak() {
		this.cache("key1", "1");
		this.cache("key2", "2");
		this.cache("key3", "3");
	}
	
	public void cache(String key, String name) {
		System.gc();
		System.runFinalization();
		this.map.put(new String(key), name);
//		this.map.put(key, name);
		log.info("{}", this.map.size());
		log.info("{}", this.map);
	}
	
	
}
