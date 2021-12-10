package com.acgist.boot;

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
	 * 判断集合是否非空
	 * 
	 * @param collection 集合
	 * 
	 * @return 是否非空
	 */
	public static final boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

}
