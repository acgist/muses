package com.acgist.concurrent.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.concurrent.ConcurrentApplication;
import com.acgist.concurrent.executor.Executor;
import com.acgist.concurrent.executor.Executor.RollbackType;

@SpringBootTest(classes = ConcurrentApplication.class)
public class ExecutorServiceTest {
	
	@Autowired
	private ExecutorService executorService;
	
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
		assertTrue(this.executorService.executeRollbackUnsuccess(siExecutor, nxExecutor));
//		assertTrue(this.executorService.execute(siExecutor, nxExecutor));
//		assertFalse(this.executorService.execute(siExecutor, nxExecutor));
//		assertTrue(this.executorService.execute(siExecutor, isExecutor, nxExecutor));
	}
	
	public static class SIExecutor extends Executor<String, Integer> {

		public SIExecutor(RollbackType rollbackType) {
			super("数字转换", rollbackType);
		}
		
		public SIExecutor(ISExecutor executor, RollbackType rollbackType) {
			super("数字转换", rollbackType);
			this.executor = executor;
		}

		@Override
		public Integer doExecute() {
			final Integer value = Integer.valueOf("100");
//			final Integer value = Integer.valueOf("AAA");
			return value;
		}
		
		@Override
		protected boolean checkExecute() {
			return true;
		}

		@Override
		public boolean doRollback() {
			return true;
		}
		
	}
	
	public static class ISExecutor extends Executor<Integer, String> {
		
		protected ISExecutor(RollbackType rollbackType) {
			super("除法运算", rollbackType);
		}
		
		@Override
		public String doExecute() {
//			final String value = String.valueOf(100 / 0);
			final String value = String.valueOf(100 / 1);
			return value;
		}
		
		@Override
		protected boolean checkExecute() {
			return true;
		}
		
		@Override
		public boolean doRollback() {
			return true;
		}
		
	}
	
}
