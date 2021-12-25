package com.acgist.boot;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

	@Test
	public void testStartsWidthIgnoreCase() {
		CostUtils.costed(100000, () -> StringUtils.startsWidthIgnoreCase("123456789", "1234567890"));
		CostUtils.costed(100000, () -> StringUtils.startsWidthIgnoreCase("1234567890abcdefghijklmnopqrstuvwxyz", "1234567890"));
	}
	
}
