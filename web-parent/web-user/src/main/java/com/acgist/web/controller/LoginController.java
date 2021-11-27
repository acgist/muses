package com.acgist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "/login/index";
	}

	@PostMapping("/login")
	public String login(String username, String password) {
		return "redirect:/user";
	}

}
