package com.acgist.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.rule.condition.Condition;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 在列表中条件
 * 
 * @author acgist
 */
@Component
public class InCondition extends Condition {

	public InCondition() {
		super(ConditionOperation.IN);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.getList(fieldClazz, fieldValue).stream()
			.anyMatch(value -> this.equals(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.IN.of(field, this.getList(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
