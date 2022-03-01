package com.acgist.user.service;

import com.acgist.boot.pojo.User;
import com.acgist.user.pojo.dto.UserDto;

/**
 * 用户服务
 * 
 * @author acgist
 */
public interface IUserService {

	/**
	 * 根据用户名称查询用户
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户
	 */
	User findByName(String name);

	/**
	 * 根据用户名称查询用户描述
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户描述
	 */
	String findMemo(String name);
	
	/**
	 * 更新用户描述
	 * 
	 * @param userDto 用户信息
	 */
	void updateMemo(UserDto userDto);

}
