package com.acgist.boot.utils;

/**
 * 数组工具
 * 
 * @author acgist
 */
public final class ArrayUtils {

	private ArrayUtils() {
	}

	/**
	 * 判断数组为空
	 * 
	 * @param array 数组
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}
	
	/**
	 * 判断数组不为空
	 * 
	 * @param array 数组
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

}
