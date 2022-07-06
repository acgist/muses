package com.acgist.concurrent;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.acgist.concurrent.executor.Executor;
import com.acgist.concurrent.executor.Executor.RollbackType;
import com.acgist.concurrent.executor.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecutorsTest {
	
	@Test
	public void testExecutor() {
//		final SIExecutor siExecutor = new SIExecutor(RollbackType.ALL);
//		final ISExecutor isExecutor = new ISExecutor(RollbackType.ALL);
//		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.ALL);
//		final SIExecutor siExecutor = new SIExecutor(RollbackType.SUCCESS);
//		final ISExecutor isExecutor = new ISExecutor(RollbackType.SUCCESS);
//		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.SUCCESS);
		final SIExecutor siExecutor = new SIExecutor(RollbackType.LAST_SUCCESS);
		final ISExecutor isExecutor = new ISExecutor(RollbackType.LAST_SUCCESS);
		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.LAST_SUCCESS);
		assertTrue(Executors.execute(siExecutor, nxExecutor));
//		assertTrue(Executors.execute(siExecutor, isExecutor, nxExecutor));
	}
	
	public static class SIExecutor extends Executor<String, Integer> {

		public SIExecutor(RollbackType rollbackType) {
			super(rollbackType);
		}
		
		public SIExecutor(ISExecutor executor, RollbackType rollbackType) {
			super(rollbackType);
			this.executor = executor;
		}

		@Override
		public Integer doExecute() {
			log.info("执行任务：{}", this);
			final Integer value = Integer.valueOf("100");
//			final Integer value = Integer.valueOf("AAA");
			this.success = true;
			return value;
		}

		@Override
		public boolean doRollback() {
			log.info("回滚任务：{}", this);
			this.rollback = true;
			return true;
		}
		
	}
	
	public static class ISExecutor extends Executor<Integer, String> {
		
		protected ISExecutor(RollbackType rollbackType) {
			super(rollbackType);
		}
		
		@Override
		public String doExecute() {
			log.info("执行任务：{}", this);
			final String value = String.valueOf(100 / 0);
//			final String value = String.valueOf(100 / 1);
			this.success = true;
			return value;
		}
		
		@Override
		public boolean doRollback() {
			log.info("回滚任务：{}", this);
			this.rollback = true;
			return true;
		}
		
	}
	
}
