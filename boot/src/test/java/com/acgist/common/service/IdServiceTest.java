package com.acgist.common.service;

import org.junit.jupiter.api.Test;

import com.acgist.boot.CostUtils;
import com.acgist.boot.service.IdService;

public class IdServiceTest {

	@Test
	public void testId() {
		final IdService idService = new IdService();
		CostUtils.costed(100000, () -> {
			idService.id();
		});
	}
	
}
