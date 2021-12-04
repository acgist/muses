package com.acgist.rest.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.Message;
import com.acgist.boot.MessageCode;

@RestController
public class RestErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorController.class);

	public static final String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH)
	public Message<Void> index(String code, String message, HttpServletResponse response) {
		LOGGER.warn("系统错误：{}-{}", message, response.getStatus());
		return Message.fail(MessageCode.of(code), message);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
