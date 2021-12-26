package com.acgist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.pojo.bean.MessageCode;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

	@GetMapping("/know")
	public String know() {
		throw MessageCodeException.of(MessageCode.CODE_1000, "异常测试");
	}
	
	@GetMapping("/unknow")
	public String unknow() {
		throw new RuntimeException("测试异常");
	}
	
	@PostMapping("/post")
	public void post() {
	}

}
