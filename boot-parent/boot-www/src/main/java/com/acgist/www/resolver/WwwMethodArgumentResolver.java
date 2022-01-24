package com.acgist.www.resolver;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * 参数解析器
 * 
 * @author acgist
 */
public interface WwwMethodArgumentResolver extends HandlerMethodArgumentResolver {

	/**
	 * @return 是否启用
	 */
	default boolean enable() {
		return true;
	}
	
	/**
	 * @return 名称
	 */
	default String name() {
		return this.getClass().getSimpleName();
	}
	
	
}
