package com.acgist.www.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求拦截器
 * 
 * @author acgist
 */
public interface WwwInterceptor extends Comparable<WwwInterceptor>, HandlerInterceptor {

	/**
	 * @return 排序
	 */
	default int order() {
		return 0;
	}
	
	/**
	 * @return 是否启用
	 */
	default boolean enable() {
		return true;
	}
	
	@Override
	default int compareTo(WwwInterceptor target) {
		return Integer.compare(this.order(), target.order());
	}
	
	/**
	 * @return 名称
	 */
	default String name() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * @return 拦截地址
	 */
	String[] patterns();
	
}
