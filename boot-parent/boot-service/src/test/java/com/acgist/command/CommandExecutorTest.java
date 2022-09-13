package com.acgist.command;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandExecutorTest {

	@Test
	public void testCommand() {
		final CommandExecutor executor = new LocalCommandExecutor("GBK");
//		final CommandResult result = executor.execute("ls", "-ano");
//		log.info("执行结果：{}-{}", result.getExitCode(), result.getContent());
//		final CommandResult result = executor.execute("java", "-ano");
//		log.info("执行结果：{}-{}", result.getExitCode(), result.getContent());
		final CommandResult result = executor.execute("java");
		log.info("执行结果：{}-{}", result.getExitCode(), result.getContent());
//		final CommandResult result = executor.execute("netstat", 2000);
//		log.info("执行结果：{}-{}", result.getExitCode(), result.getContent());
//		final CommandResult result = executor.execute("netstat", "-ano");
//		log.info("执行结果：{}-{}", result.getExitCode(), result.getContent());
	}
	
}
