package com.acgist.gateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acgist.boot.RsaUtils;
import com.acgist.gateway.service.impl.RsaService;

public class RsaServiceTest {

	private static RsaService rsaService;
	
	@BeforeAll
	private static final void init() {
		final Map<String, String> key = RsaUtils.buildKey();
		rsaService = new RsaService(key.get(RsaUtils.PUBLIC_KEY), key.get(RsaUtils.PRIVATE_KEY));
		rsaService.init();
	}

	@Test
	public void testEncrypt() {
		String content = "acgist";
		String value = rsaService.encrypt(content);
		System.out.println(value);
		assertEquals(content, rsaService.decrypt(value));
		content = content.repeat(100);
		value = rsaService.encrypt(content);
		System.out.println(value);
		assertEquals(content, rsaService.decrypt(value));
	}
	
	@Test
	public void testSignature() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);
		map.put("name", "acgist");
		final String signature = rsaService.signature(map);
		assertTrue(rsaService.verify(map, signature));
	}
	
}
