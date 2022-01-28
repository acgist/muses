package com.acgist.user.service;

import com.acgist.boot.pojo.User;
import com.acgist.user.pojo.dto.UserDto;

public interface IUserService {

	User findByName(String name);

	String findMemo(String name);
	
	void updateMemo(UserDto userDto);

}
