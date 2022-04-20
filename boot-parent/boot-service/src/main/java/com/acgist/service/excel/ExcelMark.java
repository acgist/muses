package com.acgist.service.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Excel导入标记
 * 
 * @author acgist
 */
@Getter
@Setter
public class ExcelMark {
	
	/**
	 * Excel导入标记信息
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	public static class MarkMessage {
		
		/**
		 * 行数
		 */
		private Integer row;
		/**
		 * 列数
		 */
		private Integer col;
		/**
		 * 标记信息
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
	 * 标记信息
	 */
	private final List<MarkMessage> marks = new ArrayList<>();
	
	/**
	 * 添加标记信息
	 * 
	 * @param row 行数
	 * @param col 列数
	 * @param message 标记信息
	 */
	public void mark(int row, int col, String message) {
		this.marks.add(new MarkMessage(row, col, message));
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
	
	/**
	 * @return 是否含有错误
	 */
	public boolean hasException() {
		return !this.marks.isEmpty();
	}
	
}
