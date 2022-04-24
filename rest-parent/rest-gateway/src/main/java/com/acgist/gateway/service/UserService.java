package com.acgist.gateway.service;

import java.util.Map;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.User;
import com.acgist.gateway.model.request.SetMemoRequest;

/**
 * 本地用户接口
 * 
 * @author acgist
 */
public interface UserService {

	/**
	 * 获取用户备注
	 * 
	 * @param user 用户
	 * 
	 * @return 响应
	 */
	Message<Map<String, Object>> getMemo(User user);
	
	/**
	 * 修改用户备注
	 * 
	 * @param user 用户
	 * @param request 请求
	 * 
	 * @return 响应
	 */
	Message<Map<String, Object>> setMemo(User user, SetMemoRequest request);
	
}
