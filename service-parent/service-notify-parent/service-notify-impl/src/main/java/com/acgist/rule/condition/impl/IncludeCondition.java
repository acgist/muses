package com.acgist.rule.condition.impl;

import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.rule.condition.Condition;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 包含文本条件
 * 
 * @author acgist
 */
@Component
public class IncludeCondition extends Condition {

	public IncludeCondition() {
		super(ConditionOperation.INCLUDE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.getList(fieldClazz, fieldValue).stream()
			.anyMatch(value -> this.contains(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		this.getList(fieldClazz, fieldValue).forEach(item -> {
			Filter.Type.LIKE.of(field, item).filter(entityClazz, wrapper.or());
		});
	}
	
}
