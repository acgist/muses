package com.acgist.www.utils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
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
 * 异常处理：
 * 1. MessageCodeException直接获取错误编码和错误提示
 * 2. 未知系统异常使用异常错误提示
 * 3. 已知系统异常如果异常提示少于64字符返回异常提示反之使用已知错误提示
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
	 * 错误地址
	 */
	public static final String SERVLET_REQUEST_URI = "javax.servlet.error.request_uri";
	/**
	 * 错误响应
	 */
	public static final String SERVLET_STATUS_CODE = "javax.servlet.error.status_code";
	/**
	 * 异常
	 */
	public static final String SERVLET_EXCEPTION = "javax.servlet.error.exception";
	/**
	 * 异常
	 */
	public static final String SPRINGBOOT_EXCEPTION = "org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR";

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
			final MessageCode messageCode = messageCodeException.getCode();
			status = messageCode.getStatus();
			message = Message.fail(messageCode, messageCodeException.getMessage());
		} else if(rootErrorMessage instanceof Throwable) {
			final Throwable throwable = (Throwable) rootErrorMessage;
			final MessageCode messageCode = messageCode(status, throwable);
			status = messageCode.getStatus();
			message = Message.fail(messageCode, message(messageCode, throwable));
		} else {
			final MessageCode messageCode = MessageCode.of(status);
			message = Message.fail(messageCode);
		}
		if(status == HttpServletResponse.SC_OK) {
			status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		response.setStatus(status);
		final String method = request.getMethod();
		final String path = Objects.toString(request.getAttribute(SERVLET_REQUEST_URI), request.getServletPath());
		final String query = request.getQueryString();
		log.warn("系统错误：{}-{}-{}", method, path, query);
		if(globalErrorMessage instanceof Throwable) {
//			log.error("""
//				系统错误：{}-{}-{}
//				错误信息：{}
//				""", method, path, query, message, globalErrorMessage);
		} else {
//			log.warn("""
//				系统错误：{}-{}-{}
//				错误信息：{}-{}
//				""", method, path, query, message, globalErrorMessage);
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
		Object throwable = request.getAttribute(SERVLET_EXCEPTION);
		if(throwable != null) {
			return throwable;
		}
		throwable = request.getAttribute(SPRINGBOOT_EXCEPTION);
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
		final Object status = request.getAttribute(SERVLET_STATUS_CODE);
		if(status instanceof Integer) {
			return (int) status;
		}
		return response.getStatus();
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
	public static final MessageCode messageCode(int status, Throwable t) {
		if (t instanceof BindException) {
			return MessageCode.CODE_3400;
		}
//		if (t instanceof InvalidGrantException) {
//			return MessageCode.CODE_3401;
//		}
		if (t instanceof TypeMismatchException) {
			return MessageCode.CODE_3400;
		}
		if (t instanceof NoHandlerFoundException) {
			return MessageCode.CODE_3404;
		}
//		if (t instanceof AuthenticationException) {
//			return MessageCode.CODE_3401;
//		}
		if (t instanceof AsyncRequestTimeoutException) {
			return MessageCode.CODE_3503;
		}
		if (t instanceof MissingPathVariableException) {
			return MessageCode.CODE_3500;
		}
		if (t instanceof ServletRequestBindingException) {
			return MessageCode.CODE_3400;
		}
		if (t instanceof HttpMessageNotReadableException) {
			return MessageCode.CODE_3400;
		}
		if (t instanceof MethodArgumentNotValidException) {
			return MessageCode.CODE_3400;
		}
		if (t instanceof ConversionNotSupportedException) {
			return MessageCode.CODE_3500;
		}
		if (t instanceof HttpMessageNotWritableException) {
			return MessageCode.CODE_3500;
		}
		if (t instanceof HttpMediaTypeNotSupportedException) {
			return MessageCode.CODE_3415;
		}
		if (t instanceof MissingServletRequestPartException) {
			return MessageCode.CODE_3400;
		}
		if (t instanceof HttpMediaTypeNotAcceptableException) {
			return MessageCode.CODE_3406;
		}
		if (t instanceof HttpRequestMethodNotSupportedException) {
			return MessageCode.CODE_3405;
		}
		if (t instanceof MissingServletRequestParameterException) {
			return MessageCode.CODE_3400;
		}
		// 唯一约束
		if(
			t instanceof DuplicateKeyException ||
			t instanceof SQLIntegrityConstraintViolationException
		) {
			return MessageCode.CODE_4001;
		}
		return MessageCode.of(status);
	}
	
	/**
	 * 获取异常信息
	 * 
	 * @param messageCode 错误编码
	 * @param t 异常
	 * 
	 * @return 异常信息
	 */
	public static final String message(MessageCode messageCode, Throwable t) {
		if(
			t instanceof BindException ||
			t instanceof MethodArgumentNotValidException
		) {
			final BindException bindException = (BindException) t;
			final List<ObjectError> allErrors = bindException.getAllErrors();
			return allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
		}
		// 为了系统安全建议不要直接返回
		final String message = t.getMessage();
		if(messageCode == MessageCode.CODE_9999) {
			return message;
		}
		if(StringUtils.isNotEmpty(message) && message.length() < 64) {
			return message;
		}
		return messageCode.getMessage();
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
