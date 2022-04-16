package com.acgist.notify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.NotifyApplication;
import com.acgist.notify.gateway.dao.mapper.GatewayMapper;
import com.acgist.notify.gateway.model.entity.GatewayEntity;

@SpringBootTest(classes = NotifyApplication.class)
public class NotifyTest {

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
