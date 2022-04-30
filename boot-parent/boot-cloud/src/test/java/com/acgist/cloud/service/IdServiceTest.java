package com.acgist.cloud.service;

import org.junit.jupiter.api.Test;

import com.acgist.boot.service.IdService;
import com.acgist.boot.utils.CostUtils;
import com.acgist.cloud.service.impl.IdServiceImpl;

public class IdServiceTest {

	@Test
	public void testId() {
		final IdService idService = new IdServiceImpl();
		CostUtils.costed(100000, idService::id);
	}
	
}
