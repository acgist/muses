package com.acgist.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.data.Message;
import com.acgist.boot.data.User;
import com.acgist.rest.UserContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户接口", description = "查询用户信息")
@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping()
	@Operation(summary = "查询当前用户", description = "查询当前登陆用户", tags = "用户接口")
	public User index() {
		return UserContext.currentUser();
	}
	
	@GetMapping
	@Operation(summary = "模拟查询用户", description = "模拟返回查询用户", tags = "用户接口")
	public Message<User> index(String name) {
		final User user = new User();
		user.setName(name);
		return Message.success(user);
	}
	
}
