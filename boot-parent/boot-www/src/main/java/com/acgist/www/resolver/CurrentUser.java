package com.acgist.www.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.acgist.boot.model.User;

/**
 * 获取当前用户
 * 
 * @author acgist
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

	/**
	 * 获取注解类型
	 * 
	 * @author acgist
	 */
	public enum Type {
		
		/**
		 * @see User#getId()
		 */
		ID,
		/**
		 * @see User#getName()
		 */
		NAME,
		/**
		 * @see User
		 */
		USER;
		
	}
	
	/**
	 * @return 注入类型
	 */
	Type value() default Type.USER;
	
}
