package com.acgist.data;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.acgist.common.JSONUtils;
import com.acgist.data.entity.DataEntity;

/**
 * 父类：VO/DTO
 * 
 * @author acgist
 *
 * @param <T> Entity
 */
public abstract class EntityConver<T extends DataEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	/**
	 * 属性拷贝
	 * 
	 * @param <I>
	 * @param <O>
	 * 
	 * @param source
	 * @param target
	 * 
	 * @return
	 */
	public <I, O> O copy(I source, O target) {
		BeanUtils.copyProperties(source, target);
		return target;
	}
	
	public abstract T toEntity();
	
	public abstract EntityConver<T> ofEntity(T entity);
	
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
