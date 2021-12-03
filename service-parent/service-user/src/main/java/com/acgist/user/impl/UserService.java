package com.acgist.user.impl;

import javax.transaction.Transactional;

import org.apache.dubbo.config.annotation.DubboService;

import com.acgist.boot.pojo.bean.User;
import com.acgist.user.pojo.IUserService;
import com.acgist.user.pojo.dto.UserDto;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class UserService implements IUserService {

	@Override
//	@Modifying
	@Transactional
	public User findByName(String name) {
		return null;
	}

	@Override
	@Transactional
	public void update(UserDto userDto) {
	}

}
