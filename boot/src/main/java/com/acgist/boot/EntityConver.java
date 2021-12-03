package com.acgist.boot;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

/**
 * VO/DTO
 * 
 * @author acgist
 *
 * @param <T> Entity
 */
public abstract class EntityConver<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	/**
	 * 属性拷贝
	 * 
	 * @param <I> 输入对象类型
	 * @param <O> 输出对象类型
	 * 
	 * @param source 输入对象
	 * @param target 输出对象
	 * 
	 * @return 输出对象
	 */
	public <I, O> O copy(I source, O target) {
		BeanUtils.copyProperties(source, target);
		return target;
	}
	
	/**
	 * 转换实体
	 * 
	 * @return 实体
	 */
	public abstract T toEntity();
	
	/**
	 * 实体转换
	 * 
	 * @param entity 实体
	 * 
	 * @return VO/DTO
	 */
	public abstract EntityConver<T> ofEntity(T entity);
	
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
