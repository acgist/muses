package com.acgist.boot;

import java.util.Map;

/**
 * Map工具
 * 
 * @author acgist
 */
public final class MapUtils {

	private MapUtils() {
	}
	
	/**
	 * 判断Map是否为空
	 * 
	 * @param map Map
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * 判断Map是否非空
	 * 
	 * @param map Map
	 * 
	 * @return 是否非空
	 */
	public static final boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
}
