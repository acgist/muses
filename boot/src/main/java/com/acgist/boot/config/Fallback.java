package com.acgist.boot.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常处理切点
 * 
 * @author acgist
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Fallback {
	
	/**
	 * 注意：返回值和方法必须一致
	 * 
	 * @return 异常处理方法
	 */
	String method();
	
	/**
	 * 注意：配置和处理异常类型无关
	 * 
	 * @return 异常抛出
	 */
	boolean throwThrowable() default true;
	
	/**
	 * 注意：只和是否执行异常处理方法有关
	 * 
	 * @return 处理异常类型
	 */
	Class<? extends Throwable> methodExecuteThrowable() default Throwable.class;
	
}
