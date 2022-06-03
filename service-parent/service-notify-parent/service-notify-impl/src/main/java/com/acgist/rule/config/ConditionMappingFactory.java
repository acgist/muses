package com.acgist.rule.config;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.acgist.rule.condition.ICondition;
import com.acgist.rule.type.ConditionOperation;

import lombok.extern.slf4j.Slf4j;

/**
 * 枚举映射工厂
 * 
 * @author acgist
 */
@Slf4j
@Component
public class ConditionMappingFactory {

	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		this.context.getBeansOfType(ICondition.class).forEach((name, instance) -> {
			log.info("创建规则条件和条件执行器映射：{}-{}", instance.getConditionOperation(), name);
			this.conditionMapping.put(instance.getConditionOperation(), instance);
		});
	}

	/**
	 * 规则条件和条件执行器映射：单例
	 * 
	 * @see #init()
	 */
	private Map<ConditionOperation, ICondition> conditionMapping = new EnumMap<>(ConditionOperation.class) {
		private static final long serialVersionUID = 1L;
		{
//			this.put(ConditionOperation.EQ, new EqCondition());
//			this.put(ConditionOperation.NE, new NeCondition());
//			this.put(ConditionOperation.LT, new LtCondition());
//			this.put(ConditionOperation.GT, new GtCondition());
//			this.put(ConditionOperation.LE, new LeCondition());
//			this.put(ConditionOperation.GE, new GeCondition());
//			this.put(ConditionOperation.LIKE, new InCondition());
//			this.put(ConditionOperation.NOT_LIKE, new InCondition());
//			this.put(ConditionOperation.IN, new InCondition());
//			this.put(ConditionOperation.NOT_IN, new NotInCondition());
//			this.put(ConditionOperation.INCLUDE, new IncludeCondition());
//			this.put(ConditionOperation.EXCLUDE, new ExcludeCondition());
		}
	};
	
	/**
	 * 获取条件执行器
	 * 
	 * @param operation 条件操作类型
	 * 
	 * @return 条件执行器
	 */
	public ICondition getCondition(ConditionOperation operation) {
		return this.conditionMapping.get(operation);
	}
	
}

