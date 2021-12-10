package com.acgist.boot;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.config.MusesConfig;

/**
 * <p>URL工具</p>
 * 
 * @author acgist
 */
public final class UrlUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtils.class);
	
	private UrlUtils() {
	}
	
	/**
	 * <p>URL编码</p>
	 * 
	 * @param content 原始内容
	 * 
	 * @return 编码内容
	 */
	public static final String encode(String content) {
		if(StringUtils.isEmpty(content)) {
			return content;
		}
		try {
			return URLEncoder
				.encode(content, MusesConfig.CHARSET)
				// 空格编码变成加号：加号解码变成空格
				.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}", content, e);
		}
		return content;
	}
	
	/**
	 * <p>URL解码</p>
	 * 
	 * @param content 编码内容
	 * 
	 * @return 原始内容
	 */
	public static final String decode(String content) {
		if(StringUtils.isEmpty(content)) {
			return content;
		}
		try {
			return URLDecoder.decode(content, MusesConfig.CHARSET);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL解码异常：{}", content, e);
		}
		return content;
	}
	
}
