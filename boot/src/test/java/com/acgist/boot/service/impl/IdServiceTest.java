package com.acgist.boot.service.impl;

import org.junit.jupiter.api.Test;

import com.acgist.boot.utils.CostUtils;

public class IdServiceTest {

	@Test
	public void testId() {
		final IdService idService = new IdService();
		CostUtils.costed(100000, idService::id);
	}
	
}
