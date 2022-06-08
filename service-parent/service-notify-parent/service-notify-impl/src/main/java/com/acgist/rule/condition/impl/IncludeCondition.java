package com.acgist.rule.condition.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
		final List<?> list = this.getList(fieldClazz, fieldValue);
		if(CollectionUtils.isEmpty(list)) {
			return true;
		}
		return list.stream()
			.anyMatch(value -> this.contains(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		final List<?> list = this.getList(fieldClazz, fieldValue);
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		list.forEach(item -> {
			Filter.Type.LIKE.of(field, item).filter(entityClazz, wrapper.or());
		});
	}
	
}
