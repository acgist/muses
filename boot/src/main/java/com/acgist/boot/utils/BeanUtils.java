package com.acgist.boot.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

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
	
}
