package com.acgist.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.beans.BeanMap;

/**
 * 查询语句工具
 * 
 * @author acgist
 */
public final class TemplateQueryUtils {

	private TemplateQueryUtils() {
	}
	
	/**
	 * 创建参数
	 * 
	 * @param args 参数
	 * @param argsNames 参数名称
	 * @param parameterLength 参数长度
	 * 
	 * @return 参数
	 */
	public static final Map<String, Object> buildParamterMap(Object[] args, String[] argsNames, int parameterLength) {
		// TODO：JDK17
		Object object;
		final Map<String, Object> paramterMap = new HashMap<>();
		for (int index = 0; index < parameterLength; index++) {
			object = args[index];
			if (object instanceof Map) {
				paramterMap.putAll((Map<String, Object>) object);
			} else if (object instanceof Boolean || object instanceof String || object instanceof Number || object instanceof Date) {
				paramterMap.put(argsNames[index], args[index]);
			} else if (object != null) {
				BeanMap.create(object).forEach((key, value) -> paramterMap.put((String) key, value));
			}
		}
		return paramterMap;
	}
	
}
