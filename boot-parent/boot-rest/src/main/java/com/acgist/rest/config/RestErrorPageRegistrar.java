package com.acgist.rest.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

import com.acgist.rest.controller.RestErrorController;

/**
 * 定义错误代码页面
 * 
 * @author acgist
 */
public class RestErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		final ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, RestErrorController.ERROR_PATH);
		final ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, RestErrorController.ERROR_PATH);
		registry.addErrorPages(errorPage400, errorPage500);
	}

}
