package com.acgist.www;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * 定义错误代码页面
 * 
 * @author acgist
 */
public class WwwErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		final ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, ErrorUtils.ERROR_PATH);
		final ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorUtils.ERROR_PATH);
		registry.addErrorPages(errorPage400, errorPage500);
	}

}
