package com.acgist.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.www.ErrorUtils;

/**
 * 统一异常处理
 * 
 * @author acgist
 */
@ControllerAdvice
public class WebControllerAdvice {

	@ExceptionHandler(Exception.class)
	public String exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(ErrorUtils.THROWABLE_SPRINTBOOT, e);
		return "forward:" + ErrorUtils.ERROR_PATH;
	}
	
}
