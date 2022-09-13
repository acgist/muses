package com.acgist.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

/**
 * 命令执行结果收集线程
 * 
 * @author acgist
 */
@Slf4j
public class StreamGobbler extends Thread {

	/**
	 * 收集类型
	 * 
	 * @author acgist
	 */
	public enum Type {

		/**
		 * 正常
		 */
		OUTPUT,
		/**
		 * 异常
		 */
		ERROR;

	}

	/**
	 * 命令执行结果类型
	 */
	private final Type type;
	/**
	 * 命令执行结果编码
	 */
	private final String charset;
	/**
	 * 命令执行结果数据缓冲
	 */
	private final StringBuilder builder;
	/**
	 * 命令执行结果输入流
	 */
	private final InputStream inputStream;
	/**
	 * 是否已经读取完成
	 */
	private volatile boolean finished = false;

	public StreamGobbler(final Type type, final String charset, final InputStream inputStream) {
		this.type = type;
		this.charset = charset;
		this.inputStream = inputStream;
		this.builder = new StringBuilder();
		this.setDaemon(true);
	}

	@Override
	public void run() {
		try {
			String line = null;
			final BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream, this.charset));
			while (!this.isInterrupted() && (line = reader.readLine()) != null) {
				this.builder.append(line).append("\r\n");
			}
		} catch (IOException e) {
			log.error("命令执行结果读取异常：{}", this.type, e);
		} finally {
			this.finished = true;
			synchronized (this) {
				this.notify();
			}
		}
	}

	/**
	 * @param timeout 超时时间
	 * 
	 * @return 执行结果
	 */
	public String getContent(long timeout) {
		if (!this.finished) {
			synchronized (this) {
				if (!this.finished) {
					try {
						this.wait(timeout);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						log.error("命令执行失败", e);
					}
				}
			}
		}
		return this.builder.toString();
	}

	/**
	 * 释放资源
	 */
	public void shutdown() {
		log.debug("释放资源：{}", this.type);
		this.finished = true;
		if (!this.isInterrupted()) {
			this.interrupt();
		}
		try {
			this.inputStream.close();
		} catch (IOException e) {
			log.error("命令释放异常", e);
		}
	}

}
