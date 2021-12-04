package com.acgist.gateway.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.acgist.rest.controller.RestErrorController;

@Configuration
public class GatewayErrorPageRegistrarAutoConfiguration implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
        final ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, RestErrorController.ERROR_PATH);
        final ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, RestErrorController.ERROR_PATH);
        final ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, RestErrorController.ERROR_PATH);
        registry.addErrorPages(errorPage400, errorPage404, errorPage500);
	}

}
