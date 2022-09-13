package com.acgist.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 命令执行结果
 * 
 * @author acgist
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandResult {

	/**
	 * 退出编码
	 */
	private int exitCode;
	/**
	 * 执行结果
	 */
	private String content;

}
