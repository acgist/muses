package com.acgist.command;

/**
 * 命令执行器
 * 
 * @author acgist
 */
public interface CommandExecutor {

	/**
	 * 默认超时时间
	 */
	long TIMEOUT = 60L * 1000;

	/**
	 * 执行命令
	 * 
	 * @param command 命令
	 * 
	 * @return 执行结果
	 */
	default CommandResult execute(String command) {
		return this.execute(command, TIMEOUT);
	}
	
	/**
	 * 执行命令
	 * 
	 * @param command 命令
	 * @param args 命令参数
	 * 
	 * @return 执行结果
	 */
	default CommandResult execute(String command, String ... args) {
		return this.execute(command + " " + String.join(" ", args), TIMEOUT);
	}
	
	/**
	 * 执行命令
	 * 
	 * @param command 命令
	 * @param timeout 超时时间
	 * 
	 * @return 执行结果
	 */
	CommandResult execute(String command, long timeout);

}
