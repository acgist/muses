package com.acgist;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.service.UserService;

@SpringBootTest(classes = Application.class)
public class ApplicationTest {
	
	@Autowired
	private UserService userService;

	@Test
	public void testService() {
		assertFalse(this.userService.findAll().isEmpty());
	}
	
}
