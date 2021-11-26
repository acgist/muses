package com.acgist.pay.repository;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.Application;
import com.acgist.pay.entity.OrderEntity;

@SpringBootTest(classes = Application.class)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;
	
	@Test
	public void testSave() {
		OrderEntity entity = new OrderEntity();
//		entity.setId(1001L);
		entity.setName("acgist");
		this.orderRepository.save(entity);
	}
	
	@Test
	public void testInsert() {
		OrderEntity entity = new OrderEntity();
		entity.setName("acgist");
		// 只会自动生成ID不会生成时间
		entity.setCreateDate(new Date());
		entity.setModifyDate(new Date());
		this.orderRepository.insert(entity);
	}
	
}
