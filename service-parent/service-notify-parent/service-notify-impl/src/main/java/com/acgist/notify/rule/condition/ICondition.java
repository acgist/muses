package com.acgist.notify.rule.condition;

import com.acgist.notify.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 条件判断
 * 
 * @author acgist
 */
public interface ICondition {
	
	/**
	 * @return 条件操作类型
	 */
	ConditionOperation getConditionOperation();

	/**
	 * 过滤规则
	 * 
	 * @param source 过滤对象
	 * @param condition 条件
	 * 
	 * @return 是否匹配
	 */
	boolean filter(Object source, RuleCondition condition);
	
	/**
	 * 创建查询条件
	 * 
	 * @param entityClazz 查询类型
	 * @param wrapper 查询条件
	 * @param condition 条件
	 */
	<T> void buildWrapper(Class<T> entityClazz, QueryWrapper<T> wrapper, RuleCondition condition);
	
}
