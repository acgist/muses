package com.acgist.boot;

import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * Spring工具
 * 
 * @author acgist
 */
public final class SpringUtils {

	private SpringUtils() {
	}

	/**
	 * ApplicationContext
	 */
	private static ApplicationContext context;

	/**
	 * @param context ApplicationContext
	 */
	public static final void setContext(ApplicationContext context) {
		SpringUtils.context = context;
	}
	
	/**
	 * 根据类型获取实例
	 * 
	 * @param <T> 类型
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> T getBean(Class<T> clazz) {
		while(SpringUtils.context == null) {
			Thread.yield();
		}
		return SpringUtils.context.getBean(clazz);
	}
	
	/**
	 * 根据类型获取实例
	 * 
	 * @param <T> 类型
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> Map<String, T> getBeanByType(Class<T> clazz) {
		while(SpringUtils.context == null) {
			Thread.yield();
		}
		return SpringUtils.context.getBeansOfType(clazz);
	}
	
}
