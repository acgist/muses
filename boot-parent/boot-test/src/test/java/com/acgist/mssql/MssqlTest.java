package com.acgist.mssql;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.mssql.entity.Domain;
import com.acgist.mssql.mapper.DomainMapper;

@SpringBootTest(classes = MssqlApplication.class)
public class MssqlTest {
	
	@Autowired
	private DomainMapper domainMapper;

	@Test
	public void testList() {
		final List<Domain> list = this.domainMapper.list();
		assertTrue(list.size() > 0);
	}
	
}
