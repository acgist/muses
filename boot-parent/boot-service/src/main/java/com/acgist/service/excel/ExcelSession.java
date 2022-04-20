package com.acgist.service.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Excel导入状态
 * 
 * @author acgist
 */
@Getter
@Setter
public class ExcelSession {
	
	/**
	 * Excel导入状态信息
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	public static class ExcelMessage {
		
		/**
		 * 行数
		 */
		private Integer row;
		/**
		 * 列数
		 */
		private Integer col;
		/**
		 * 错误信息
		 */
		private String message;
		
	}
	
	/**
	 * 总条数
	 */
	private Integer total;
	/**
	 * 处理完成条数
	 */
	private Integer finish;
	/**
	 * 错误信息
	 */
	private final List<ExcelMessage> error = new ArrayList<>();
	
	/**
	 * 添加错误信息
	 * 
	 * @param row 行数
	 * @param col 列数
	 * @param message 错误信息
	 */
	public void error(int row, int col, String message) {
		this.error.add(new ExcelMessage(row, col, message));
	}
	
	/**
	 * 计算进度
	 * 
	 * 上传：25%
	 * 解析：50%
	 * 入库：25%
	 * 
	 * @return 进度
	 */
	public Double process() {
		return BigDecimal.valueOf(0.25 + (0.5D * finish / total)).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
}
