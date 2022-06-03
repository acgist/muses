package com.acgist.rule.condition;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.acgist.boot.utils.DateUtils;
import com.acgist.boot.utils.FormatStyle.DateTimeStyle;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 条件
 * 
 * 注意：不要保留任何状态，保证单例线程安全。
 * 
 * @author acgist
 */
@Slf4j
public abstract class Condition implements ICondition {
	
	/**
	 * 过滤操作类型
	 */
	protected final ConditionOperation conditionOperation;
	
	/**
	 * @param conditionOperation 过滤操作类型
	 */
	protected Condition(ConditionOperation conditionOperation) {
		this.conditionOperation = conditionOperation;
	}
	
	@Override
	public ConditionOperation getConditionOperation() {
		return this.conditionOperation;
	}
	
	@Override
	public boolean filter(Object source, RuleCondition condition) {
		final String field = condition.getField();
		final String fieldValue = condition.getValue();
		if(StringUtils.isEmpty(fieldValue)) {
			return true;
		}
		final Class<?> fieldClazz = this.getType(source.getClass(), field);
		final Object fieldValueRt = this.getValue(source, field);
		if(this.filter(fieldValue, fieldClazz, fieldValueRt)) {
			return true;
		} else {
			log.warn("不等：{}", condition.getCondition());
			return false;
		}
	}
	
	@Override
	public <T> void buildWrapper(Class<T> entityClazz, QueryWrapper<T> wrapper, RuleCondition condition) {
		final String field = condition.getField();
		final String fieldValue = condition.getValue();
		if(StringUtils.isEmpty(fieldValue)) {
			return;
		}
		final Class<?> fieldClazz = this.getType(entityClazz, field);
		this.buildWrapper(entityClazz, field, fieldClazz, fieldValue, wrapper);
	}
	
	/**
	 * 过滤
	 * 
	 * @param fieldValue 字段值
	 * @param fieldClazz 字段类型
	 * @param fieldValueRt 字段当前值
	 * 
	 * @return 是否过滤：true-通过；false-不通过；
	 */
	protected abstract boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt);
	
	/**
	 * SQL过滤条件
	 * 
	 * @param <T> 类型
	 * 
	 * @param entityClazz 查询类型
	 * @param field 字段名
	 * @param fieldClazz 字段类型
	 * @param fieldValue 字段值
	 * @param wrapper 查询条件
	 */
	protected abstract <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper);

	/**
	 * 获取字段类型
	 * 
	 * @param clazz 过滤对象类型
	 * @param field 字段
	 * 
	 * @return 字段类型
	 */
	protected Class<?> getType(Class<?> clazz, String field) {
		final Field value = FieldUtils.getField(clazz, field, true);
		Objects.requireNonNull(value, "条件字段不存在：" + field);
		return value.getType();
	}
	
	/**
	 * 读取字段值
	 * 
	 * @param source 过滤对象
	 * @param field 字段
	 * 
	 * @return 字段值
	 */
	protected Object getValue(Object source, String field) {
		final Field value = FieldUtils.getField(source.getClass(), field, true);
		Objects.requireNonNull(value, "条件字段不存在：" + field);
		try {
			return value.get(source);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("读取属性异常", e);
		}
		return null;
	}
	
	/**
	 * 获取单值
	 * 
	 * @param clazz 类型
	 * @param value 条件
	 * 
	 * @return 单值
	 */
	protected Object getObject(Class<?> clazz, String value) {
		if(Long.TYPE.isAssignableFrom(clazz)) {
			return Long.valueOf(value);
		}
		if(Integer.TYPE.isAssignableFrom(clazz)) {
			return Integer.valueOf(value);
		}
		if(Number.class.isAssignableFrom(clazz)) {
			return Long.valueOf(value);
		}
		if(Boolean.TYPE.isAssignableFrom(clazz)) {
			return Boolean.valueOf(value);
		}
		if(Date.class.isAssignableFrom(clazz)) {
			return DateUtils.parse(value);
		}
		// 时间、字符串等不用转换
		return value;
	}
	
	/**
	 * 获取数组
	 * 
	 * @param clazz 类型
	 * @param value 条件
	 * 
	 * @return 数组
	 */
	protected <T> List<T> getList(Class<T> clazz, String value) {
		return JSONUtils.toList(value, clazz);
	}
	
	/**
	 * 判断内容是否相等
	 * 
	 * @param source 原始对象
	 * @param target 目标对象
	 * 
	 * @return 是否相等
	 */
	protected boolean equals(Object source, Object target) {
		// 支持配置空值
		return source == target || String.valueOf(source).equals(String.valueOf(target));
	}
	
	/**
	 * 判断内容是否包含
	 * 
	 * @param source 原始对象
	 * @param target 包含对象
	 * 
	 * @return 是否包含
	 */
	protected boolean contains(Object source, Object target) {
		return String.valueOf(source).contains(String.valueOf(target));
	}
	
	/**
	 * 比较当前值和配置值
	 * 
	 * @param fieldValue 字段值
	 * @param fieldClazz 字段类型
	 * @param fieldValueRt 字段当前值
	 * 
	 * @return 整数：大于；0：等于；负数：小于；
	 */
	protected int compare(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		if(Long.TYPE.isAssignableFrom(fieldClazz)) {
			return ((Long) fieldValueRt).compareTo(Long.valueOf(fieldValue));
		}
		if(Integer.TYPE.isAssignableFrom(fieldClazz)) {
			return ((Integer) fieldValueRt).compareTo(Integer.valueOf(fieldValue));
		}
		// 其他转为Long比较
		if(Number.class.isAssignableFrom(fieldClazz)) {
			return Long.valueOf(String.valueOf(fieldValueRt)).compareTo(Long.valueOf(fieldValue));
		}
		if(Date.class.isAssignableFrom(fieldClazz)) {
			return ((Date) fieldValueRt).compareTo(DateUtils.parse(fieldValue, DateTimeStyle.YYYY_MM_DD_HH24_MM_SS.getFormat()));
		}
		return String.valueOf(fieldValueRt).compareTo(fieldValue);
	}
	
}
