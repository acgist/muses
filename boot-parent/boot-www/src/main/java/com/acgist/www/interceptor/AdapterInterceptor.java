package com.acgist.www.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * 请求拦截器适配器
 * 
 * @author acgist
 */
public abstract class AdapterInterceptor implements WwwInterceptor {

	@Value("${system.error.path:/error}")
	private String errorPath;
	
	/**
	 * 判断是否错误请求
	 * 
	 * @param request 请求
	 * 
	 * @return 是否错误请求
	 */
	protected boolean error(HttpServletRequest request) {
		return StringUtils.isNotEmpty(this.errorPath) && this.errorPath.equals(request.getServletPath());
	}
	
}
