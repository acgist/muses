package com.acgist.gateway.service.impl;

import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.User;
import com.acgist.gateway.model.GatewaySession;
import com.acgist.gateway.model.request.SetMemoRequest;
import com.acgist.user.api.IUserService;
import com.acgist.user.model.dto.UserDto;

@Service
public class UserService {

	@DubboReference
	private IUserService userService;
	
	private static final String PROPERTY_MEMO = "memo";
	
	/**
	 * 获取用户备注
	 * 
	 * @param user 用户
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> getMemo(User user) {
		return GatewaySession.getInstance()
			.putResponse(PROPERTY_MEMO, this.userService.findMemo(user.getName()))
			.buildSuccess();
	}
	
	/**
	 * 修改用户备注
	 * 
	 * @param user 用户
	 * @param request 请求
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> setMemo(User user, SetMemoRequest request) {
		final UserDto userDto = new UserDto();
		userDto.setName(user.getName());
		userDto.setMemo(request.getMemo());
		this.userService.updateMemo(userDto);
		return GatewaySession.getInstance()
			.putResponse(PROPERTY_MEMO, this.userService.findMemo(userDto.getMemo()))
			.buildSuccess();
	}
	
}
