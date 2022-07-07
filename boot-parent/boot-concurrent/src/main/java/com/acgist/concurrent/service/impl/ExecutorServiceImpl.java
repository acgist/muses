package com.acgist.concurrent.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.acgist.concurrent.executor.Executor;
import com.acgist.concurrent.service.ExecutorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecutorServiceImpl implements ExecutorService {

	@Autowired
	private TaskExecutor taskExecutor;
	
	@Override
	public boolean execute(Executor<?, ?> ... executors) {
		final List<Executor<?, ?>> list = Stream.of(executors).collect(Collectors.toList());
		final CompletableFuture<Void> future = CompletableFuture.allOf(
			list.stream()
				.map(executor -> CompletableFuture.supplyAsync(this.execute(executor), this.taskExecutor))
				.collect(Collectors.toList())
				.toArray(CompletableFuture[]::new)
		);
		try {
			future.get();
		} catch (Exception e) {
			log.error("任务执行异常", e);
		}
		return list.stream().map(Executor::allSuccess).allMatch(Boolean.TRUE::equals);
	}

	@Override
	public boolean executeRollbackUnsuccess(Executor<?, ?> ... executors) {
		final boolean success = this.execute(executors);
		if(!success) {
			this.rollback(executors);
		}
		return success;
	}
	
	@Override
	public boolean rollback(Executor<?, ?> ... executors) {
		final List<Executor<?, ?>> list = Stream.of(executors).collect(Collectors.toList());
		final CompletableFuture<Void> future = CompletableFuture.allOf(
			list.stream()
				.map(executor -> CompletableFuture.supplyAsync(this.rollback(executor), this.taskExecutor))
				.collect(Collectors.toList())
				.toArray(CompletableFuture[]::new)
		);
		try {
			future.get();
		} catch (Exception e) {
			log.error("任务回滚异常", e);
		}
		return list.stream().map(Executor::allRollback).allMatch(Boolean.TRUE::equals);
	}

	/**
	 * 执行任务
	 * 
	 * @param executor 任务执行器
	 * 
	 * @return supplier
	 */
	private Supplier<Boolean> execute(Executor<?, ?> executor) {
		return () -> {
			executor.execute();
			return executor.allSuccess();
		};
	}
	
	/**
	 * 回滚任务
	 * 
	 * @param executor 任务执行器
	 * 
	 * @return supplier
	 */
	private Supplier<Boolean> rollback(Executor<?, ?> executor) {
		return () -> {
			executor.rollback();
			return executor.allRollback();
		};
	}
	
}
