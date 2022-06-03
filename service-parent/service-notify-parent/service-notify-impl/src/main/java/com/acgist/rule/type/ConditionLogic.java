package com.acgist.rule.type;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;

import lombok.Getter;

/**
 * 比较逻辑
 * 
 * @author acgist
 */
@Getter
public enum ConditionLogic {

	// ||
	OR("||"),
	// &&
	AND("&&");

	/**
	 * 别名
	 */
	private final String alias;

	/**
	 * @param alias 别名
	 */
	private ConditionLogic(String alias) {
		this.alias = alias;
	}
	
	/**
	 * @return 序列化
	 */
	public String serialize() {
		return " " + this.alias + " ";
	}
	
	/**
	 * @return 比较逻辑
	 */
	public static final ConditionLogic of(char value) {
		final ConditionLogic[] values = ConditionLogic.values();
		for (ConditionLogic conditionLogic : values) {
			if(conditionLogic.alias.charAt(0) == value) {
				return conditionLogic;
			}
		}
		throw new MessageCodeException(MessageCode.CODE_9999, "不支持的比较逻辑：" + value);
	}

}