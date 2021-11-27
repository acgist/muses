package com.acgist.concurrent;

/**
 * 任务执行器
 * 
 * @author acgist
 *
 * @param <I> 输入信息
 * @param <O> 输出信息
 */
public abstract class Executor<I, O> {

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
		if(this.success) {
			return this.executor.success();
		}
		return false;
	}

	/**
	 * 执行任务
	 */
	public void execute() {
		this.execute(this.input);
	}
	
	/**
	 * 执行任务
	 * 
	 * @param input 输入信息
	 */
	public void execute(I input) {
		if(this.input != input) {
			this.input = input;
		}
		this.doExecute();
		if(this.executor != null) {
			this.executor.execute(this.output);
		}
	}
	
	/**
	 * 执行任务
	 */
	public abstract void doExecute();
	
	/**
	 * 回滚任务
	 */
	public void rollback() {
		switch (this.rollbackType) {
		case ALL:
			this.rollbackAll();
			break;
		case SUCCESS:
			this.rollbackSuccess();
			break;
		case LAST_SUCCESS:
			this.rollbackLastSuccess();
			break;
		}
	}
	
	/**
	 * 回滚所有任务
	 */
	public void rollbackAll() {
		if(this.executor != null) {
			this.executor.rollback();
		}
		this.doRollback();
	}

	/**
	 * 回滚成功任务
	 */
	public void rollbackSuccess() {
		if(this.executor != null) {
			this.executor.rollback();
		}
		if(this.success) {
			this.doRollback();
		}
	}
	
	/**
	 * 回滚最后成功任务
	 */
	public void rollbackLastSuccess() {
		if(this.executor != null) {
			this.executor.rollback();
		}
		if(this.executor != null) {
			if(this.success && !this.executor.success()) {
				this.doRollback();
			}
		} else {
			if(this.success) {
				this.doRollback();
			}
		}
	}

	/**
	 * 执行回滚
	 */
	public abstract void doRollback();
	
	public I getInput() {
		return input;
	}

	public void setInput(I input) {
		this.input = input;
	}

	public O getOutput() {
		return output;
	}

	public void setOutput(O output) {
		this.output = output;
	}

}
