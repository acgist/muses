package com.acgist.concurrent;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务执行器
 * 
 * @author acgist
 *
 * @param <I> 输入信息
 * @param <O> 输出信息
 */
@Getter
@Setter
public abstract class Executor<I, O> {

	/**
	 * 回滚类型
	 * 
	 * @author acgist
	 */
	public enum RollbackType {
		
		// 回滚所有
		ALL,
		// 回滚成功
		SUCCESS,
		// 回滚最后成功
		LAST_SUCCESS;
		
	}
	
	/**
	 * 输入信息
	 */
	protected I input;
	/**
	 * 输出信息
	 */
	protected O output;
	/**
	 * 是否成功
	 */
	protected boolean success;
	/**
	 * 是否回滚
	 */
	protected boolean rollback;
	/**
	 * 下一个执行器
	 */
	protected Executor<O, ?> executor;
	/**
	 * 回滚类型
	 */
	protected final RollbackType rollbackType;

	protected Executor(RollbackType rollbackType) {
		this.rollbackType = rollbackType;
	}
	
	/**
	 * 判断是否执行成功
	 * 
	 * @return 是否成功
	 */
	public boolean success() {
		if(this.success && this.executor != null) {
			return this.executor.success();
		}
		return this.success;
	}
	
	/**
	 * 执行任务
	 * 
	 * @return 输出信息
	 */
	public O execute() {
		return this.execute(this.input);
	}
	
	/**
	 * 执行任务
	 * 
	 * @param input 输入信息
	 * 
	 * @return 输出信息
	 */
	protected O execute(I input) {
		if(this.input != input) {
			this.input = input;
		}
		this.output = this.doExecute();
		if(this.success && this.executor != null) {
			this.executor.execute(this.output);
		}
		return this.output;
	}
	
	/**
	 * 执行任务
	 */
	protected abstract O doExecute();
	
	/**
	 * 回滚任务
	 * 
	 * @return 是否回滚成功
	 */
	public boolean rollback() {
		switch (this.rollbackType) {
		case ALL:
			return this.rollbackAll();
		case SUCCESS:
			return this.rollbackSuccess();
		case LAST_SUCCESS:
			return this.rollbackLastSuccess();
		default:
			return false;
		}
	}
	
	/**
	 * 回滚所有任务
	 * 
	 * @return 是否回滚成功
	 */
	protected boolean rollbackAll() {
		boolean success = true;
		if(this.executor != null) {
			success = this.executor.rollback() && success;
		}
		success = this.doRollback() && success;
		return success;
	}

	/**
	 * 回滚成功任务
	 * 
	 * @return 是否回滚成功
	 */
	protected boolean rollbackSuccess() {
		boolean success = true;
		if(this.executor != null) {
			success = this.executor.rollback() && success;
		}
		if(this.success) {
			success = this.doRollback() && success;
		}
		return success;
	}
	
	/**
	 * 回滚最后成功任务
	 * 
	 * @return 是否回滚成功
	 */
	protected boolean rollbackLastSuccess() {
		if(this.executor != null) {
			if(this.success && !this.executor.success()) {
				return this.doRollback();
			} else {
				return this.executor.rollback();
			}
		} else {
			if(this.success) {
				return this.doRollback();
			} else {
				return true;
			}
		}
	}

	/**
	 * 执行回滚
	 * 
	 * @return 是否回滚成功
	 */
	protected abstract boolean doRollback();
	
}
