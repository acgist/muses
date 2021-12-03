package com.acgist.user.pojo;

import com.acgist.boot.pojo.bean.User;
import com.acgist.user.pojo.dto.UserDto;

public interface IUserService {

	User findByName(String name);
	
	void update(UserDto userDto);
	
}
