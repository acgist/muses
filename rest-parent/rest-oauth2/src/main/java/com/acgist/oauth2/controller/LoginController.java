package com.acgist.oauth2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String get(String message, Model model, HttpSession session) {
		model.addAttribute("message", message);
		return "/login";
	}
	
}
