package com.acgist.boot.utils;

import java.util.Collection;

/**
 * 集合工具
 * 
 * @author acgist
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param collection 集合
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断集合是否不为空
	 * 
	 * @param collection 集合
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	/**
	 * 获取第一个元素
	 * 
	 * @param <T> 类型
	 * @param collection 集合
	 * 
	 * @return 第一个元素
	 */
	public static final <T> T getFirst(Collection<T> collection) {
		if(isEmpty(collection)) {
			return null;
		}
		return collection.stream().findFirst().orElse(null);
	}

}
