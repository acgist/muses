package com.acgist.notify.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.notify.rule.condition.Condition;
import com.acgist.notify.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 否定模糊条件
 * 
 * @author acgist
 */
@Component
public class NotLikeCondition extends Condition {

	public NotLikeCondition() {
		super(ConditionOperation.NOT_LIKE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return !this.contains(fieldValueRt, fieldValue);
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.NOT_LIKE.of(field, this.getObject(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
