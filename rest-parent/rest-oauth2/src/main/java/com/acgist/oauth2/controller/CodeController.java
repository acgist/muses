package com.acgist.oauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.model.Message;

@RestController
@RequestMapping("/oauth2")
public class CodeController {

	@GetMapping("/code")
	public Message<String> get(String code) {
		// 写出授权Code
		return Message.success(code);
	}
	
}
