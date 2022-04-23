package com.acgist.boot.utils;

import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapUtilsTest {

	@Test
	public void testToUrlQuery() {
		log.info("{}", MapUtils.toUrlQuery(null));
		log.info("{}", MapUtils.toUrlQuery(Map.of()));
		log.info("{}", MapUtils.toUrlQuery(Map.of("", "")));
		log.info("{}", MapUtils.toUrlQuery(Map.of("1", "2", "3", "4")));
		log.info("{}", MapUtils.toUrlQuery(Map.of("1", "2", "3", "测试")));
	}
	
}
