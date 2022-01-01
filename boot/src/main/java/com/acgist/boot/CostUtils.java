package com.acgist.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间消耗统计工具
 * 
 * @author acgist
 */
public final class CostUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CostUtils.class);

	private CostUtils() {
	}

	/**
	 * 执行消耗统计
	 * 
	 * @param count 执行次数
	 * @param coster 消耗函数
	 * 
	 * @return 消耗时间
	 */
	public static final long costed(int count, Coster coster) {
		final long time = System.currentTimeMillis();
		for (int index = 0; index < count; index++) {
			coster.execute();
		}
		final long costed = System.currentTimeMillis() - time;
		LOGGER.info("消耗时间：{}", costed);
		return costed;
	}

	/**
	 * 消耗函数
	 * 
	 * @author acgist
	 */
	public interface Coster {
		/**
		 * 消耗方法
		 */
		void execute();
	}

}
