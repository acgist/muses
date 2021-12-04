package com.acgist.gateway.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.Message;
import com.acgist.boot.pojo.bean.User;
import com.acgist.gateway.config.GatewayBody;
import com.acgist.gateway.request.GetMemoRequest;
import com.acgist.gateway.request.SetMemoRequest;
import com.acgist.rest.config.CurrentUser;
import com.acgist.user.pojo.dto.UserDto;
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
	public Message<String> memo(@CurrentUser User user, @Valid @GatewayBody GetMemoRequest request) {
		return Message.success(this.userService.findMemo(user.getName()));
	}
	
	@PostMapping("/memo")
	public Message<String> memo(@CurrentUser User user, @Valid @GatewayBody SetMemoRequest request) {
		final UserDto userDto = new UserDto();
		userDto.setName(user.getName());
		userDto.setMemo(request.getMemo());
		this.userService.updateMemo(userDto);
		return Message.success(request.getMemo());
	}
	
}
