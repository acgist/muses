package com.acgist.data.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板查询
 * 
 * Pojo(paramter...)
 * List(paramter...)
 * Page(paramter..., Pageable)
 * 
 * 注意：参数 操作 目标
 * 
 * $(boolean)
 * $(name != null)
 * $(name == root || name == acgist)
 * $(name != null && name == acgist)
 * 
 * 默认使用SQL
 * 如果使用JPQL需要指定列名：SELECT user.name as name, user.memo as memo FROM UserEntity user
 * 
 * @author acgist
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TemplateQuery {

	// 特殊符号
	public static final String IF = "$";
	public static final String LEFT = "(";
	public static final String RIGHT = ")";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String COMMA_SPACE = TemplateQuery.COMMA + TemplateQuery.SPACE;
	// 条件判断
	public static final String CONDITION = "$(";
	public static final String CONDITION_OR = "||";
	public static final String CONDITION_AND = "&&";
	// 语句拼接
	public static final String WHERE = "where";
	public static final String ORDER_BY = "order by";
	// 语句查找
	public static final String QUERY_OR = "or ";
	public static final int QUERY_OR_LENGHT = QUERY_OR.length();
	public static final String QUERY_AND = "and ";
	public static final int QUERY_AND_LENGHT = QUERY_AND.length();
	public static final String QUERY_SELECT = "select ";
	public static final int QUERY_WHERE_LENGHT = QUERY_SELECT.length();
	
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
	 * @return 没有结果是否执行默认方法
	 */
	boolean fallback() default false;
	
	/**
	 * @return 本地语句
	 */
	boolean nativeQuery() default true;

	/**
	 * @return 返回类型（集合分页有效）
	 */
	Class<?> resultType() default Object.class;

}
