package com.acgist.boot;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapUtilsTest.class);
	
	@Test
	public void testToUrlQuery() {
		LOGGER.info("{}", MapUtils.toUrlQuery(null));
		LOGGER.info("{}", MapUtils.toUrlQuery(Map.of()));
		LOGGER.info("{}", MapUtils.toUrlQuery(Map.of("", "")));
		LOGGER.info("{}", MapUtils.toUrlQuery(Map.of("1", "2", "3", "4")));
	}
	
}
