package com.acgist.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.boot.model.Message;
import com.acgist.www.utils.ErrorUtils;

/**
 * 统一错误页面
 * 
 * @author acgist
 */
@Controller
public class WebErrorController implements ErrorController {

	@ResponseBody
	@RequestMapping(value = ErrorUtils.ERROR_PATH)
//	@RequestMapping(value = ErrorUtils.ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Message<String> index(HttpServletRequest request, HttpServletResponse response) {
		return ErrorUtils.message(request, response);
	}

	@RequestMapping(value = ErrorUtils.ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		model.put("message", ErrorUtils.message(request, response));
		return ErrorUtils.ERROR_PATH;
	}
	
}
