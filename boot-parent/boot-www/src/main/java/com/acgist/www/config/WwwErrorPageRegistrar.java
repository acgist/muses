package com.acgist.www.config;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${system.error.path:/error}")
	private String errorPath;
	
	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		final ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, this.errorPath);
		final ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, this.errorPath);
		registry.addErrorPages(errorPage400, errorPage500);
	}

}
