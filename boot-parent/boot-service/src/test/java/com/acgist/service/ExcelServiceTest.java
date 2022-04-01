package com.acgist.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.elasticsearch.core.List;
import org.junit.jupiter.api.Test;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.model.entity.BootEntity;
import com.acgist.service.ExcelService.ExcelHeader;
import com.acgist.service.impl.ExcelServiceImpl;

import lombok.Getter;
import lombok.Setter;

public class ExcelServiceTest {

	@Getter
	@Setter
	public static final class ExcelEntity extends BootEntity {

		private static final long serialVersionUID = 1L;

		@ExcelHeader(name = "年龄")
		private Integer age;
		@ExcelHeader(name = "名称")
		private String name;
		
	}

	public static interface ExcelMapper extends BootMapper<ExcelEntity> {
	}
	
	@Test
	public void testDownload() throws IOException {
		final ExcelService<ExcelEntity> service = new ExcelServiceImpl<ExcelMapper, ExcelServiceTest.ExcelEntity>() {
		};
		final ExcelEntity entity = new ExcelEntity();
		entity.setAge(11);
		entity.setName("acgist");
		final Map<String, String> header = service.header();
		final OutputStream output = Files.newOutputStream(Paths.get("D:/tmp/excel.xlsx"));
		service.download(List.of(entity), header, output);
		output.close();
	}
	
}
