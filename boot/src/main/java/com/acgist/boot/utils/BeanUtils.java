package com.acgist.boot.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.acgist.boot.model.MessageCodeException;

import lombok.extern.slf4j.Slf4j;

/**
 * Bean工具
 * 
 * @author acgist
 */
@Slf4j
public final class BeanUtils {

	private BeanUtils() {
	}
	
	/**
	 * 获取类型实例
	 * 
	 * @param <T> 类型
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> T newInstance(Class<T> clazz) {
		Objects.requireNonNull(clazz, "无效类型");
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			log.error("获取类型实例异常：{}", clazz, e);
		}
		return null;
	}
	
	/**
	 * 读取对象字段属性
	 * 
	 * @param field 字段
	 * @param value 对象
	 * 
	 * @return 字段属性
	 */
	public static final Object fieldValue(String field, Object value) {
		try {
			return FieldUtils.getField(value.getClass(), field, true).get(value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("读取对象字段属性异常", e);
		}
		return null;
	}
	
	/**
	 * 获取切点注解
	 * 
	 * @param <T> 注解类型
	 * 
	 * @param proceedingJoinPoint 切点
	 * @param clazz 注解类型
	 * 
	 * @return 注解
	 */
	public static final <T extends Annotation> T getAnnotation(ProceedingJoinPoint proceedingJoinPoint, Class<T> clazz) {
		if (proceedingJoinPoint.getSignature() instanceof MethodSignature methodSignature) {
			final T t = methodSignature.getMethod().getAnnotation(clazz);
			if (t == null) {
				throw MessageCodeException.of("获取切点注解失败：", clazz);
			}
			return t;
		}
		throw MessageCodeException.of("切点注解类型错误：", clazz);
	}
	
	/**
	 * 属性拷贝
	 * 
	 * @param object 对象
	 * @param data 属性
	 */
	public static final void copy(Object object, Map<String, Object> data) {
		if(object == null || MapUtils.isEmpty(data)) {
			return;
		}
		BeanUtils.copy(object, data, data.keySet().toArray(String[]::new));
	}
	
	/**
	 * 属性拷贝：自动转换属性
	 * 
	 * @param object 对象
	 * @param data 属性
	 * @param fields 拷贝属性
	 */
	public static final void copy(Object object, Map<String, Object> data, String ... fields) {
		if(object == null || MapUtils.isEmpty(data) || ArrayUtils.isEmpty(fields)) {
			return;
		}
		final Class<?> clazz = object.getClass();
		for (String fieldName : fields) {
			BeanUtils.copy(object, FieldUtils.getField(clazz, fieldName, true), data.get(fieldName));
		}
	}
	
	/**
	 * 设置属性
	 * 
	 * @param object 对象
	 * @param field 属性
	 * @param fieldValue 属性的值
	 */
	public static final void copy(Object object, Field field, Object fieldValue) {
		if(object == null || field == null || fieldValue == null) {
			return;
		}
		final Class<?> fieldType = field.getType();
		if(fieldType.isAssignableFrom(fieldValue.getClass())) {
			// 相同支持类型
		} else {
			// 不同类型转换
			if(Long.class.isAssignableFrom(fieldType)) {
				fieldValue = Long.valueOf(fieldValue.toString());
			} else if(Date.class.isAssignableFrom(fieldType)) {
				final LocalDateTime localDateTime = DateUtils.parse(fieldValue.toString());
				if(localDateTime != null) {
					fieldValue = DateUtils.toDate(localDateTime);
				} else {
					log.info("不支持的日期格式：{}-{}-{}", fieldType, field, fieldValue);
					return;
				}
			} else if(String.class.isAssignableFrom(fieldType)) {
				fieldValue = fieldValue.toString();
			} else if(Integer.class.isAssignableFrom(fieldType)) {
				fieldValue = Integer.valueOf(fieldValue.toString());
			} else if(Boolean.class.isAssignableFrom(fieldType)) {
				fieldValue = Boolean.valueOf(fieldValue.toString());
			} else if(LocalDateTime.class.isAssignableFrom(fieldType)) {
				fieldValue = DateUtils.parse(fieldValue.toString());
			} else {
				log.warn("不支持的属性类型：{}-{}-{}", fieldType, field, fieldValue);
				return;
			}
		}
		// 属性的值为空忽略
		if(fieldValue == null) {
			return;
		}
		try {
			field.set(object, fieldValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("设置属性异常：{}-{}-{}", fieldType, field, fieldValue);
		}
	}
	
}
