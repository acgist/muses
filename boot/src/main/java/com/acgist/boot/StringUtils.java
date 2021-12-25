package com.acgist.boot;

import java.util.Base64;

/**
 * 字符串工具
 * 
 * @author acgist
 */
public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param value 字符串
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * 判断字符串是否非空
	 * 
	 * @param value 字符串
	 * 
	 * @return 是否非空
	 */
	public static final boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	/**
	 * 判断字符串是否相等
	 * 
	 * @param source 原始字符串
	 * @param target 目标字符串
	 * 
	 * @return 是否相等
	 */
	public static final boolean equals(String source, String target) {
		return source == null ? target == null : source.equals(target);
	}

	/**
	 * Base64编码
	 * 
	 * @param value 原始数据
	 * 
	 * @return 编码数据
	 */
	public static final String base64Encode(byte[] value) {
		return new String(Base64.getEncoder().encode(value));
	}

	/**
	 * Base64解码
	 * 
	 * @param value 编码数据
	 * 
	 * @return 原始数据
	 */
	public static final byte[] base64Decode(String value) {
		if (value == null) {
			throw MessageCodeException.of("格式错误：", value);
		}
		return Base64.getDecoder().decode(value.getBytes());
	}
	
	/**
	 * 判断开头文本是否匹配
	 * 
	 * @param source 原始文本
	 * @param target 目标文本
	 * 
	 * @return 是否匹配
	 */
	public static final boolean startsWidthIgnoreCase(String source, String target) {
		if(source.length() < target.length()) {
			return false;
		}
		return source.toLowerCase().startsWith(target.toLowerCase());
	}

}
