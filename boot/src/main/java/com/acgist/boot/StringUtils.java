package com.acgist.boot;

import java.util.Base64;

/**
 * 字符串工具
 * 
 * @author acgist
 */
public final class StringUtils {
	
	/**
	 * 初始数组大小
	 */
	private static final int SIZE = 16;

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
	    if(value == null) {
	        return null;
	    }
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
		    return null;
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

	/**
	 * 字符串分隔
	 * 
	 * @param source 原始字符串
	 * @param symbol 符号
	 * 
	 * @return 字符串数组
	 */
	public static final String[] split(String source, String symbol) {
		return split(source, symbol, false);
	}
	
	/**
	 * 字符串分隔
	 * 
	 * @param source 原始字符串
	 * @param symbol 符号
	 * 
	 * @return 字符串数组
	 */
	public static final String[] splitFull(String source, String symbol) {
		return split(source, symbol, true);
	}
	
	/**
	 * 字符串分隔
	 * 
	 * @param source 原始字符串
	 * @param symbol 符号
	 * @param full 是否完整返回
	 * 
	 * @return 字符串数组
	 */
	public static final String[] split(String source, String symbol, boolean full) {
		if(source == null || symbol == null) {
			return new String[0];
		}
		int size = 0;
		int left = 0;
		int index = 0;
		final int length = symbol.length();
		String[] array = new String[SIZE];
		do {
			index = source.indexOf(symbol, left);
			if(index < 0) {
				if(full) {
					array[size] = source.substring(left == 0 ? left : left - length);
				} else {
					array[size] = source.substring(left);
				}
			} else {
				if(full) {
					array[size] = source.substring(left == 0 ? left : left - length, index);
				} else {
					array[size] = source.substring(left, index);
				}
				left = index + length;
			}
			size++;
			if(size >= array.length) {
				final String[] newArray = new String[size + SIZE];
				System.arraycopy(array, 0, newArray, 0, size);
				array = newArray;
			}
		} while (index >= 0);
		final String[] result = new String[size];
		System.arraycopy(array, 0, result, 0, size);
		return result;
	}

}
