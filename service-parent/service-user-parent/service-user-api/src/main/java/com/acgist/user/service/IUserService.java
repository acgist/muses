package com.acgist.user.service;

import com.acgist.boot.pojo.bean.User;
import com.acgist.user.pojo.dto.UserDto;

public interface IUserService {

	User findByName(String name);

	String findMemo(String name);
	
	void updateMemo(UserDto userDto);

}
