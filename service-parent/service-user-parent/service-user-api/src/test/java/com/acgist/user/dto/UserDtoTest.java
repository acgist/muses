package com.acgist.user.dto;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.user.model.dto.UserDto;
import com.acgist.user.model.entity.UserEntity;

public class UserDtoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDtoTest.class);
	
	@Test
	public void toEntity() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("acgist");
		LOGGER.info("{}", entity);
		UserDto dto = new UserDto();
		dto.copy(entity);
		LOGGER.info("{}", dto);
	}
	
}
