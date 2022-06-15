package com.acgist.notify.rule.condition.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.acgist.model.query.FilterQuery.Filter;
import com.acgist.notify.rule.condition.Condition;
import com.acgist.notify.rule.type.ConditionOperation;
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
		final List<?> list = this.getList(fieldClazz, fieldValue);
		if(CollectionUtils.isEmpty(list)) {
			return true;
		}
		return list.stream()
			.anyMatch(value -> this.equals(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		final List<?> list = this.getList(fieldClazz, fieldValue);
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		Filter.Type.IN.of(field, list).filter(entityClazz, wrapper);
	}
	
}
