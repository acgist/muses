package com.acgist.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.pojo.bean.User;
import com.acgist.rest.UserContext;

@RestController
public class OauthController {

	@GetMapping("/user")
	public User user() {
		return UserContext.currentUser();
	}
	
	/**
	 * 授权
	 */
	public void authorization(String method, String path) {
		// TODO:401
	}
	
	/**
	 * 认证
	 */
	public void authentication(String method, String path) {
		// TODO:401
	}
	
}
