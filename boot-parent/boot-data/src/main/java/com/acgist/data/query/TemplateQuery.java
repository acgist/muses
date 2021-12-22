package com.acgist.data.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板查询
 * 
 * @author yusheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TemplateQuery {

	/**
	 * {@value}
	 */
	public static final String IF = "$";
	/**
	 * {@value}
	 */
	public static final String LINE = "\n";
	/**
	 * {@value}
	 */
	public static final String SPACE = " ";
	/**
	 * {@value}
	 */
	public static final String WHERE = "where";
	
	/**
	 * 查询语句
	 * 
	 * @return 查询语句
	 */
	String select();
	
	/**
	 * 统计语句
	 * 
	 * @return 统计语句
	 */
	String count() default "";
	
	/**
	 * 条件语句
	 * 
	 * @return 条件语句
	 */
	String where() default "";
	
	/**
	 * 排序语句
	 * 
	 * @return 排序语句
	 * 
	 * @see #attach()
	 */
	String sorted() default "";
	
	/**
	 * 附加语句
	 * 
	 * @return 附加语句
	 */
	String attach() default "";
	
	/**
	 * 本地语句
	 * 
	 * @return 本地语句
	 */
	boolean nativeQuery() default true;
	
}
