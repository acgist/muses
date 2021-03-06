package com.acgist.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.UserApplication;
import com.acgist.user.api.IUserService;
import com.acgist.user.dao.mapper.UserMapper;
import com.acgist.user.model.dto.UserDto;
import com.acgist.user.model.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = UserApplication.class)
public class UserServiceTest {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private IUserService userService;
	
	@Test
	public void testSelectByName() {
		log.info("{}", this.userService.selectByName("root"));
		log.info("{}", this.userService.selectByName("test"));
		log.info("{}", this.userService.selectByName("acgist"));
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
