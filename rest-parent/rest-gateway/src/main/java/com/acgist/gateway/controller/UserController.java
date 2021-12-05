package com.acgist.gateway.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.Message;
import com.acgist.boot.pojo.bean.User;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayBody;
import com.acgist.gateway.request.GetMemoRequest;
import com.acgist.gateway.request.SetMemoRequest;
import com.acgist.rest.config.CurrentUser;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;
import com.acgist.user.service.IUserService;

/**
 * 用户
 * 
 * @author acgist
 */
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

	@DubboReference
	private IUserService userService;
	
	@GetMapping("/memo")
	public Message<Map<String, Object>> memo(@CurrentUser User user, @Valid @GatewayBody GetMemoRequest request) {
		return GatewaySession.getInstance()
			.putResponse(UserEntity.PROPERTY_MEMO, this.userService.findMemo(user.getName()))
			.buildSuccess();
	}
	
	@PostMapping("/memo")
	public Message<Map<String, Object>> memo(@CurrentUser User user, @Valid @GatewayBody SetMemoRequest request) {
		final UserDto userDto = new UserDto();
		userDto.setName(user.getName());
		userDto.setMemo(request.getMemo());
		this.userService.updateMemo(userDto);
		return GatewaySession.getInstance()
			.putResponse(UserEntity.PROPERTY_MEMO, this.userService.findMemo(userDto.getMemo()))
			.buildSuccess();
	}
	
}
