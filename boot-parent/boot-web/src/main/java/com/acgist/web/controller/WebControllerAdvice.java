package com.acgist.web.controller;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.www.utils.ErrorUtils;

/**
 * 统一异常处理
 * 
 * @author acgist
 */
@ControllerAdvice
public class WebControllerAdvice {

	/**
	 * 最大循环时间
	 */
	private static final int MAX_FORWARD_TIME = 50;
	/**
	 * 重置时间
	 */
	private static final int RESET_FORWARD_TIME = 1000;
	
	/**
	 * 当前循环时间
	 */
	private ThreadLocal<AtomicLong> index = new ThreadLocal<AtomicLong>();
	
	@ExceptionHandler(Exception.class)
	public String exception(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		final AtomicLong index = this.index.get();
		if(
			index == null ||
			System.currentTimeMillis() - index.get() > RESET_FORWARD_TIME
		) {
			this.index.set(new AtomicLong(System.currentTimeMillis()));
		} else if(System.currentTimeMillis() - index.get() > MAX_FORWARD_TIME) {
			this.index.remove();
			return "redirect:" + ErrorUtils.ERROR_PATH;
		}
		request.setAttribute(ErrorUtils.SPRINGBOOT_EXCEPTION, e);
		return "forward:" + ErrorUtils.ERROR_PATH;
	}
	
//	统一使用全局异常处理
//	@ExceptionHandler(BindException.class)
//	public void bindException(Exception e, HttpServletRequest request, HttpServletResponse response) {
//	}
	
}
