package com.acgist.www.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
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
@Inherited
@Documented
public @interface CurrentUser {

	/**
	 * 获取参数类型
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
		USER,
		/**
		 * 根据参数类型获取
		 */
		AUTO;
		
	}
	
	/**
	 * 如果ID和Name类型相同不能通过类型获取参数
	 * 
	 * @return 参数类型
	 */
	Type value() default Type.AUTO;
	
}
