package com.acgist.boot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acgist.boot.service.impl.RsaServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RsaServiceTest {

	private static RsaServiceImpl rsaService;
	
	@BeforeAll
	private static final void init() {
		rsaService = new RsaServiceImpl();
		rsaService.init();
	}

	@Test
	public void testEncrypt() {
		String content = "acgist";
		String value = rsaService.encrypt(content);
		log.info(value);
		assertEquals(content, rsaService.decrypt(value));
		content = content.repeat(100);
		value = rsaService.encrypt(content);
		log.info(value);
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
