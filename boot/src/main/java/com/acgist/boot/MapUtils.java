package com.acgist.boot;

import java.util.Map;
import java.util.stream.Collectors;

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
	
	/**
	 * <p>Map转为URL参数</p>
	 * 
	 * @param map Map
	 * 
	 * @return URL参数
	 */
	public static final String toUrlQuery(Map<String, String> map) {
		if(MapUtils.isEmpty(map)) {
			return null;
		}
		return map.entrySet().stream()
			.map(entry -> String.join("=", entry.getKey(), UrlUtils.encode(entry.getValue())))
			.collect(Collectors.joining("&"));
	}
	
}
