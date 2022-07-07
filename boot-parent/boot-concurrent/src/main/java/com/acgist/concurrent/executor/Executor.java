package com.acgist.concurrent.executor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务执行器
 * 
 * @author acgist
 *
 * @param <I> 输入信息
 * @param <O> 输出信息
 */
@Slf4j
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
	 * 任务名称
	 */
	protected final String name;
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
	 * 回滚类型
	 */
	protected final RollbackType rollbackType;
	/**
	 * 下一个执行器
	 * 
	 * 如果是链式执行器回滚操作时只需要执行第一个执行器的回滚方法即可
	 */
	protected Executor<O, ?> executor;
	
	protected Executor(String name, RollbackType rollbackType) {
		this.name = name;
		this.rollbackType = rollbackType;
		this.success = false;
		this.rollback = false;
	}
	
	/**
	 * 判断是否全部执行成功
	 * 
	 * @return 是否全部执行成功
	 */
	public boolean allSuccess() {
		if(this.success && this.executor != null) {
			return this.executor.allSuccess();
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
		if(this.success) {
			return this.output;
		}
		if(this.input != input) {
			this.input = input;
		}
		// 执行任务
		try {
			log.info("任务执行：{}", this.name);
			this.output = this.doExecute();
			this.success = this.checkExecute();
		} catch (Exception e) {
			log.error("任务执行异常：{}", this.name, e);
			this.output = this.checkExecuteException();
		}
		// 执行结果
		if(this.success) {
			if(this.executor != null) {
				log.info("任务执行成功：{}-{}-执行下一个执行器", this.name, this.executor.name);
				this.executor.execute(this.output);
			} else {
				log.info("任务执行成功：{}", this.name);
			}
		} else {
			log.warn("任务执行失败：{}", this.name);
		}
		return this.output;
	}
	
	/**
	 * 执行任务
	 * 
	 * @return 执行结果
	 */
	protected abstract O doExecute();
	
	/**
	 * 判断执行是否成功
	 * 
	 * @return 是否成功
	 */
	protected abstract boolean checkExecute();
	
	/**
	 * 执行异常检查执行结果
	 * 
	 * @return 执行结果
	 */
	protected O checkExecuteException() {
		return null;
	}

	/**
	 * 判断是否全部回滚成功
	 * 
	 * @return 是否全部回滚成功
	 */
	public boolean allRollback() {
		if(this.rollback && this.executor != null) {
			return this.executor.allRollback();
		}
		return this.rollback;
	}
	
	/**
	 * 回滚任务
	 * 
	 * @return 是否回滚成功
	 */
	public boolean rollback() {
		if(this.rollback) {
			return this.rollback;
		}
		try {
			this.rollback =
				switch (this.rollbackType) {
				case ALL -> this.rollbackAll();
				case SUCCESS -> this.rollbackSuccess();
				case LAST_SUCCESS -> this.rollbackLastSuccess();
				default -> true;
				};
		} catch (Exception e) {
			log.error("任务回滚异常：{}-{}", this.name, this.rollbackType, e);
			this.rollback = this.checkRollbackException();
		}
		if(this.rollback) {
			log.info("任务回滚成功：{}-{}", this.name, this.rollbackType);
			return this.rollback;
		} else {
			log.warn("任务回滚失败：{}-{}-继续重试", this.name, this.rollbackType);
			return this.rollback();
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
		log.info("任务回滚：{}", this.name);
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
		if(this.executor != null && this.executor.success) {
			// 判断下一个执行器是否成功：没有成功不必递归向下调用
			success = this.executor.rollback() && success;
		}
		if(this.success) {
			log.info("任务回滚：{}", this.name);
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
			if(this.success && !this.executor.success) {
				log.info("任务回滚：{}", this.name);
				return this.doRollback();
			} else {
				return this.executor.rollback();
			}
		} else {
			if(this.success) {
				log.info("任务回滚：{}", this.name);
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
	
	/**
	 * 回滚异常检查回滚结果
	 * 
	 * @return 是否回滚成功
	 */
	protected boolean checkRollbackException() {
		return false;
	}
	
}
