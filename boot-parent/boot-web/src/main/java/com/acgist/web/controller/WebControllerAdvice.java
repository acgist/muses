package com.acgist.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.boot.utils.ErrorUtils;

/**
 * 统一异常处理
 * 
 * @author acgist
 */
@ControllerAdvice
public class WebControllerAdvice {

	@ExceptionHandler(Exception.class)
	public String exception(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		final Integer errorIndex = ErrorUtils.getSystemErrorIndexAndIncrement(request);
		if(errorIndex > ErrorUtils.SYSTEM_ERROR_INDEX_MAX) {
			return "redirect:" + ErrorUtils.ERROR_PATH;
		}
		ErrorUtils.putSystemErrorException(request, e);
		return "forward:" + ErrorUtils.ERROR_PATH;
	}
	
//	统一使用全局异常处理
//	@ExceptionHandler(BindException.class)
//	public void bindException(Exception e, HttpServletRequest request, HttpServletResponse response) {
//	}
	
}
