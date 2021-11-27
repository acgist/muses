package com.acgist.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/login")
	public String login() {
		LOGGER.debug("登陆页面");
		return "/login/index";
	}

	@PostMapping("/login")
	public String login(String username, String password) {
		LOGGER.debug("开始登陆：{}-{}", username, password);
		return "redirect:/user";
	}

}
