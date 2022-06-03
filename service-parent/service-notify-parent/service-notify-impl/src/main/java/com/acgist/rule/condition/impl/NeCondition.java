package com.acgist.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.rule.condition.Condition;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 不等条件
 * 
 * @author acgist
 */
@Component
public class NeCondition extends Condition {

	public NeCondition() {
		super(ConditionOperation.NE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return !this.equals(fieldValueRt, fieldValue);
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.NE.of(field, getObject(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
