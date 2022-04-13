package com.acgist.boot;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * Bean工具
 * 
 * @author acgist
 */
@Slf4j
public final class BeanUtils {

	private BeanUtils() {
	}
	
	/**
	 * 获取类型实例
	 * 
	 * @param <T> 类型
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> T newInstance(Class<T> clazz) {
		Objects.requireNonNull(clazz, "无效类型");
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			log.error("通过反射生成实例异常：{}", clazz, e);
		}
		return null;
	}
	
}
