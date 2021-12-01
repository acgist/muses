package com.acgist.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CostUtils.class);
	
	private CostUtils() {
	}

	public static final void costed(int count, Coster coster) {
		final long time = System.currentTimeMillis();
		for (int index = 0; index < count; index++) {
			coster.execute();
		}
		final long costed = System.currentTimeMillis() - time;
		LOGGER.info("消耗时间：{}", costed);
	}
	
	public interface Coster {
		void execute();
	}
	
}
