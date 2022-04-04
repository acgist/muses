package com.acgist.user.dto;

import org.junit.jupiter.api.Test;

import com.acgist.user.model.dto.UserDto;
import com.acgist.user.model.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDtoTest {

	@Test
	public void toEntity() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("acgist");
		log.info("{}", entity);
		UserDto dto = new UserDto();
		dto.copy(entity);
		log.info("{}", dto);
	}
	
}
