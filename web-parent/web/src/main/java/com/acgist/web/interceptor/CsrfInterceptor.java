package com.acgist.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.StringUtils;
import com.acgist.boot.pojo.bean.MessageCode;
import com.acgist.www.ErrorUtils;

/**
 * CSRF拦截器
 * 
 * POST请求验证是否含有Token防止CSRF攻击
 */
public class CsrfInterceptor implements HandlerInterceptor {

	private static final String SESSION_CSRF_TOKEN = "token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String method = request.getMethod();
		final HttpSession session = request.getSession();
		final String path = request.getServletPath();
		final String trueToken = (String) session.getAttribute(SESSION_CSRF_TOKEN);
		if (
			!ErrorUtils.ERROR_PATH.equals(path) &&
			HttpMethod.POST.matches(method.toUpperCase())
		) {
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
