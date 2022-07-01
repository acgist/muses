package com.acgist.user.api.impl;

import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.acgist.boot.model.User;
import com.acgist.user.api.IUserService;
import com.acgist.user.dao.mapper.PathMapper;
import com.acgist.user.dao.mapper.RoleMapper;
import com.acgist.user.dao.mapper.UserMapper;
import com.acgist.user.model.dto.UserDto;
import com.acgist.user.model.entity.PathEntity;
import com.acgist.user.model.entity.RoleEntity;
import com.acgist.user.model.entity.UserEntity;
import com.acgist.user.model.mapstruct.UserMapstruct;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PathMapper pathMapper;
	@Autowired
	private UserMapstruct userMapstruct;
	
	@Override
	@Cacheable(cacheNames = "user")
	public User selectByName(String name) {
		final UserEntity entity = this.userMapper.selectByName(name);
		if(entity == null) {
			return null;
		}
		final User user = this.userMapstruct.toUser(entity);
		user.setRoles(this.roleMapper.selectByUser(name).stream().filter(RoleEntity::getEnabled).map(RoleEntity::getName).collect(Collectors.toSet()));
		user.setPaths(this.pathMapper.selectByUser(name).stream().filter(PathEntity::getEnabled).map(PathEntity::getPath).collect(Collectors.toSet()));
		return user;
	}

	@Override
	public String selectMemo(String name) {
		final UserEntity entity = this.userMapper.selectByName(name);
		if(entity == null) {
			return null;
		}
		return entity.getMemo();
	}
	
	@Override
	@CacheEvict(cacheNames = "user", key = "#userDto.name")
	public void updateMemo(UserDto userDto) {
		final UserEntity entity = this.userMapper.selectByName(userDto.getName());
		if(entity == null) {
			return;
		}
		entity.setMemo(userDto.getMemo());
		this.userMapper.updateById(entity);
	}

}
