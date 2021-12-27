package com.acgist.user.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.UserApplication;
import com.acgist.user.pojo.dto.UserDto;

@SpringBootTest(classes = UserApplication.class)
public class UserServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
	
	@Autowired
	private IUserService userService;
	
	@Test
	public void testFindByName() {
		LOGGER.info("{}", this.userService.findByName("123"));
		LOGGER.info("{}", this.userService.findByName("test"));
		LOGGER.info("{}", this.userService.findByName("acgist"));
	}
	
	@Test
	public void testUpdate() {
		final UserDto userDto = new UserDto();
		userDto.setName("acgist");
		userDto.setMemo("测试" + System.currentTimeMillis());
		this.userService.updateMemo(userDto);
		userDto.setName("123");
		userDto.setMemo("测试" + System.currentTimeMillis());
		this.userService.updateMemo(userDto);
	}
	
}
