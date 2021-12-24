package com.acgist.boot.pojo.bean;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.acgist.boot.JSONUtils;

/**
 * 对象属性复制
 * 
 * @author acgist
 */
public abstract class PojoCopy implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 属性拷贝
	 * 
	 * @param object 输拷贝对象
	 * 
	 * @return this
	 */
	public PojoCopy copy(Object object) {
		BeanUtils.copyProperties(object, this);
		return this;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
