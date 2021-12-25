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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务执行器
 * 
 * @author acgist
 */
public class Executors {

	private static final AtomicInteger INDEX = new AtomicInteger();
	private static final Logger LOGGER = LoggerFactory.getLogger(Executors.class);
	private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
		10,
		20,
		30,
		TimeUnit.SECONDS,
		new LinkedBlockingQueue<>(1000),
		runnable -> {
			final Thread thread = new Thread(runnable);
			thread.setName(String.format("EXECUTORS-%02d", INDEX.getAndIncrement()));
			thread.setDaemon(true);
			return thread;
		}
	);
	
	private static final Supplier<Boolean> execute(Executor<?, ?> executor) {
		return () -> {
			executor.execute();
			return executor.success();
		};
	}
	
	private static final Supplier<Boolean> rollback(Executor<?, ?> executor) {
		return () -> {
			return executor.rollback();
		};
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
			LOGGER.error("任务执行异常", e);
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
		final List<Supplier<Boolean>> stream = Stream.of(executors).map(Executors::rollback).collect(Collectors.toList());
		final CompletableFuture<Void> future = CompletableFuture.allOf(
			stream.stream().map(executor -> CompletableFuture.supplyAsync(executor, EXECUTOR))
				.collect(Collectors.toList())
				.toArray(CompletableFuture[]::new)
		);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("任务回滚异常", e);
		}
	}
	
}
