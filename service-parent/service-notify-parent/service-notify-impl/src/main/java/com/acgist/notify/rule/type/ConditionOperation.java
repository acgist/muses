package com.acgist.notify.rule.type;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;

import lombok.Getter;

/**
 * 比较条件
 * 
 * @author acgist
 */
@Getter
public enum ConditionOperation {

	// ==
	EQ("=="),
	// !=
	NE("!="),
	// <
	LT("<"),
	// >
	GT(">"),
	// <=
	LE("<="),
	// >=
	GE(">="),
	// like查询
	LIKE("like"),
	// notLike查询
	NOT_LIKE("notLike"),
	// 存在列表中（相等判断）：[a, b, c, d]
	IN("in"),
	// 不在列表中（相等判断）：[a, b, c, d]
	NOT_IN("notIn"),
	// 包含文本（包含判断）：[a, b, c, d]
	INCLUDE("include"),
	// 排除文本（包含判断）：[a, b, c, d]
	EXCLUDE("exclude");
	
	/**
	 * 别名
	 */
	private final String alias;
	
	/**
	 * @param alias 别名
	 */
	private ConditionOperation(String alias) {
		this.alias = alias;
	}

	/**
	 * @return 序列化
	 */
	public String serialize() {
		return " " + this.name() + " ";
	}
	
	/**
	 * @return 比较条件
	 */
	public static final ConditionOperation of(String value) {
		final ConditionOperation[] values = ConditionOperation.values();
		for (ConditionOperation conditionOperation : values) {
			if(
				conditionOperation.alias.equalsIgnoreCase(value.trim()) ||
				conditionOperation.name().equalsIgnoreCase(value.trim())
			) {
				return conditionOperation;
			}
		}
		throw new MessageCodeException(MessageCode.CODE_9999, "不支持的比较条件：" + value);
	}
	
}
