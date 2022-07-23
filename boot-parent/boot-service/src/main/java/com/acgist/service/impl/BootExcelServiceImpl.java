package com.acgist.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.utils.BeanUtils;
import com.acgist.dao.mapper.BootMapper;
import com.acgist.model.entity.BootEntity;
import com.acgist.service.BootExcelService;
import com.acgist.service.excel.ExcelMark;
import com.acgist.service.excel.ExcelMarkContext;
import com.acgist.service.excel.StringFormatter;

/**
 * Excel Service实现
 * 
 * @author acgist
 *
 * @param <M> Mapper
 * @param <T> 类型
 */
public abstract class BootExcelServiceImpl<M extends BootMapper<T>, T extends BootEntity> extends BootServiceImpl<M, T> implements BootExcelService<T> {

	@Value("${system.excel.header.font:宋体}")
	private String headerFont;
	@Value("${system.excel.header.size:16}")
	private Short headerSize;
	@Value("${system.excel.cell.font:宋体}")
	private String cellFont;
	@Value("${system.excel.cell.size:12}")
	private Short cellSize;
	@Value("${system.excel.font.width:256}")
	private Integer fontWidth;
	
	/**
	 * Header缓存
	 */
	private Map<String, ExcelHeaderValue> header;
	
	@Override
	public void mark(int sheet, OutputStream output, ExcelMark mark) {
		try (
			final XSSFWorkbook workbook = new XSSFWorkbook(mark.stream());
		) {
			final CellStyle cellStyle = this.failCellStyle(workbook);
			final XSSFSheet sheetValue = workbook.getSheetAt(sheet);
			mark.getMarks().forEach(message -> {
				final XSSFRow row = sheetValue.getRow(message.getRow());
				if(row == null) {
					return;
				}
				final XSSFCell cell = row.getCell(message.getCol());
				if(cell == null) {
					this.markCell(cellStyle, sheetValue, row.createCell(message.getCol()), message.getMessage());
				} else {
					this.markCell(cellStyle, sheetValue, cell, message.getMessage());
				}
			});
			workbook.write(output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "读取Excel文件异常");
		}
	}
	
	@Override
	public void markCell(CellStyle cellStyle, XSSFSheet sheet, Cell cell, String message) {
		if(cell == null) {
			// 为空：Excel拷贝最后一行为空行时可能导致复制文件最后一行不存在
			return;
		}
		cell.setCellStyle(cellStyle);
		Comment comment = cell.getCellComment();
		if(comment == null) {
			final XSSFClientAnchor anchor = new XSSFClientAnchor();
			anchor.setRow1(cell.getRowIndex());
			anchor.setRow2(cell.getRowIndex() + 4);
			anchor.setCol1(cell.getColumnIndex());
			anchor.setCol2(cell.getColumnIndex() + 8);
			final XSSFDrawing drawing = sheet.createDrawingPatriarch();
			comment = drawing.createCellComment(anchor);
			cell.setCellComment(comment);
		}
		RichTextString richText = comment.getString();
		if(richText == null) {
			richText = new XSSFRichTextString(message);
			comment.setString(richText);
		} else if(richText instanceof XSSFRichTextString xssfRichText) {
			xssfRichText.append(message);
		} else {
			final XSSFRichTextString xssfRichText = new XSSFRichTextString(message);
			xssfRichText.append(richText.getString());
			comment.setString(xssfRichText);
		}
	}
	
	@Override
	public void download(List<T> list, Map<String, ExcelHeaderValue> header, OutputStream output) throws IOException {
		try (
			output;
			final XSSFWorkbook workbook = new XSSFWorkbook();
		) {
			final AtomicInteger col = new AtomicInteger(0);
			final AtomicInteger row = new AtomicInteger(0);
			final XSSFSheet sheet = workbook.createSheet(this.entityClass.getSimpleName());
			// 列宽
			final List<Integer> colWidth = new ArrayList<>();
			// 设置列头
			final XSSFRow headerRow = sheet.createRow(row.getAndIncrement());
			final Set<String> keys = header.keySet();
			final List<ExcelHeaderValue> headers = new ArrayList<>(header.values());
			final CellStyle headerCellStyle = this.headerCellStyle(workbook);
			headers.forEach(value -> {
				final XSSFCell cell = headerRow.createCell(col.getAndIncrement());
				cell.setCellStyle(headerCellStyle);
				final String data = value.getOutName();
				cell.setCellValue(data);
				colWidth.add(data.getBytes().length);
			});
			// 列宽
			final int colLength = headers.size();
			// 设置数据
			final CellStyle dataCellStyle = this.dataCellStyle(workbook);
			list.forEach(value -> {
				col.set(0);
				final XSSFRow dataRow = sheet.createRow(row.getAndIncrement());
				keys.forEach(field -> {
					if(colLength <= col.get()) {
						// 列宽超过头部退出循环
						return;
					}
					final Object fieldValue = BeanUtils.fieldValue(field, value);
					final ExcelHeaderValue excelHeaderValue = headers.get(col.get());
					final XSSFCell cell = dataRow.createCell(col.get());
					cell.setCellStyle(dataCellStyle);
					final String data = excelHeaderValue.getFormatter().format(fieldValue);
					if(data != null) {
						cell.setCellValue(data);
						colWidth.set(col.get(), Math.max(colWidth.get(col.get()), data.getBytes().length));
					}
					col.incrementAndGet();
				});
			});
			// 设置宽度
			col.set(0);
			colWidth.forEach(value -> sheet.setColumnWidth(col.getAndIncrement(), this.fontWidth * value));
			workbook.write(output);
		} finally {
			output.flush();
		}
	}
	
	/**
	 * 新建字段信息
	 * 
	 * @param field 字段
	 * @param name 表头
	 * @param clazz 格式化工具类型
	 * 
	 * @return 字段信息
	 */
	protected ExcelHeaderValue buildExcelHeader(String field, String name, Class<? extends Formatter> clazz) {
		return new ExcelHeaderValue(field, name, name, this.formatter(clazz));
	}
	
	/**
	 * 新建字段信息
	 * 
	 * @param field 字段
	 * @param name 表头
	 * @param clazz 格式化工具类型
	 * @param transferGroup 枚举分组
	 * 
	 * @return 字段信息
	 */
	protected ExcelHeaderValue buildExcelHeader(String field, String name, Class<? extends Formatter> clazz, String transferGroup) {
		return new ExcelHeaderValue(field, name, name, this.formatter(clazz), transferGroup);
	}
	
	/**
	 * 设置字段信息
	 * 
	 * @param map 字段集合
	 * @param field 字段
	 * @param name 表头
	 * @param clazz 格式化工具类型
	 */
	protected void putExcelHeader(Map<String, ExcelHeaderValue> map, String field, String name, Class<? extends Formatter> clazz) {
		map.put(field, this.buildExcelHeader(field, name, clazz));
	}
	
	/**
	 * 设置字段信息
	 * 
	 * @param map 字段集合
	 * @param field 字段
	 * @param name 表头
	 * @param clazz 格式化工具类型
	 * @param transferGroup 枚举分组
	 */
	protected void putExcelHeader(Map<String, ExcelHeaderValue> map, String field, String name, Class<? extends Formatter> clazz, String transferGroup) {
		map.put(field, this.buildExcelHeader(field, name, clazz, transferGroup));
	}
	
	@Override
	public Map<String, ExcelHeaderValue> header() {
		if(this.header != null) {
			return this.header;
		}
		// 注解
		this.header = FieldUtils.getAllFieldsList(this.entityClass).stream()
			.map(field -> {
				final ExcelHeader header = field.getAnnotation(ExcelHeader.class);
				if(header == null) {
					return null;
				}
				return Map.entry(
					field.getName(),
					new ExcelHeaderValue(field.getName(), header.outName(), header.loadName(), this.formatter(header.formatter()))
				);
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, LinkedHashMap::new));
		// 属性
		if(MapUtils.isEmpty(this.header)) {
			this.header = FieldUtils.getAllFieldsList(this.entityClass).stream()
				.map(field -> Map.entry(
					field.getName(),
					new ExcelHeaderValue(field.getName(), field.getName(), field.getName(), this.formatter(StringFormatter.class)))
				)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, LinkedHashMap::new));
		}
		return this.header;
	}
	
	@Override
	public CellStyle headerCellStyle(XSSFWorkbook workbook) {
		final Font font = workbook.createFont();
		font.setFontName(this.headerFont);
		font.setFontHeightInPoints(this.headerSize);
		final CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(font);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return headerCellStyle;
	}
	
	@Override
	public CellStyle dataCellStyle(XSSFWorkbook workbook) {
		final Font font = workbook.createFont();
		font.setFontName(this.cellFont);
		font.setFontHeightInPoints(this.cellSize);
		final CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setFont(font);
		// 设置边框
//		dataCellStyle.setBorderBottom(BorderStyle.THIN);
//		dataCellStyle.setBorderLeft(BorderStyle.THIN);
//		dataCellStyle.setBorderTop(BorderStyle.THIN);
//		dataCellStyle.setBorderRight(BorderStyle.THIN);
		return dataCellStyle;
	}
	
	@Override
	public CellStyle failCellStyle(XSSFWorkbook workbook) {
		final CellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setFillForegroundColor(IndexedColors.RED.index);
//		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillBackgroundColor(IndexedColors.RED.index);
		cellStyle.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
		return cellStyle;
	}

	@Override
	public List<List<Object>> load(InputStream input, int sheet) {
		final List<List<Object>> list = new ArrayList<>();
		try (
			input;
			final XSSFWorkbook workbook = new XSSFWorkbook(input);
		) {
			final XSSFSheet sheetValue = workbook.getSheetAt(sheet);
			// 默认最长列宽128
			final AtomicInteger colLength = new AtomicInteger(128);
			final Iterator<Row> rowIterator = sheetValue.rowIterator();
			rowIterator.forEachRemaining(row -> {
				// 补全row
				if(list.size() < row.getRowNum()) {
					IntStream.range(list.size(), row.getRowNum()).forEach(rowIndex -> {
						final List<Object> empty = IntStream.range(0, colLength.get()).boxed().map(cellIndex -> null).collect(Collectors.toList());
						list.add(empty);
					});
				}
				final List<Object> data = new ArrayList<>();
				final Iterator<Cell> cellIterator = row.cellIterator();
				cellIterator.forEachRemaining(cell -> {
					// 索引填充：默认返回有值数据
					// 补全cell
					if(data.size() < cell.getColumnIndex()) {
						IntStream.range(data.size(), Integer.min(cell.getColumnIndex(), colLength.get())).forEach(i -> data.add(null));
					}
					if(colLength.get() <= cell.getColumnIndex()) {
						// 删除无效单元：删除容易导致拷贝异常
//						cellIterator.remove();
						// 列宽超过头部退出循环
						return;
					}
					data.add(this.cellValue(cell));
				});
				// 设置头部长度
				if(row.getRowNum() == 0) {
					colLength.set(data.size());
				}
				// 填充数据
				if(data.size() < colLength.get()) {
					IntStream.range(data.size(), colLength.get()).forEach(i -> data.add(null));
				}
				// 添加数据
				list.add(data);
			});
			ExcelMarkContext.copy(workbook);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "读取Excel文件异常");
		}
		return list;
	}

	@Override
	public <K> List<K> load(List<List<Object>> list, Class<K> clazz, Map<String, ExcelHeaderValue> header) {
		if(CollectionUtils.isEmpty(list)) {
			return List.of();
		}
		// 表头
		final List<Object> excelHeader = list.get(0);
		// 表头 = 字段信息
		final Map<String, ExcelHeaderValue> mapping = header.entrySet().stream()
			.collect(Collectors.toMap(entry -> entry.getValue().getLoadName(), entry -> entry.getValue()));
		// 索引 = 字段信息
		final Map<Integer, ExcelHeaderValue> headerMapping = new HashMap<>();
		for (int index = 0; index < excelHeader.size(); index++) {
			headerMapping.put(index, mapping.get(Objects.toString(excelHeader.get(index), "")));
		}
		final AtomicInteger row = new AtomicInteger();
		ExcelMarkContext.setTotal(list.size());
		return list.stream()
			// 跳过表头
			.skip(1)
			// 表头字段转换
			.map(data -> {
				ExcelMarkContext.setComplete(row.incrementAndGet());
				boolean hasException = false;
				final Map<String, Object> map = new HashMap<>();
				for (int index = 0; index < data.size(); index++) {
					final ExcelHeaderValue headerValue = headerMapping.get(index);
					if(headerValue == null) {
						ExcelMarkContext.mark(row.get(), index, "所在列不存在");
						continue;
					}
					try {
						map.put(headerValue.getField(), headerValue.getFormatter().parse(data.get(index)));
					} catch (Exception e) {
						// 记录异常信息
						hasException = true;
						ExcelMarkContext.mark(row.get(), index, e.getMessage());
					}
				}
				return hasException ? null : map;
			})
			.filter(Objects::nonNull)
			// 数据类型转换
			.map(data -> {
				final K newInstance = BeanUtils.newInstance(clazz);
				if(newInstance == null) {
					return null;
				}
				BeanMap.create(newInstance).putAll(data);
				return newInstance;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
	
	/**
	 * 读取Cell数据
	 * 
	 * @param cell Cell
	 * 
	 * @return Cell数据
	 */
	private Object cellValue(Cell cell) {
		// 类型判断
		final CellType cellType = cell.getCellType();
		if(cellType == CellType.STRING) {
			return cell.getStringCellValue();
		} else if(cellType == CellType.BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if(cellType == CellType.NUMERIC) {
			if(DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}
		} else if(cellType == CellType.FORMULA) {
			// 公式需要转为文本
//			return cell.getCellFormula();
			return cell.getStringCellValue();
		} else {
			return cell.toString();
		}
	}
	
}
