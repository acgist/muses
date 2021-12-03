package com.acgist.user.dto;

import org.junit.jupiter.api.Test;

import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;

public class UserDtoTest {

	@Test
	public void toEntity() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("acgist");
		System.out.println(entity);
		UserDto dto = new UserDto();
		dto.ofEntity(entity);
		System.out.println(dto);
		System.out.println(dto.toEntity());
	}
	
}
