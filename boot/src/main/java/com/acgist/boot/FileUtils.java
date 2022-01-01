package com.acgist.boot;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 文件工具
 * 
 * @author acgist
 */
public final class FileUtils {

	/**
	 * 文件大小比例
	 */
	private static final int DATA_SCALE = 1024;
	/**
	 * 文件大小单位
	 */
	private static final String[] FILE_SCALE_UNIT = {"B", "KB", "MB", "GB", "TB"};
	
	private FileUtils() {
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param size 文件大小
	 * 
	 * @return 文件大小
	 */
	public static final String formatSize(Long size) {
		if(size == null || size == 0L) {
			return "0B";
		}
		int index = 0;
		BigDecimal decimal = BigDecimal.valueOf(size);
		final BigDecimal dataScale = BigDecimal.valueOf(DATA_SCALE);
		while(decimal.compareTo(dataScale) >= 0) {
			if(++index >= FILE_SCALE_UNIT.length) {
				index = FILE_SCALE_UNIT.length - 1;
				break;
			}
			decimal = decimal.divide(dataScale);
		}
		return decimal.setScale(2, RoundingMode.HALF_UP) + FILE_SCALE_UNIT[index];
	}
	
}
