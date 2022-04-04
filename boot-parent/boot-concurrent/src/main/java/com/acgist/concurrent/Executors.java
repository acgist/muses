package com.acgist.concurrent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * 任务执行器
 * 
 * @author acgist
 */
@Slf4j
public class Executors {

	/**
	 * 线程计数器
	 */
	private static final AtomicInteger INDEX = new AtomicInteger();
	/**
	 * 线程池
	 */
	private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
		10,
		20,
		30,
		TimeUnit.SECONDS,
		new LinkedBlockingQueue<>(100000),
		runnable -> {
			final Thread thread = new Thread(runnable);
			thread.setName(String.format("EXECUTORS-%02d", INDEX.getAndIncrement()));
			thread.setDaemon(true);
			return thread;
		}
	);
	
	/**
	 * 执行任务
	 * 
	 * @param executor 任务执行器
	 * 
	 * @return supplier
	 */
	private static final Supplier<Boolean> execute(Executor<?, ?> executor) {
		return () -> {
			executor.execute();
			return executor.success();
		};
	}
	
	/**
	 * 回滚任务
	 * 
	 * @param executor 任务执行器
	 * 
	 * @return supplier
	 */
	private static final Supplier<Boolean> rollback(Executor<?, ?> executor) {
		return () -> executor.rollback();
	}
	
	/**
	 * 执行任务
	 * 
	 * @param executors 任务执行器
	 * 
	 * @return 是否成功
	 */
	public static final boolean execute(Executor<?, ?> ... executors) {
		final List<Executor<?, ?>> list = Stream.of(executors).collect(Collectors.toList());
		final CompletableFuture<Void> future = CompletableFuture.allOf(
			list.stream().map(executor -> CompletableFuture.supplyAsync(Executors.execute(executor), EXECUTOR))
				.collect(Collectors.toList())
				.toArray(CompletableFuture[]::new)
		);
		boolean success = true;
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("任务执行异常", e);
		} finally {
			success = list.stream().map(Executor::success).allMatch(Boolean.TRUE::equals);
			if(!success) {
				rollback(executors);
			}
		}
		return success;
	}
	
	/**
	 * 任务回滚
	 * 
	 * @param executors 任务执行器
	 */
	private static final void rollback(Executor<?, ?> ... executors) {
		final List<Executor<?, ?>> list = Stream.of(executors).collect(Collectors.toList());
		final CompletableFuture<Void> future = CompletableFuture.allOf(
			list.stream().map(executor -> CompletableFuture.supplyAsync(Executors.rollback(executor), EXECUTOR))
				.collect(Collectors.toList())
				.toArray(CompletableFuture[]::new)
		);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("任务回滚异常", e);
		}
	}
	
}
