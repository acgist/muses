package com.acgist.boot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtilsTest {
	
	@Test
	public void testStartsWidthIgnoreCase() {
		CostUtils.costed(100000, () -> StringUtils.startsWidthIgnoreCase("123456789", "1234567890"));
		CostUtils.costed(100000, () -> StringUtils.startsWidthIgnoreCase("1234567890abcdefghijklmnopqrstuvwxyz", "1234567890"));
	}
	
	@Test
	public void testSplit() {
		String[] array = StringUtils.split("", ":", true);
		assertEquals(1, array.length);
		log.info("{}", String.join("-", array));
		array = StringUtils.split("1", ":", true);
		assertEquals(1, array.length);
		log.info("{}", String.join("-", array));
		array = StringUtils.split("a:b::", ":", true);
		assertEquals(4, array.length);
		log.info("{}", String.join("-", array));
		array = StringUtils.split("a:b::", ":", false);
		assertEquals(4, array.length);
		log.info("{}", String.join("-", array));
		array = StringUtils.split(":a:b::1", ":", true);
		assertEquals(5, array.length);
		log.info("{}", String.join("-", array));
		array = StringUtils.split(":a:b::1", ":", false);
		assertEquals(5, array.length);
		log.info("{}", String.join("-", array));
	}
	
	@Test
	public void testCosted() {
		CostUtils.costed(100000, () -> "a:b::".split(":"));
		CostUtils.costed(100000, () -> StringUtils.split("a:b::", ":", true));
		CostUtils.costed(100000, () -> StringUtils.split("a:b::", ":", false));
	}
	
}
