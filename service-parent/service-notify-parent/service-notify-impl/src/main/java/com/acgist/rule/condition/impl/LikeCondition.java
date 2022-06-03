package com.acgist.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.rule.condition.Condition;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 模糊条件
 * 
 * @author acgist
 */
@Component
public class LikeCondition extends Condition {

	public LikeCondition() {
		super(ConditionOperation.LIKE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.contains(fieldValueRt, fieldValue);
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.LIKE.of(field, this.getObject(fieldClazz, fieldValue)).filter(entityClazz, wrapper);
	}
	
}
