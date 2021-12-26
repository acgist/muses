package com.acgist.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.pojo.bean.Message;
import com.acgist.boot.pojo.bean.MessageCode;

/**
 * 统一错误页面
 * 
 * @author acgist
 */
@Controller
public class WebErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebErrorController.class);

	public static final String ERROR_PATH = "/error";
	public static final String STATUS_SERVLET = "javax.servlet.error.status_code";
	public static final String THROWABLE_SERVLET = "javax.servlet.error.exception";
	public static final String THROWABLE_SPRINTBOOT = "org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR";

	@ResponseBody
	@RequestMapping(value = ERROR_PATH)
//	@RequestMapping(value = ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Message<String> index(HttpServletRequest request, HttpServletResponse response) {
		return this.message(request, response);
	}

	@RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		model.put("message", this.message(request, response));
		return ERROR_PATH;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
	/**
	 * 获取错误信息
	 * 
	 * @param request 请求
	 * @param response 响应
	 * 
	 * @return 错误信息
	 */
	private Message<String> message(HttpServletRequest request, HttpServletResponse response) {
		final Message<String> message;
		final Object object = this.throwable(request);
		int status = this.status(request, response);
		if(object instanceof MessageCodeException) {
			final MessageCodeException messageCodeException = (MessageCodeException) object;
			if(status == HttpServletResponse.SC_OK) {
				status = messageCodeException.getCode().getStatus();
			}
			message = Message.fail(messageCodeException.getCode(), messageCodeException.getMessage());
		} else if(object instanceof Throwable) {
			final Throwable throwable = (Throwable) object;
			if(status == HttpServletResponse.SC_OK) {
				status = this.status(throwable, response);
			}
			final MessageCode messageCode = MessageCode.of(status);
			message = Message.fail(messageCode, throwable.getMessage());
		} else {
			final MessageCode messageCode = MessageCode.of(status);
			message = Message.fail(messageCode);
		}
		if(status == HttpServletResponse.SC_OK) {
			status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		response.setStatus(status);
		final String method = request.getMethod();
		final String path = request.getServletPath();
		final String query = request.getQueryString();
		if(object instanceof Throwable) {
			LOGGER.error("系统错误：{}-{}-{}-{}", message, method, path, query, object);
		} else {
			LOGGER.warn("系统错误：{}-{}-{}-{}-{}", message, method, path, query, object);
		}
		return message;
	}
	
	/**
	 * 获取异常信息
	 * 
	 * @param request 请求
	 * 
	 * @return 异常
	 */
	private Object throwable(HttpServletRequest request) {
		Object object = request.getAttribute(THROWABLE_SERVLET);
		if(object != null) {
			return object;
		}
		object = request.getAttribute(THROWABLE_SPRINTBOOT);
		if(object != null) {
			return object;
		}
		return object;
	}
	
	/**
	 * 获取响应编码
	 * 
	 * @param request 请求
	 * @param response 响应
	 * 
	 * @return 响应编码
	 */
	private int status(HttpServletRequest request, HttpServletResponse response) {
		final Object status = request.getAttribute(STATUS_SERVLET);
		if(status instanceof Integer) {
			return (int) status;
		}
		return response.getStatus();
	}
	
	/**
	 * 获取响应编码
	 * 
	 * @param t 异常
	 * @param response 响应
	 * 
	 * @return 响应编码
	 * 
	 * @see DefaultHandlerExceptionResolver
	 */
	private int status(Throwable t, HttpServletResponse response) {
		if (t instanceof BindException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof TypeMismatchException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof NoHandlerFoundException) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		if (t instanceof AsyncRequestTimeoutException) {
			return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
		}
		if (t instanceof MissingPathVariableException) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		if (t instanceof ServletRequestBindingException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof HttpMessageNotReadableException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof MethodArgumentNotValidException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof ConversionNotSupportedException) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		if (t instanceof HttpMessageNotWritableException) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		if (t instanceof HttpMediaTypeNotSupportedException) {
			return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
		}
		if (t instanceof MissingServletRequestPartException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof HttpMediaTypeNotAcceptableException) {
			return HttpServletResponse.SC_NOT_ACCEPTABLE;
		}
		if (t instanceof HttpRequestMethodNotSupportedException) {
			return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
		}
		if (t instanceof MissingServletRequestParameterException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}
	
}
