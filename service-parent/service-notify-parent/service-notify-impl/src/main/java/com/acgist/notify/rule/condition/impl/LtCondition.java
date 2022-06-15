package com.acgist.notify.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.notify.rule.condition.Condition;
import com.acgist.notify.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 小于条件
 * 
 * @author acgist
 */
@Component
public class LtCondition extends Condition {

	public LtCondition() {
		super(ConditionOperation.LT);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.compare(fieldValue, fieldClazz, fieldValueRt) < 0;
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.LT.of(field, getObject(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
