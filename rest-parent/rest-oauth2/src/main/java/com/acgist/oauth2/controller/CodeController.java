package com.acgist.oauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class CodeController {

	@GetMapping("/code")
	public String get(String code) {
		return code;
	}
	
}
