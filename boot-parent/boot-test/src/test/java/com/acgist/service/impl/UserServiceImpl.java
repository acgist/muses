package com.acgist.service.impl;

import org.springframework.stereotype.Service;

import com.acgist.mysql.mapper.UserMapper;
import com.acgist.service.IUserService;
import com.acgist.user.model.entity.UserEntity;

@Service
public class UserServiceImpl extends BootServiceImpl<UserMapper, UserEntity> implements IUserService {

}
