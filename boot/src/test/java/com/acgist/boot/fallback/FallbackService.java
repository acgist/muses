package com.acgist.boot.fallback;

import org.springframework.stereotype.Service;

import com.acgist.boot.config.Fallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FallbackService {

	@Fallback(method = "fallback", throwThrowable = false)
//	@Fallback(method = "fallback", throwThrowable = false, methodExecuteThrowable = MessageCodeException.class)
	public Integer execute() {
		return 1 / 0;
	}
	
	public void fallback() {
		log.info("执行方法：fallback");
	}
	
	@Fallback(method = "fallback")
	public Integer execute(String name) {
		return 1 / 0;
	}
	
	public void fallback(String name) {
		log.info("执行方法：fallback-args：{}", name);
	}
	
}
