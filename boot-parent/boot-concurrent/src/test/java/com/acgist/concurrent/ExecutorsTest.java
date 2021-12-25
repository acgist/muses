package com.acgist.concurrent;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.concurrent.Executor.RollbackType;

public class ExecutorsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorsTest.class);

	@Test
	public void testExecutor() {
//		final SIExecutor siExecutor = new SIExecutor(RollbackType.ALL);
//		final ISExecutor isExecutor = new ISExecutor(RollbackType.ALL);
//		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.ALL);
//		assertTrue(Executors.execute(siExecutor, isExecutor, nxExecutor));
		final SIExecutor siExecutor = new SIExecutor(RollbackType.SUCCESS);
		final ISExecutor isExecutor = new ISExecutor(RollbackType.SUCCESS);
		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.SUCCESS);
		assertTrue(Executors.execute(siExecutor, isExecutor, nxExecutor));
//		final SIExecutor siExecutor = new SIExecutor(RollbackType.LAST_SUCCESS);
//		final ISExecutor isExecutor = new ISExecutor(RollbackType.LAST_SUCCESS);
//		final SIExecutor nxExecutor = new SIExecutor(isExecutor, RollbackType.LAST_SUCCESS);
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
			LOGGER.info("执行任务：{}", this);
			final Integer value = Integer.valueOf("100");
//			final Integer value = Integer.valueOf("AAA");
			this.success = true;
			return value;
		}

		@Override
		public boolean doRollback() {
			LOGGER.info("回滚任务：{}", this);
			return true;
		}
		
	}
	
	public static class ISExecutor extends Executor<Integer, String> {
		
		protected ISExecutor(RollbackType rollbackType) {
			super(rollbackType);
		}
		
		@Override
		public String doExecute() {
			LOGGER.info("执行任务：{}", this);
			final String value = String.valueOf(100 / 0);
//			final String vlaue = String.valueOf(100 / 1);
			this.success = true;
			return value;
		}
		
		@Override
		public boolean doRollback() {
			LOGGER.info("回滚任务：{}", this);
			return true;
		}
		
	}
	
}
