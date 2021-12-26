package com.acgist.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.pojo.bean.Message;
import com.acgist.boot.pojo.bean.MessageCode;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);
	
	@ExceptionHandler(Exception.class)
	public Message<String> exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		final Message<String> message;
		int status = this.status(request, response);
		if(e instanceof MessageCodeException) {
			final MessageCodeException messageCodeException = (MessageCodeException) e;
			final int codeStatus = messageCodeException.getCode().getStatus();
			if(codeStatus != HttpServletResponse.SC_OK) {
				status = codeStatus;
			}
			message = Message.fail(messageCodeException.getCode(), messageCodeException.getMessage());
		} else {
			if(status == HttpServletResponse.SC_OK) {
				status = this.status(e, response);
			}
			final MessageCode messageCode = MessageCode.of(status);
			message = Message.fail(messageCode, e.getMessage());
		}
		response.setStatus(status);
		final String method = request.getMethod();
		final String path = request.getServletPath();
		final String query = request.getQueryString();
		LOGGER.warn("系统错误：{}-{}-{}-{}-{}", message, method, path, query, e);
		return message;
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
		final Object status = request.getAttribute(RestErrorController.STATUS_SERVLET);
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
