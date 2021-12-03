package com.acgist.rest.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.acgist.boot.Message;
import com.acgist.boot.MessageCode;
import com.acgist.boot.MessageCodeException;

/**
 * 系统异常
 * 
 * @author acgist
 */
@RestControllerAdvice
public class GatewayControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayControllerAdvice.class);

	@ExceptionHandler(Exception.class)
	public Message<String> exception(Exception e, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		if (e instanceof MessageCodeException) {
			final MessageCodeException exception = (MessageCodeException) e;
			return Message.fail(exception.getCode(), exception.getMessage(), null);
		} else {
			return Message.fail(MessageCode.CODE_9999, null);
		}
	}

}
