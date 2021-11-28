package com.acgist.resources.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@PostMapping()
	public Object index() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getPrincipal();
	}

	@PostMapping("/all")
	public Authentication all() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
}
