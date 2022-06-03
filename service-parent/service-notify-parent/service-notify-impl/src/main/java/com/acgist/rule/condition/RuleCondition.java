package com.acgist.rule.condition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.acgist.boot.utils.SpringUtils;
import com.acgist.rule.config.ConditionMapping;
import com.acgist.rule.config.ConditionMappingFactory;
import com.acgist.rule.type.ConditionLogic;
import com.acgist.rule.type.ConditionOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.Getter;
import lombok.Setter;

/**
 * 过滤规则条件
 * 
 * @author yusheng
 */
@Getter
@Setter
public class RuleCondition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 完整条件
	 */
	private String condition;
	/**
	 * 条件字段
	 */
	private String field;
	/**
	 * 条件值：
	 * 单值：1
	 * 列表：[1, 2]
	 */
	private String value;
	/**
	 * 比较逻辑
	 */
	private ConditionLogic conditionLogic = ConditionLogic.AND;
	/**
	 * 比较条件
	 */
	private ConditionOperation conditionOperation;
	/**
	 * 枝叶
	 */
	private List<RuleCondition> leaf;
	
	/**
	 * 设置完整条件
	 * 
	 * @param condition 完整条件
	 */
	public void buildCondition(String condition) {
		this.condition = condition.trim();
		int index = 0;
		this.field = this.condition.substring(index, index = this.condition.indexOf(' ', index)).trim();
		while(this.condition.charAt(index) == ' ') {
			index++;
		}
		final String value = this.condition.substring(index, index = this.condition.indexOf(' ', index)).trim();
		this.conditionOperation = ConditionOperation.of(value);
		while(this.condition.charAt(index) == ' ') {
			index++;
		}
		this.value = this.condition.substring(index).trim();
	}
	
	/**
	 * 获取完整条件
	 * 
	 * @param transfer 是否翻译
	 * 
	 * @return 完整条件
	 */
	public String buildCondition(boolean transfer) {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(transfer ? SpringUtils.getBean(ConditionMapping.class).transfer(this.field) : this.field)
			.append(this.conditionOperation.serialize())
			.append(this.value);
		return builder.toString();
	}
	
	/**
	 * 添加枝叶规则
	 * 
	 * @param children 枝叶规则
	 */
	public void addLeaf(RuleCondition children) {
		if(this.leaf == null) {
			this.leaf = new ArrayList<>();
		}
		this.leaf.add(children);
	}
	
	/**
	 * 验证规则
	 * 
	 * @return 规则是否正确
	 */
	public boolean verify() {
		return StringUtils.isNotEmpty(this.condition) || CollectionUtils.isNotEmpty(this.leaf);
	}

	/**
	 * 过滤
	 * 
	 * @param object 过滤对象
	 * @param mappingFactory 映射工厂
	 * 
	 * @return 过滤结果
	 */
	public boolean filter(Object object, ConditionMappingFactory mappingFactory) {
		if(CollectionUtils.isEmpty(this.leaf)) {
			if(this.conditionOperation == null) {
				return true;
			} else {
				return mappingFactory.getCondition(this.conditionOperation).filter(object, this);
			}
		} else {
			if (this.conditionLogic == ConditionLogic.OR) {
				return this.leaf.stream()
					.map(condition -> condition.filter(object, mappingFactory))
					.anyMatch(value -> value);
			} else {
				return this.leaf.stream()
					.map(condition -> condition.filter(object, mappingFactory))
					.allMatch(value -> value);
			}
		}
	}

	/**
	 * 过滤
	 * 
	 * @param clazz 类型
	 * @param wrapper wrapper
	 * @param mappingFactory 映射工厂
	 */
	public <T> void buildWrapper(Class<T> clazz, QueryWrapper<T> wrapper, ConditionMappingFactory mappingFactory) {
		if(CollectionUtils.isEmpty(this.leaf)) {
			if(this.conditionOperation == null) {
				return;
			} else {
				mappingFactory.getCondition(this.conditionOperation).buildWrapper(clazz, wrapper, this);
			}
		} else {
			if (this.conditionLogic == ConditionLogic.OR) {
				this.leaf.forEach(condition -> {
					wrapper.or(orWrapper -> condition.buildWrapper(clazz, orWrapper, mappingFactory));
				});
			} else {
				this.leaf.forEach(condition -> {
					wrapper.and(orWrapper -> condition.buildWrapper(clazz, orWrapper, mappingFactory));
				});
			}
		}
	}
	
	/**
	 * @return 序列化
	 */
	public String serialize() {
		final String serialize = this.serialize(false);
		if(StringUtils.isNotEmpty(serialize)) {
			// 去掉首尾
			return serialize.substring(1, serialize.length() - 1);
		} else {
			return serialize;
		}
	}
	
	/**
	 * @return 序列化
	 */
	public String serializeTransfer() {
		final String serialize = this.serialize(true);
		if(StringUtils.isNotEmpty(serialize)) {
			// 去掉首尾
			return serialize.substring(1, serialize.length() - 1);
		} else {
			return serialize;
		}
	}
	
	/**
	 * @param transfer 是否翻译
	 * 
	 * @return 序列化
	 */
	private String serialize(boolean transfer) {
		if(CollectionUtils.isEmpty(this.leaf)) {
			return null;
		}
		return this.leaf.stream()
			.map(value -> {
				if(CollectionUtils.isEmpty(value.getLeaf())) {
					return value.buildCondition(transfer);
				} else {
					return value.serialize(transfer);
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.joining(this.conditionLogic.serialize(), "(", ")"));
	}
	
	/**
	 * 解析条件
	 * 
	 * @param rule 规则条件
	 * 
	 * @return 条件规则
	 */
	public static final RuleCondition deserialization(String rule) {
		if(StringUtils.isEmpty(rule)) {
			return null;
		}
		final RuleCondition root = new RuleCondition();
		deserialization(new AtomicInteger(), rule, root);
		return root;
	}
	
	/**
	 * 解析树形结构
	 * 
	 * @param index 解析位置
	 * @param rule 规则条件
	 * @param root 上级规则条件
	 */
	private static final void deserialization(AtomicInteger index, String rule, RuleCondition root) {
		final StringBuilder builder = new StringBuilder();
		while(true && rule.length() > index.get()) {
			final char value = rule.charAt(index.getAndIncrement());
			if(value == '\n' || value == '\r') {
				// 忽略
				continue;
			} else if(value == '\\') {
				// 转义
				builder
					.append(value)
					.append(rule.charAt(index.getAndIncrement()));
				continue;
			} else if(value == ' ') {
				// 空格
				builder.append(value);
				continue;
			} else if(value == '&' || value == '|') {
				// 规则
				buildLeaf(builder, root);
				// 逻辑：注意只有最后一个逻辑
				root.setConditionLogic(ConditionLogic.of(value));
			} else if(value == '(') {
				// 规则
				buildLeaf(builder, root);
				// 下级
				final RuleCondition leaf = new RuleCondition();
				deserialization(index, rule, leaf);
				// 验证有效节点
				if(leaf.verify()) {
					root.addLeaf(leaf);
				}
			} else if(value == ')') {
				// 规则
				buildLeaf(builder, root);
				// 结束
				return;
			} else {
				builder.append(value);
			}
		}
		buildLeaf(builder, root);
	}
	
	/**
	 * 添加叶子节点
	 * 
	 * @param builder 条件内容
	 * @param root 上级规则条件
	 */
	private static final void buildLeaf(StringBuilder builder, RuleCondition root) {
		final String condition = builder.toString().trim();
		if(condition.length() > 0) {
			final RuleCondition last = new RuleCondition();
			last.buildCondition(condition);
			root.addLeaf(last);
		}
		builder.setLength(0);
	}
	
}
