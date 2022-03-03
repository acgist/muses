package com.acgist.user.service.impl;

import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.acgist.boot.data.User;
import com.acgist.user.dao.mapper.PathMapper;
import com.acgist.user.dao.mapper.RoleMapper;
import com.acgist.user.dao.mapper.UserMapper;
import com.acgist.user.data.dto.UserDto;
import com.acgist.user.data.entity.PathEntity;
import com.acgist.user.data.entity.RoleEntity;
import com.acgist.user.data.entity.UserEntity;
import com.acgist.user.service.IUserService;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PathMapper pathMapper;
	
	@Override
	@Cacheable(cacheNames = "user")
	public User findByName(String name) {
		final UserEntity entity = this.userMapper.findByName(name);
		if(entity == null) {
			return null;
		}
		final User user = new User();
		user.copy(entity);
		user.setRoles(this.roleMapper.findByUser(name).stream().map(RoleEntity::getName).collect(Collectors.toSet()));
		user.setPaths(this.pathMapper.findByUser(name).stream().map(PathEntity::getPath).collect(Collectors.toSet()));
		return user;
	}

	@Override
	public String findMemo(String name) {
		final UserEntity entity = this.userMapper.findByName(name);
		if(entity == null) {
			return null;
		}
		return entity.getMemo();
	}
	
	@Override
	@CacheEvict(cacheNames = "user", key = "#userDto.name")
	public void updateMemo(UserDto userDto) {
		final UserEntity entity = this.userMapper.findByName(userDto.getName());
		if(entity == null) {
			return;
		}
		entity.setMemo(userDto.getMemo());
		this.userMapper.updateById(entity);
	}

}
