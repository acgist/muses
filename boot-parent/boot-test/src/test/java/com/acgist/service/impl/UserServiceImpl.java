package com.acgist.service.impl;

import org.springframework.stereotype.Service;

import com.acgist.mysql.mapper.UserMapper;
import com.acgist.mysql.repository.UserRepository;
import com.acgist.service.UserService;
import com.acgist.user.pojo.entity.UserEntity;

@Service
public class UserServiceImpl extends BootServiceImpl<UserEntity> implements UserService {

	public UserServiceImpl(UserMapper mapper, UserRepository repository) {
		super(mapper, repository);
	}

}
