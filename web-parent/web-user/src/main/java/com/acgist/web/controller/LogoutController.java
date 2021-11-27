package com.acgist.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/logout")
	public String login() {
		LOGGER.info("用户登出");
		return "redirect:/";
	}

}
