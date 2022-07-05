package com.acgist.boot.config;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.utils.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理自动配置
 * 
 * @author acgist
 */
@Slf4j
@Aspect
@Configuration
public class FallbackAutoConfiguration {

	/**
	 * 注解切点
	 */
	@Pointcut("@annotation(com.acgist.boot.config.Fallback)")
	public void fallback() {
	}
	
	/**
	 * 环绕执行
	 * 
	 * @param proceedingJoinPoint 切点
	 * 
	 * @return 返回
	 * 
	 * @throws Throwable 异常
	 */
	@Around("fallback()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			return proceedingJoinPoint.proceed();
		} catch (Throwable t) {
			final Fallback fallback = BeanUtils.getAnnotation(proceedingJoinPoint, Fallback.class);
			if(fallback.methodExecuteThrowable().isAssignableFrom(t.getClass())) {
				log.error("方法执行异常执行失败方法：{}-{}", fallback.method(), proceedingJoinPoint, t);
				MethodUtils.invokeMethod(proceedingJoinPoint.getThis(), fallback.method(), proceedingJoinPoint.getArgs());
			}
			if(fallback.throwThrowable()) {
				throw t;
			} else {
				return null;
			}
		}
	}
	
}
