package com.acgist.boot;

import java.util.Base64;
import java.util.regex.Pattern;

public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * 数值正则表达式（正负整数）
	 */
	private static final String NUMERIC_REGEX = "\\-?[0-9]+";
	/**
	 * 数值正则表达式（正负小数、正负整数）
	 */
	private static final String DECIMAL_REGEX = "\\-?[0-9]+(\\.[0-9]+)?";

	public static final boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	public static final boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public static final boolean isNumeric(String value) {
		return StringUtils.regex(value, NUMERIC_REGEX, true);
	}

	public static final boolean isDecimal(String value) {
		return StringUtils.regex(value, DECIMAL_REGEX, true);
	}

	public static final boolean regex(String value, String regex, boolean ignoreCase) {
		if (value == null || regex == null) {
			return false;
		}
		Pattern pattern;
		if (ignoreCase) {
			pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(regex);
		}
		return pattern.matcher(value).matches();
	}

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
		if(value == null) {
			throw new IllegalArgumentException("数据错误");
		}
		return Base64.getDecoder().decode(value.getBytes());
	}

}
