package com.acgist.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.acgist.boot.MapUtils;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.dao.mapper.BootMapper;
import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.service.ExcelService;

import lombok.extern.slf4j.Slf4j;

/**
 * Excel Service实现
 * 
 * @author acgist
 *
 * @param <M> Mapper
 * @param <T> 类型
 */
@Slf4j
public abstract class ExcelServiceImpl<M extends BootMapper<T>, T extends BootEntity> extends BootServiceImpl<M, T> implements ExcelService<T> {

	@Override
	public void download(FilterQuery query, OutputStream output) {
		try {
			this.download(this.list(query), this.header(), output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
	@Override
	public void download(FilterQuery query, Map<String, String> header, OutputStream output) {
		try {
			this.download(this.list(query), header, output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
	@Override
	public void download(List<T> list, Map<String, String> header, OutputStream output) throws IOException {
		try (
			output;
			final XSSFWorkbook workbook = new XSSFWorkbook();
		) {
			final AtomicInteger col = new AtomicInteger(0);
			final AtomicInteger row = new AtomicInteger(0);
			final XSSFSheet sheet = workbook.createSheet(this.entityClass.getSimpleName());
			// 设置列头
			final XSSFRow headerRow = sheet.createRow(row.getAndIncrement());
			final Set<String> keys = header.keySet();
			final Collection<String> values = header.values();
			values.forEach(value -> {
				final XSSFCell cell = headerRow.createCell(col.getAndIncrement());
				cell.setCellStyle(this.headerCellStyle(workbook));
				cell.setCellValue(value);
			});
			list.forEach(value -> {
				col.set(0);
				final XSSFRow dataRow = sheet.createRow(row.getAndIncrement());
				keys.forEach(field -> {
					Object object = null;
					try {
						object = FieldUtils.getField(value.getClass(), field, true).get(value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("读取属性异常：{}", field, e);
					}
					final XSSFCell cell = dataRow.createCell(col.getAndIncrement());
					cell.setCellStyle(this.dataCellStyle(workbook));
					cell.setCellValue(Objects.toString(object, ""));
				});
			});
			workbook.write(output);
		} finally {
			output.flush();
		}
	}
	
	@Override
	public CellStyle headerCellStyle(XSSFWorkbook workbook) {
		final Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 18);
		final CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(font);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return headerCellStyle;
	}
	
	@Override
	public CellStyle dataCellStyle(XSSFWorkbook workbook) {
		final Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		final CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setFont(font);
//		dataCellStyle.setBorderBottom(BorderStyle.THIN);
//		dataCellStyle.setBorderLeft(BorderStyle.THIN);
//		dataCellStyle.setBorderTop(BorderStyle.THIN);
//		dataCellStyle.setBorderRight(BorderStyle.THIN);
		return dataCellStyle;
	}

	@Override
	public Map<String, String> header() {
		// 注解
		final Map<String, String> map = FieldUtils.getAllFieldsList(this.entityClass).stream()
			.map(field -> {
				final ExcelHeader header = field.getAnnotation(ExcelHeader.class);
				if(header == null) {
					return null;
				}
				return Map.entry(field.getName(), header.name());
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, LinkedHashMap::new));
		if(MapUtils.isNotEmpty(map)) {
			return map;
		}
		// 属性
		return FieldUtils.getAllFieldsList(this.entityClass).stream()
			.map(field -> Map.entry(field.getName(), field.getName()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, LinkedHashMap::new));
	}
	
}
