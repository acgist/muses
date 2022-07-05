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
	 * @return 异常处理方法名称
	 */
	String method();
	
	/**
	 * 注意：和处理异常类型无关
	 * 
	 * @return 是否抛出异常
	 */
	boolean throwThrowable() default true;
	
	/**
	 * 注意：和是否抛出异常无关
	 * 
	 * @return 处理异常类型
	 */
	Class<? extends Throwable> methodExecuteThrowable() default Throwable.class;
	
}
