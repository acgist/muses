package com.acgist.boot.utils;

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
	 * 是否不用Spring启动
	 */
	public static boolean noneSpring = false;
	/**
	 * ApplicationContext
	 */
	private static ApplicationContext context;
	/**
	 * 最大循环次数
	 */
	private static final int MAX_LOOP_INDEX = 1000;

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
		if(SpringUtils.noneSpring) {
			return null;
		}
		int index = 0;
		while(SpringUtils.context == null && ++index < MAX_LOOP_INDEX) {
			Thread.yield();
		}
		return SpringUtils.context.getBean(clazz);
	}
	
	/**
	 * 根据类型获取实例（可以为空）
	 * 
	 * @param <T> 类型
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> T getBeanNullable(Class<T> clazz) {
		return getBeanByType(clazz).values().stream().findFirst().orElse(null);
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
		if(SpringUtils.noneSpring) {
			return Map.of();
		}
		int index = 0;
		while(SpringUtils.context == null && ++index < MAX_LOOP_INDEX) {
			Thread.yield();
		}
		return SpringUtils.context.getBeansOfType(clazz);
	}
	
}
