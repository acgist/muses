package com.acgist.test.service.impl;

import org.springframework.stereotype.Service;

import com.acgist.service.impl.BootServiceImpl;
import com.acgist.test.mysql.mapper.UserMapper;
import com.acgist.test.service.IUserService;
import com.acgist.user.model.entity.UserEntity;

@Service
public class UserServiceImpl extends BootServiceImpl<UserMapper, UserEntity> implements IUserService {

}
