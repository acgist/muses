package com.acgist.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.boot.data.Message;
import com.acgist.www.ErrorUtils;

/**
 * 统一异常处理
 * 
 * @author acgist
 */
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

	@ExceptionHandler(Exception.class)
	public Message<String> exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		return ErrorUtils.message(e, request, response);
	}
	
}
