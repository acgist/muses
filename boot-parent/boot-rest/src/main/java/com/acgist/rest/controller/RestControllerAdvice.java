package com.acgist.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.boot.model.Message;
import com.acgist.boot.utils.ErrorUtils;

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
	
//	统一使用全局异常处理
//	@ExceptionHandler(BindException.class)
//	public void bindException(Exception e, HttpServletRequest request, HttpServletResponse response) {
//	}
	
}
