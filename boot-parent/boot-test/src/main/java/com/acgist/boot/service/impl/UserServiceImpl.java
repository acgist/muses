package com.acgist.boot.service.impl;

import org.springframework.stereotype.Service;

import com.acgist.boot.dao.mapper.UserMapper;
import com.acgist.boot.dao.repository.UserRepository;
import com.acgist.boot.service.UserService;
import com.acgist.user.pojo.entity.UserEntity;

@Service
public class UserServiceImpl extends BootServiceImpl<UserEntity> implements UserService {

	public UserServiceImpl(UserMapper mapper, UserRepository repository) {
		super(mapper, repository);
	}

}
