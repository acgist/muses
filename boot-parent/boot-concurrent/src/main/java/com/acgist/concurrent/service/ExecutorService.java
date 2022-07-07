package com.acgist.concurrent.service;

import com.acgist.concurrent.executor.Executor;

/**
 * 执行器
 * 
 * @author acgist
 */
public interface ExecutorService {

	/**
	 * 执行任务
	 * 
	 * @param executors 任务执行器
	 * 
	 * @return 是否成功
	 */
	boolean execute(Executor<?, ?> ... executors);
	
	/**
	 * 执行任务，执行失败回滚。
	 * 
	 * @param executors 任务执行器
	 * 
	 * @return 是否成功
	 */
	boolean executeRollbackUnsuccess(Executor<?, ?> ... executors);
	
	/**
	 * 任务回滚
	 * 
	 * @param executors 任务执行器
	 * 
	 * @return 是否成功
	 */
	boolean rollback(Executor<?, ?> ... executors);
	
}
