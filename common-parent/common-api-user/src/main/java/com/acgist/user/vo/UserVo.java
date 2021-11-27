package com.acgist.user.vo;

import com.acgist.data.EntityConver;
import com.acgist.user.entity.UserEntity;

public class UserVo extends EntityConver<UserEntity> {

	private static final long serialVersionUID = 1L;

	private String name;
	
	@Override
	public UserEntity toEntity() {
		final UserEntity user = new UserEntity();
		return this.copy(this, user);
	}

	@Override
	public EntityConver<UserEntity> ofEntity(UserEntity entity) {
		return this.copy(entity, this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
