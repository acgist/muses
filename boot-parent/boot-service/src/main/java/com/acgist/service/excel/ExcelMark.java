package com.acgist.service.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.utils.JSONUtils;

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
		private int row;
		/**
		 * 列数
		 */
		private int col;
		/**
		 * 标记信息
		 */
		private String message;
		
	}
	
	/**
	 * 总条数
	 */
	private int total;
	/**
	 * 用户绑定
	 */
	private final String user;
	/**
	 * 处理完成条数
	 */
	private int complete;
	/**
	 * 是否完成
	 */
	private boolean finish;
	/**
	 * Excel输入流：不直接保存Excel对象（不能多次输出）
	 */
	private ByteArrayInputStream stream;
	/**
	 * 消息消费者：处理
	 */
	private BiConsumer<String, Object> consumer;
	/**
	 * 标记信息
	 */
	private final List<MarkMessage> marks = new ArrayList<>();
	
	public ExcelMark(String user) {
		this.user = user;
	}
	
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
	public double process() {
		if(this.total == 0) {
			return 0.75D;
		}
		if(this.finish) {
			return 1.00D;
		}
		return BigDecimal.valueOf(0.25 + (0.50D * this.complete / this.total)).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * @return 是否含有错误
	 */
	public boolean hasException() {
		return !this.marks.isEmpty();
	}
	
	/**
	 * @return Excel输出流
	 */
	public ByteArrayInputStream stream() {
		this.stream.reset();
		return this.stream;
	}
	
	/**
	 * 拷贝文档
	 * 
	 * @param source 原始文档
	 */
	public void copy(XSSFWorkbook source) {
		try (
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
		) {
			source.write(output);
			// 不用关闭
			this.stream = new ByteArrayInputStream(output.toByteArray());
		} catch (IOException e) {
			throw MessageCodeException.of(e, "拷贝文档异常");
		}
	}
	
	/**
	 * 发送通知消息
	 * 
	 * @param message 通知消息
	 */
	public void sendMessage(Object message) {
		this.consumer.accept(this.user, JSONUtils.toJSON(message));
	}
	
}
