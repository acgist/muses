package com.acgist.user.model.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.acgist.boot.model.User;
import com.acgist.user.model.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapstruct {
	
	@Mappings({
		@Mapping(target = "roles", ignore = true),
		@Mapping(target = "paths", ignore = true)
	})
	User toUser(UserEntity entity);

}
