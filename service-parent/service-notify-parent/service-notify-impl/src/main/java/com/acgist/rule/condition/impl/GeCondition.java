package com.acgist.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.rule.condition.Condition;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 大于等于条件
 * 
 * @author acgist
 */
@Component
public class GeCondition extends Condition {

	public GeCondition() {
		super(ConditionOperation.GE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.compare(fieldValue, fieldClazz, fieldValueRt) > -1;
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.GE.of(field, this.getObject(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
