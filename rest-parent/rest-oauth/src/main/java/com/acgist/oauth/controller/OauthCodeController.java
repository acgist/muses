package com.acgist.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/code")
public class OauthCodeController {

	@GetMapping
	public String index(String code) {
		return code;
	}
	
}
