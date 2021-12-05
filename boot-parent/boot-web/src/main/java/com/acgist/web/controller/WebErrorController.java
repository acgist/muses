package com.acgist.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.boot.Message;
import com.acgist.boot.MessageCode;
import com.acgist.boot.StringUtils;

/**
 * 统一错误页面
 * 
 * @author acgist
 */
@Controller
public class WebErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebErrorController.class);

	public static final String ERROR_PATH = "/error";

	@ResponseBody
	@RequestMapping(value = ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Message<String> index(String code, String message, HttpServletResponse response) {
		final MessageCode messageCode = MessageCode.of(code, response.getStatus());
		LOGGER.warn("系统错误：{}-{}", messageCode, message);
		if (StringUtils.isEmpty(message)) {
			return Message.fail(messageCode);
		} else {
			return Message.fail(messageCode, message);
		}
	}

	@RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
	public String index(String code, String message, ModelMap model, HttpServletResponse response) {
		final MessageCode messageCode = MessageCode.of(code, response.getStatus());
		LOGGER.warn("系统错误：{}-{}", messageCode, message);
		if (StringUtils.isEmpty(message)) {
			model.put("message", Message.fail(messageCode));
		} else {
			model.put("message", Message.fail(messageCode, message));
		}
		return ERROR_PATH;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
}
