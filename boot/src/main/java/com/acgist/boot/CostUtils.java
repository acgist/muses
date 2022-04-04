package com.acgist.boot;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间消耗统计工具
 * 
 * @author acgist
 */
@Slf4j
public final class CostUtils {

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
		log.info("消耗时间：{}", costed);
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
