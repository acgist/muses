package com.acgist.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 统一异常处理
 * 
 * @author acgist
 */
@ControllerAdvice
public class WebControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebControllerAdvice.class);
	
	@Primary
	@ExceptionHandler(Exception.class)
	public String exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.error("系统异常：{}", response.getStatus(), e);
		return "/error";
	}
	
}
