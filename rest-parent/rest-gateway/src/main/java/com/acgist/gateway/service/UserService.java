package com.acgist.gateway.service;

import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.acgist.boot.Message;
import com.acgist.boot.pojo.bean.User;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.request.SetMemoRequest;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;
import com.acgist.user.service.IUserService;

@Service
public class UserService {

	@DubboReference
	private IUserService userService;
	
	/**
	 * 获取用户备注
	 * 
	 * @param user 用户
	 * 
	 * @return 响应
	 */
	public Message<Map<String, Object>> getMemo(User user) {
		return GatewaySession.getInstance()
			.putResponse(UserEntity.PROPERTY_MEMO, this.userService.findMemo(user.getName()))
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
			.putResponse(UserEntity.PROPERTY_MEMO, this.userService.findMemo(userDto.getMemo()))
			.buildSuccess();
	}
	
}
