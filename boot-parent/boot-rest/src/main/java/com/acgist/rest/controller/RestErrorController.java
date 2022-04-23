package com.acgist.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.model.Message;
import com.acgist.www.utils.ErrorUtils;

/**
 * 统一错误页面
 * 
 * @author acgist
 */
@RestController
public class RestErrorController implements ErrorController {

	@RequestMapping(value = ErrorUtils.ERROR_PATH)
	public Message<String> index(HttpServletRequest request, HttpServletResponse response) {
		return ErrorUtils.message(request, response);
	}

}
