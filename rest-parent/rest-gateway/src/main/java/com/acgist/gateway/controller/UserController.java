package com.acgist.gateway.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.pojo.bean.Message;
import com.acgist.boot.pojo.bean.User;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.request.DeleteRequest;
import com.acgist.gateway.request.GetMemoRequest;
import com.acgist.gateway.request.SetMemoRequest;
import com.acgist.gateway.resolver.GatewayBody;
import com.acgist.gateway.service.UserService;
import com.acgist.rest.resolver.CurrentUser;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/memo")
	public Message<Map<String, Object>> memo(@CurrentUser User user, @Valid @GatewayBody GetMemoRequest request) {
		return this.userService.getMemo(user);
	}
	
//	@PutMapping("/memo")
	@PostMapping("/memo")
	public Message<Map<String, Object>> memo(@CurrentUser User user, @Valid @GatewayBody SetMemoRequest request) {
		return this.userService.setMemo(user, request);
	}

	@DeleteMapping("/delete")
	public Message<Map<String, Object>> delete(@Valid @GatewayBody DeleteRequest request) {
		// 忽略
		return GatewaySession.getInstance()
			.putResponse("id", request.getId())
			.buildSuccess();
	}
	
}
