package com.acgist.www.config;

import java.net.BindException;

import javax.annotation.PostConstruct;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.utils.ErrorUtils;
import com.acgist.www.resolver.RequestJsonArgumentResolver;

/**
 * Www MVC配置
 * 
 * @author acgist
 */
@Configuration
public class WwwMvcAutoConfiguration {

	@PostConstruct
	public void init() {
		this.registerException();
	}
	
	/**
	 * 注册异常
	 */
	public void registerException() {
		ErrorUtils.register(MessageCode.CODE_3400, BindException.class);
		ErrorUtils.register(MessageCode.CODE_3400, TypeMismatchException.class);
		ErrorUtils.register(MessageCode.CODE_3404, NoHandlerFoundException.class);
		ErrorUtils.register(MessageCode.CODE_3503, AsyncRequestTimeoutException.class);
		ErrorUtils.register(MessageCode.CODE_3500, MissingPathVariableException.class);
		ErrorUtils.register(MessageCode.CODE_3400, ServletRequestBindingException.class);
		ErrorUtils.register(MessageCode.CODE_3400, HttpMessageNotReadableException.class);
		ErrorUtils.register(MessageCode.CODE_3400, MethodArgumentNotValidException.class);
		ErrorUtils.register(MessageCode.CODE_3500, ConversionNotSupportedException.class);
		ErrorUtils.register(MessageCode.CODE_3500, HttpMessageNotWritableException.class);
		ErrorUtils.register(MessageCode.CODE_3415, HttpMediaTypeNotSupportedException.class);
		ErrorUtils.register(MessageCode.CODE_3400, MissingServletRequestPartException.class);
		ErrorUtils.register(MessageCode.CODE_3406, HttpMediaTypeNotAcceptableException.class);
		ErrorUtils.register(MessageCode.CODE_3405, HttpRequestMethodNotSupportedException.class);
		ErrorUtils.register(MessageCode.CODE_3400, MissingServletRequestParameterException.class);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
	public RequestJsonArgumentResolver requestJsonArgumentResolver() {
		return new RequestJsonArgumentResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
	public WwwMvcConfig wwwMvcConfig() {
		return new WwwMvcConfig();
	}

}
