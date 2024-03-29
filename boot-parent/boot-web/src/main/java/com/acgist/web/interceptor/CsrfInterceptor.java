package com.acgist.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.www.interceptor.AdapterInterceptor;

/**
 * CSRF拦截器
 * 
 * POST请求验证是否含有Token防止CSRF攻击
 */
public class CsrfInterceptor extends AdapterInterceptor {

	private static final String SESSION_CSRF_TOKEN = "token";

	@Override
	public String[] patterns() {
		return new String[] { "/**" };
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(this.error(request)) {
			return true;
		}
		final String method = request.getMethod();
		final HttpSession session = request.getSession();
		final String trueToken = (String) session.getAttribute(SESSION_CSRF_TOKEN);
		if (HttpMethod.POST.matches(method.toUpperCase())) {
			String token = (String) request.getParameter(SESSION_CSRF_TOKEN);
			if(StringUtils.isEmpty(token)) {
				token = request.getHeader(SESSION_CSRF_TOKEN);
			}
			if (StringUtils.isNotEmpty(trueToken) && StringUtils.equals(token, trueToken)) {
				this.buildCsrfToken(session);
				return true;
			} else {
				throw MessageCodeException.of(MessageCode.CODE_3403);
			}
		}
		if (trueToken == null) {
			this.buildCsrfToken(session);
		}
		return true;
	}
	
	/**
	 * 生成Token
	 * 
	 * @param session session
	 */
	private void buildCsrfToken(HttpSession session) {
		session.setAttribute(SESSION_CSRF_TOKEN, UUID.randomUUID().toString());
	}

}
