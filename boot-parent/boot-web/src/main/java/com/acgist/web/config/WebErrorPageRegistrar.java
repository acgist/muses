package com.acgist.web.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.acgist.web.controller.WebErrorController;

/**
 * 定义错误代码页面
 * 
 * @author acgist
 */
@Configuration
public class WebErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		final ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, WebErrorController.ERROR_PATH);
		final ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, WebErrorController.ERROR_PATH);
		final ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, WebErrorController.ERROR_PATH);
		registry.addErrorPages(errorPage400, errorPage404, errorPage500);
	}

}
