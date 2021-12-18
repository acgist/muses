package com.acgist.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.pojo.bean.User;
import com.acgist.rest.UserContext;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@PostMapping()
	public User index() {
		return UserContext.currentUser();
	}
	
}
