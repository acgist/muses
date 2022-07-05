package com.acgist.boot.utils;

import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLUtilsTest {

	@Test
	public void testToQuery() {
		log.info("{}", URLUtils.toQuery(null));
		log.info("{}", URLUtils.toQuery(Map.of()));
		log.info("{}", URLUtils.toQuery(Map.of("", "")));
		log.info("{}", URLUtils.toQuery(Map.of("1", "2", "3", "4")));
		log.info("{}", URLUtils.toQuery(Map.of("1", "2", "3", "测试")));
	}
	
}
