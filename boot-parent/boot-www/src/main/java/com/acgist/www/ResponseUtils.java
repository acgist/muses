package com.acgist.www;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.acgist.boot.pojo.bean.Message;

/**
 * 响应工具
 * 
 * @author acgist
 */
public final class ResponseUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);
	
	private ResponseUtils() {
	}

	/**
	 * 写出消息
	 * 
	 * @param message 消息
	 * @param response 响应
	 */
	public static final void response(Message<?> message, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try {
			response.getWriter().write(message.toString());
		} catch (IOException e) {
			LOGGER.error("写出响应数据异常", e);
		}
	}
	
}
