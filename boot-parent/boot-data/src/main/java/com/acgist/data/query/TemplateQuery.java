package com.acgist.data.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板查询
 * 
 * List(paramter...)
 * Result(paramter...)
 * Page(paramter..., Pageable)
 * 
 * $(boolean)
 * $(name != null)
 * $(name == acgist)
 * $(name == root || name == acgist)
 * 
 * @author yusheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TemplateQuery {

	public static final String IF = "$";
	public static final String LEFT = "(";
	public static final String RIGHT = ")";
	public static final String LINE = "\n";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String OR_TOKEN = "\\|\\|";
	public static final String AND_TOKEN = "\\&\\&";
	public static final String OR = "or";
	public static final String AND = "and";
	public static final String WHERE = "where";
	public static final String SELECT = "select";
	public static final String ORDER_BY = "order by";
	
	/**
	 * 条件判断
	 * 
	 * @author acgist
	 */
	public enum Condition {

		EQ("=="),
		NE("!="),
		LE("<="),
		LT("<"),
		GE(">="),
		GT(">");

		/**
		 * 条件符号
		 */
		private final String symbol;

		private Condition(String symbol) {
			this.symbol = symbol;
		}
		
		public String symbol() {
			return this.symbol;
		}

	}

	/**
	 * @return 执行语句：插入、删除、更新、查询
	 */
	String query();

	/**
	 * @return 统计语句
	 */
	String count() default "";

	/**
	 * @return 条件语句
	 */
	String where() default "";

	/**
	 * @return 排序语句：order by
	 */
	String sorted() default "";

	/**
	 * @return 附加语句：limit、group、having、order by
	 */
	String attach() default "";

	/**
	 * @return 没有结果是否执行默认方法（单个对象有效）
	 */
	boolean fallback() default false;
	
	/**
	 * @return 本地语句
	 */
	boolean nativeQuery() default true;

	/**
	 * @return 返回类型（集合分页有效）
	 */
	Class<?> clazz() default Object.class;

}
