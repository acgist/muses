package com.acgist.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.model.Message;
import com.acgist.oauth2.service.SmsService;

@RestController
@RequestMapping("/oauth2")
public class CodeSmsController {
	
	@Autowired
	private SmsService smsService;
	
	@GetMapping("/code.sms")
	public Message<String> sms(String mobile) {
		if(this.smsService.send(mobile)) {
			return Message.success();
		} else {
			return Message.fail();
		}
	}
	
}
