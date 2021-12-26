package com.acgist.boot;

import org.junit.jupiter.api.Test;

public class HTTPUtilsTest {

	@Test
	public void testCosted() {
		CostUtils.costed(100, () -> {
			HTTPUtils.get("https://www.baidu.com");
			HTTPUtils.get("https://www.acgist.com");
		});
	}

}
