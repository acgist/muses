package com.acgist.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth2/code")
public class CodeController {

	@GetMapping
	@ResponseBody
	public String get(String code) {
		return code;
	}
	
	@PostMapping
	public String post(String code) {
		return code;
	}
	
}
