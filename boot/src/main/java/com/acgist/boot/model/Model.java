package com.acgist.boot.model;

import java.io.Serializable;

import com.acgist.boot.utils.JSONUtils;

/**
 * 对象属性复制
 * 
 * @author acgist
 */
public abstract class Model implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
