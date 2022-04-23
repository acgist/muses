package com.acgist.www.utils;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.model.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应工具
 * 
 * @author acgist
 */
@Slf4j
public final class ResponseUtils {

	private ResponseUtils() {
	}

	/**
	 * 写出错误消息
	 * 
	 * @param message 消息
	 * @param response 响应
	 */
	public static final void fail(Message<?> message, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response(message, response);
	}
	
	/**
	 * 写出消息
	 * 
	 * @param message 消息
	 * @param response 响应
	 */
	public static final void response(Message<?> message, HttpServletResponse response) {
		Objects.requireNonNull(message, "响应错误：message");
		Objects.requireNonNull(response, "响应错误：response");
		response.setContentType(MusesConfig.APPLICATION_JSON_UTF8);
		try {
			response.getWriter().write(message.toString());
		} catch (IOException e) {
			log.error("写出响应数据异常：{}", message, e);
		}
	}
	
}
