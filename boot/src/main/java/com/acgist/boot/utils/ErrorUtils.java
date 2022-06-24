package com.acgist.boot.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
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
	 * 异常映射
	 */
	private static final Map<Class<?>, MessageCode> CODE_MAPPING = new LinkedHashMap<>();
	
	/**
	 * 错误地址
	 */
	public static final String ERROR_PATH = "/error";
	/**
	 * 系统异常信息
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
	 * 系统异常
	 */
	public static final String EXCEPTION_SYSTEM = "system.error.exception";
	/**
	 * Servlet异常
	 */
	public static final String EXCEPTION_SERVLET = "javax.servlet.error.exception";
	/**
	 * SpringBoot异常
	 */
	public static final String EXCEPTION_SPRINGBOOT = "org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR";

	private ErrorUtils() {
	}
	
	/**
	 * 注册异常（注意继承顺序）
	 * 
	 * @param code 异常编码
	 * @param clazz 异常类型
	 */
	public static final void register(MessageCode code, Class<?> clazz) {
		log.info("注册异常：{}-{}", code, clazz);
		CODE_MAPPING.put(clazz, code);
	}
	
	/**
	 * 获取错误信息
	 * 
	 * @param request 请求
	 * 
	 * @return 错误信息
	 */
	public static final Object getSystemErrorMessage(HttpServletRequest request) {
		return request.getAttribute(ErrorUtils.ERROR_MESSAGE);
	}
	
	/**
	 * 设置系统异常
	 * 
	 * @param request 请求
	 * @param e 异常
	 */
	public static final void putSystemErrorException(HttpServletRequest request, Exception e) {
		request.setAttribute(ErrorUtils.EXCEPTION_SYSTEM, e);
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
		// 获取异常信息
		final Object globalErrorMessage = t == null ? globalErrorMessage(request) : t;
		final Object rootErrorMessage = ExceptionUtils.root(globalErrorMessage);
		if(rootErrorMessage instanceof MessageCodeException) {
			// 自定义的异常
			final MessageCodeException messageCodeException = (MessageCodeException) rootErrorMessage;
			final MessageCode messageCode = messageCodeException.getCode();
			status = messageCode.getStatus();
			message = Message.fail(messageCode, messageCodeException.getMessage());
		} else if(rootErrorMessage instanceof Throwable) {
			// 未知异常
			final Throwable throwable = (Throwable) rootErrorMessage;
			final MessageCode messageCode = messageCode(status, throwable);
			status = messageCode.getStatus();
			message = Message.fail(messageCode, message(messageCode, throwable));
		} else {
			// 没有异常
			final MessageCode messageCode = MessageCode.of(status);
			message = Message.fail(messageCode);
		}
		// 状态编码
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
		// 系统异常
		Object throwable = request.getAttribute(EXCEPTION_SYSTEM);
		if(throwable != null) {
			return throwable;
		}
		// Servlet异常
		throwable = request.getAttribute(EXCEPTION_SERVLET);
		if(throwable != null) {
			return throwable;
		}
		// SpringBoot异常
		throwable = request.getAttribute(EXCEPTION_SPRINGBOOT);
		if(throwable != null) {
			return throwable;
		}
		return throwable;
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
			return (Integer) status;
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
	 * @see ResponseEntityExceptionHandler
	 * @see DefaultHandlerExceptionResolver
	 */
	public static final MessageCode messageCode(int status, Throwable t) {
		return CODE_MAPPING.entrySet().stream()
			.filter(entry -> {
				final Class<?> clazz = t.getClass();
				final Class<?> mappingClazz = entry.getKey();
				return mappingClazz.equals(clazz) || mappingClazz.isAssignableFrom(clazz);
			})
			.map(Map.Entry::getValue)
			.findFirst()
			.orElse(MessageCode.of(status));
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
		if(messageCode == MessageCode.CODE_9999 && StringUtils.isNotEmpty(message)) {
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