package com.acgist.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.model.entity.BootEntity;
import com.acgist.service.BootExcelService.ExcelHeader;
import com.acgist.service.BootExcelService.ExcelHeaderValue;
import com.acgist.service.excel.ExcelMark;
import com.acgist.service.excel.IntegerFormatter;
import com.acgist.service.impl.BootExcelServiceImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelServiceTest {

	@Getter
	@Setter
	public static final class ExcelEntity extends BootEntity {

		private static final long serialVersionUID = 1L;

		@ExcelHeader(name = "年龄", formatter = IntegerFormatter.class)
		private Integer age;
		@ExcelHeader(name = "名称")
		private String name;
		
	}

	public static interface ExcelMapper extends BootMapper<ExcelEntity> {
	}
	
	@Test
	public void testDownload() throws IOException {
		final BootExcelService<ExcelEntity> service = new BootExcelServiceImpl<ExcelMapper, ExcelServiceTest.ExcelEntity>() {
		};
		final ExcelEntity a = new ExcelEntity();
		a.setAge(11);
		a.setName("acgist");
		final ExcelEntity b = new ExcelEntity();
		b.setAge(11);
		b.setName("这是一个测试长度问题");
		final Map<String, ExcelHeaderValue> header = service.header();
		final OutputStream output = Files.newOutputStream(Paths.get("D:/tmp/excel.xlsx"));
		service.download(List.of(a, b), header, output);
		output.close();
	}
	
	@Test
	public void testLoad() {
		final BootExcelService<ExcelEntity> service = new BootExcelServiceImpl<ExcelMapper, ExcelServiceTest.ExcelEntity>() {
		};
		final List<ExcelEntity> load = service.loadEntity("D:/tmp/excel.xlsx");
//		final List<ExcelEntity> load = service.load("D:/tmp/excel.xlsx", ExcelEntity.class);
		log.info("{}", load);
	}

	@Test
	public void testExcelSession() throws FileNotFoundException {
		final BootExcelService<ExcelEntity> service = new BootExcelServiceImpl<ExcelMapper, ExcelServiceTest.ExcelEntity>() {
		};
		service.mark("1234");
//		service.loadEntity("D:/tmp/excel.xlsx").forEach(entity -> log.info("{}", entity));
		service.loadEntity("D:/tmp/excelMark.xlsx").forEach(entity -> log.info("{}", entity));
		final ExcelMark mark = service.removeMark();
		if(mark.hasException()) {
			service.mark(new FileInputStream(new File("D:/tmp/excelMark.xlsx")), new FileOutputStream(new File("D:/tmp/excelException.xlsx")), mark);
		}
	}
	
}
