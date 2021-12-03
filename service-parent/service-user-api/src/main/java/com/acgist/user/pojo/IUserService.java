package com.acgist.user.pojo;

import com.acgist.boot.pojo.bean.User;

public interface IUserService {

	User findByName(String name);
	
}
