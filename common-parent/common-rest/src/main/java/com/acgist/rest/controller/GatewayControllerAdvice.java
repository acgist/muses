package com.acgist.rest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acgist.rest.GatewaySession;
import com.acgist.rest.config.GatewayCode;
import com.acgist.rest.config.GatewayCodeException;

/**
 * 系统异常
 * 
 * @author acgist
 */
@ControllerAdvice
public class GatewayControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayControllerAdvice.class);

	@Autowired
	private ApplicationContext context;

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	public Map<String, Object> exception(Exception e, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if (e instanceof GatewayCodeException) {
			final GatewayCodeException exception = (GatewayCodeException) e;
			return session.buildFail(exception.getCode(), exception.getMessage()).buildResponse();
		} else {
			return session.buildFail(GatewayCode.CODE_9999).buildResponse();
		}
	}

}
