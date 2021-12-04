package com.acgist.rest.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.boot.Message;
import com.acgist.boot.MessageCode;
import com.acgist.boot.MessageCodeException;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);

	@ExceptionHandler(Exception.class)
	public Message<Void> exception(Exception e, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		if (e instanceof MessageCodeException) {
			final MessageCodeException exception = (MessageCodeException) e;
			return Message.fail(exception.getCode(), exception.getMessage());
		} else if (e instanceof ValidationException) {
			return Message.fail(MessageCode.CODE_1002, e.getMessage());
		} else {
			return Message.fail(MessageCode.CODE_9999, e.getMessage());
		}
	}

}
