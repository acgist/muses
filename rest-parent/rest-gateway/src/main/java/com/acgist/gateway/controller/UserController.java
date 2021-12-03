package com.acgist.gateway.controller;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.Message;
import com.acgist.gateway.request.GatewayBody;
import com.acgist.gateway.request.GetNameRequest;
import com.acgist.gateway.request.SetNameRequest;

/**
 * 用户
 * 
 * @author acgist
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/name")
	public Message<Void> name(@GatewayBody @Valid GetNameRequest request) {
		System.out.println(request);
		return Message.success();
	}
	
	@PostMapping("/name")
	public Message<String> name(@GatewayBody @Valid SetNameRequest request) {
		System.out.println(request);
		return Message.success(request.getName());
	}
	
}
