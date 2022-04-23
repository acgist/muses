package com.acgist.www.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理工具
 * 
 * 注意：浏览器访问时有时候会出现多次异常输入属于正常情况（浏览器会重试）
 * 
 * @author acgist
 */
@Slf4j
public final class ErrorUtils {
	
	/**
	 * 错误地址
	 */
	public static final String ERROR_PATH = "/error";
	/**
	 * 系统异常
	 */
	public static final String ERROR_MESSAGE = "system.error.message";
	/**
	 * 错误响应
	 */
	public static final String STATUS_SERVLET = "javax.servlet.error.status_code";
	/**
	 * 异常
	 */
	public static final String THROWABLE_SERVLET = "javax.servlet.error.exception";
	/**
	 * 异常
	 */
	public static final String THROWABLE_SPRINTBOOT = "org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR";

	private ErrorUtils() {
	}
	
	/**
	 * 获取错误信息
	 * 
	 * @param request 请求
	 * @param response 响应
	 * 
	 * @return 错误信息
	 */
	public static final Message<String> message(HttpServletRequest request, HttpServletResponse response) {
		return message(null, request, response);
	}
	
	/**
	 * 获取错误信息
	 * 
	 * @param request 请求
	 * @param response 响应
	 * 
	 * @return 错误信息
	 */
	public static final Message<String> message(Throwable t, HttpServletRequest request, HttpServletResponse response) {
		final Message<String> message;
		int status = status(request, response);
		final Object globalErrorMessage = t == null ? globalErrorMessage(request) : t;
		final Object rootErrorMessage = rootErrorMessage(globalErrorMessage);
		if(rootErrorMessage instanceof MessageCodeException) {
			final MessageCodeException messageCodeException = (MessageCodeException) rootErrorMessage;
			status = status(status, messageCodeException);
			message = Message.fail(messageCodeException.getCode(), messageCodeException.getMessage());
		} else if(rootErrorMessage instanceof Throwable) {
			final Throwable throwable = (Throwable) rootErrorMessage;
			status = status(status, throwable);
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
		if(globalErrorMessage instanceof Throwable) {
			// 有时候dispatcherServlet会打印异常有时候又不会
			log.error("系统错误：{}-{}-{}-{}", message, method, path, query, globalErrorMessage);
		} else {
			log.warn("系统错误：{}-{}-{}-{}-{}", message, method, path, query, globalErrorMessage);
		}
		request.setAttribute(ERROR_MESSAGE, message);
		return message;
	}
	
	/**
	 * 获取异常信息
	 * 
	 * @param request 请求
	 * 
	 * @return 异常
	 */
	public static final Object globalErrorMessage(HttpServletRequest request) {
		Object throwable = request.getAttribute(THROWABLE_SERVLET);
		if(throwable != null) {
			return throwable;
		}
		throwable = request.getAttribute(THROWABLE_SPRINTBOOT);
		if(throwable != null) {
			return throwable;
		}
		return throwable;
	}
	
	/**
	 * 获取原始异常
	 * 
	 * @param t 异常
	 * 
	 * @return 原始异常
	 */
	public static final Object rootErrorMessage(Object t) {
		if(t instanceof Throwable) {
			return MessageCodeException.root((Throwable) t);
		}
		return t;
	}
	
	/**
	 * 获取响应状态
	 * 
	 * @param request 请求
	 * @param response 响应
	 * 
	 * @return 响应状态
	 */
	public static final int status(HttpServletRequest request, HttpServletResponse response) {
		final Object status = request.getAttribute(STATUS_SERVLET);
		if(status instanceof Integer) {
			return (int) status;
		}
		return response.getStatus();
	}
	
	/**
	 * 获取响应状态
	 * 
	 * @param status 原始状态
	 * @param e 异常
	 * 
	 * @return 响应状态
	 */
	public static final int status(int status, MessageCodeException e) {
		final String code = e.getCode().getCode();
		final int length = MessageCode.HTTP_STATUS.length();
		if (MessageCode.HTTP_STATUS.equals(code.substring(0, length))) {
			return Integer.parseInt(code.substring(length));
		}
		return status;
	}
	
	/**
	 * 获取响应状态
	 * 
	 * @param status 原始状态
	 * @param t 异常
	 * 
	 * @return 响应状态
	 * 
	 * @see DefaultHandlerExceptionResolver
	 */
	public static final int status(int status, Throwable t) {
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
		if (t instanceof MissingServletRequestParameterException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		}
		if (t instanceof HttpRequestMethodNotSupportedException) {
			return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
		}
		return status;
	}
	
	/**
	 * 判断是否错误请求
	 * 
	 * @param request 请求
	 * 
	 * @return 是否错误请求
	 */
	public static final boolean error(HttpServletRequest request) {
		return ERROR_PATH.equals(request.getServletPath());
	}
	
}
