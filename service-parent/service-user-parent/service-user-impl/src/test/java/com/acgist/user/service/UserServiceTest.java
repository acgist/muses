package com.acgist.user.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.UserApplication;
import com.acgist.user.api.IUserService;
import com.acgist.user.dao.mapper.UserMapper;
import com.acgist.user.model.dto.UserDto;
import com.acgist.user.model.entity.UserEntity;

@SpringBootTest(classes = UserApplication.class)
public class UserServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private IUserService userService;
	
	@Test
	public void testFindByName() {
		LOGGER.info("{}", this.userService.findByName("root"));
		LOGGER.info("{}", this.userService.findByName("test"));
		LOGGER.info("{}", this.userService.findByName("acgist"));
	}
	
	@Test
	public void testSave() {
		final UserEntity user = new UserEntity();
		user.setName("test");
		user.setPassword("test");
		this.userMapper.insert(user);
	}
	
	@Test
	public void testUpdate() {
		final UserDto userDto = new UserDto();
		userDto.setName("test");
		userDto.setMemo("测试" + System.currentTimeMillis());
		this.userService.updateMemo(userDto);
		userDto.setName("123");
		userDto.setMemo("测试" + System.currentTimeMillis());
		this.userService.updateMemo(userDto);
	}
	
}
