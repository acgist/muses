package com.acgist.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.user.pojo.entity.UserEntity;

@SpringBootTest(classes = ServiceApplication.class)
public class ServiceTest {
	
	@Autowired
	private UserService userService;

	@Test
	public void testService() {
		final List<UserEntity> list = this.userService.findAll();
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}
	
}
