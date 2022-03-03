package com.acgist.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.gateway.dao.mapper.GatewayMapper;
import com.acgist.gateway.data.entity.GatewayEntity;
import com.acgist.main.GatewayApplication;

@SpringBootTest(classes = GatewayApplication.class)
public class GatewayTest {

	@Autowired
	private GatewayMapper gatewayMapper;
	
	@Test
	public void testSave() {
		final GatewayEntity gateway = new GatewayEntity();
		gateway.setId(1L);
		gateway.setRequest("test");
		this.gatewayMapper.insert(gateway);
		gateway.setId(2L);
		this.gatewayMapper.insert(gateway);
	}
	
}
