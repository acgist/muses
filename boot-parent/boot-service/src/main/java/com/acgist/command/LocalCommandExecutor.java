package com.acgist.command;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.acgist.command.StreamGobbler.Type;

import lombok.extern.slf4j.Slf4j;

/**
 * 本地命令执行器
 * 
 * @author acgist
 */
@Slf4j
public class LocalCommandExecutor implements CommandExecutor {

	/**
	 * 编码
	 */
	private final String charset;

	/**
	 * 线程池
	 */
	private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

	public LocalCommandExecutor() {
		this(StandardCharsets.UTF_8.name());
	}

	public LocalCommandExecutor(String charset) {
		this.charset = charset;
	}

	@Override
	public CommandResult execute(String command, long timeout) {
		Process process = null;
		Future<Integer> future = null;
		OutputStream outputStream = null;
		StreamGobbler outputGobbler = null;
		StreamGobbler errorGobbler = null;
		try {
			log.info("执行命令：{}-{}", timeout, command);
			process = Runtime.getRuntime().exec(command);
			final Process finalProcess = process;
			outputGobbler = new StreamGobbler(Type.OUTPUT, this.charset, finalProcess.getInputStream());
			outputGobbler.start();
			errorGobbler = new StreamGobbler(Type.ERROR, this.charset, finalProcess.getErrorStream());
			errorGobbler.start();
			outputStream = finalProcess.getOutputStream();
			future = EXECUTOR.submit(() -> {
				finalProcess.waitFor();
				return finalProcess.exitValue();
			});
			final int exitCode = future.get(timeout, TimeUnit.MILLISECONDS);
			String content;
			if (exitCode == 0) {
				content = outputGobbler.getContent(timeout);
			} else {
				content = errorGobbler.getContent(timeout);
			}
			return new CommandResult(exitCode, content);
		} catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
			log.error("命令执行异常：{}", command, e);
			return new CommandResult(-1, e.getMessage());
		} finally {
			// 优先关闭
			if (process != null) {
				process.destroy();
			}
			if (future != null && !(future.isDone() || future.isCancelled())) {
				try {
					future.cancel(true);
				} catch (Exception e) {
					log.error("命令释放异常", e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("命令释放异常", e);
				}
			}
			if (outputGobbler != null) {
				outputGobbler.shutdown();
			}
			if (errorGobbler != null) {
				errorGobbler.shutdown();
			}
		}
	}

}
