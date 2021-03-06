package com.acgist.service.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel导入标记管理
 * 
 * @author acgist
 */
public class ExcelMarkContext {
	
	private static final ExcelMarkContext INSTANCE = new ExcelMarkContext();

	/**
	 * Excel导入标记索引
	 */
	private final ThreadLocal<String> markIndex = new ThreadLocal<String>();
	/**
	 * Excel导入标记集合
	 */
	private final Map<String, ExcelMark> markMapping = new ConcurrentHashMap<>();
	
	/**
	 * 开始记录Excel标记：开始导入调用
	 * 
	 * @param index 索引：区别每次导入
	 * @param user 用户
	 */
	public static final void build(String index, String user) {
		INSTANCE.markIndex.set(index);
		INSTANCE.markMapping.put(index, new ExcelMark(user));
	}
	
	/**
	 * 获取索引
	 * 
	 * @return 索引
	 */
	public static final String index() {
		return INSTANCE.markIndex.get();
	}
	
	/**
	 * 线程绑定
	 * 
	 * @param index 索引
	 */
	public static final void rebind(String index) {
		INSTANCE.markIndex.set(index);
	}
	
	/**
	 * 获取Excel标记
	 * 
	 * @return Excel标记
	 */
	public static final ExcelMark get() {
		return get(index());
	}
	
	/**
	 * 获取Excel标记
	 * 
	 * @param index 索引
	 * 
	 * @return Excel标记
	 */
	public static final ExcelMark get(String index) {
		return index == null ? null : INSTANCE.markMapping.get(index);
	}
	
	/**
	 * 删除Excel标记并且返回
	 * 
	 * @return Excel标记
	 */
	public static final ExcelMark remove() {
		return remove(index());
	}
	
	/**
	 * 删除Excel标记并且返回
	 * 
	 * @param index 索引
	 * 
	 * @return Excel标记
	 */
	public static final ExcelMark remove(String index) {
		if(index == null) {
			return null;
		}
		INSTANCE.markIndex.remove();
		return INSTANCE.markMapping.remove(index);
	}
	
	/**
	 * 获取导入进度
	 * 
	 * @return 进度
	 */
	public static final double process() {
		return process(index());
	}
	
	/**
	 * 获取导入进度
	 * 
	 * @param index 索引
	 * 
	 * @return 进度
	 */
	public static final double process(String index) {
		final ExcelMark excelMark = get(index);
		// 如果导入标记为空返回100%
		return excelMark == null ? 1.00D : excelMark.process();
	}
	
	/**
	 * 设置进度
	 * 
	 * @param process 进度
	 */
	public static final void process(double process) {
		process(index(), process);
	}
	
	/**
	 * 设置进度
	 * 
	 * @param index 索引
	 * @param process 进度
	 */
	public static final void process(String index, double process) {
		final ExcelMark excelMark = get(index);
		if(excelMark != null) {
			excelMark.setProcess(process);
		}
	}
	
	/**
	 * 标记完成
	 */
	public static final void finish() {
		finish(index());
	}
	
	/**
	 * 标记完成
	 * 
	 * @param index 索引
	 */
	public static final void finish(String index) {
		final ExcelMark excelMark = get(index);
		if(excelMark != null) {
			excelMark.setFinish(true);
		}
	}
	
	/**
	 * 拷贝文档
	 * 
	 * @param source 原始文档
	 */
	public static final void copy(XSSFWorkbook workbook) {
		final ExcelMark excelMark = get();
		if(excelMark != null) {
			excelMark.copy(workbook);
		}
	}
	
	/**
	 * 设置总条数
	 * 
	 * @param total 总条数
	 */
	public static final void setTotal(int total) {
		final ExcelMark excelMark = get();
		if(excelMark != null) {
			excelMark.setTotal(total);
		}
	}
	
	/**
	 * 设置完成条数
	 * 
	 * @param total 完成条数
	 */
	public static final void setComplete(int complete) {
		final ExcelMark excelMark = get();
		if(excelMark != null) {
			excelMark.setComplete(complete);
		}
	}

	/**
	 * 记录标记
	 * 
	 * @param row 行数
	 * @param col 列数
	 * @param message 标记信息
	 */
	public static final void mark(int row, int col, String message) {
		final ExcelMark excelMark = get();
		if(excelMark != null) {
			excelMark.mark(row, col, message);
		}
	}
	
	/**
	 * 设置消息消费者
	 * 
	 * @param consumer 消费者
	 */
	public static final void bindConsumer(BiConsumer<String, Object> consumer) {
		bindConsumer(index(), consumer);
	}
	
	/**
	 * 设置消息消费者
	 * 
	 * @param index 索引
	 * @param consumer 消费者
	 */
	public static final void bindConsumer(String index, BiConsumer<String, Object> consumer) {
		final ExcelMark excelMark = get(index);
		if(excelMark != null) {
			excelMark.setConsumer(consumer);
		}
	}
	
	/**
	 * 发送消息
	 * 
	 * @param message 消息
	 */
	public static final void sendMessage(Object message) {
		sendMessage(index(), message);
	}
	
	/**
	 * 发送消息
	 * 
	 * @param index 索引
	 * @param message 消息
	 */
	public static final void sendMessage(String index, Object message) {
		final ExcelMark excelMark = get(index);
		if(excelMark != null) {
			excelMark.sendMessage(message);
		}
	}
	
}
