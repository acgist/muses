package com.acgist.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.model.dto.TableColumnDto;
import com.acgist.model.dto.TableDto;
import com.acgist.service.DatabaseService;

public class DatabaseServiceImpl implements DatabaseService {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public TableDto table(String tableName) throws SQLException {
		try (
			// 连接需要关闭不然容易超过最大连接数量
			final Connection connection = this.dataSource.getConnection();
		) {
			return this.table(tableName, connection);
		}
	}
	
	@Override
	public TableDto table(String table, Connection connection) throws SQLException {
		final TableDto tableDto = new TableDto();
		tableDto.setTable(table);
		final DatabaseMetaData databaseMetaData = connection.getMetaData();
		try (
			final Statement statement = connection.createStatement();
			final ResultSet tableResult = statement.executeQuery("select * from " + table + " limit 1");
			final ResultSet tableInfoResultSet = databaseMetaData.getTables(null, null, table, new String[] { "TABLE" });
		) {
			// 数据库表信息
			String tableComment = null;
			if (tableInfoResultSet.next()) {
				// MySQL5.7必须设置后面参数才能获取表的备注：remarks=true&useInformationSchema=true
				tableComment = tableInfoResultSet.getString("REMARKS");
			}
			if (StringUtils.isEmpty(tableComment)) {
				tableComment = table;
			}
			tableDto.setComment(tableComment);
			// 数据库列信息
//			show full columns from table;
			final ResultSetMetaData tableMetaData = tableResult.getMetaData();
			final int columnCount = tableMetaData.getColumnCount();
			final List<TableColumnDto> cloumns = new ArrayList<>();
			tableDto.setColumns(cloumns);
			for (int index = 1; index <= columnCount; index++) {
				final String column = tableMetaData.getColumnName(index);
				final String columnType = tableMetaData.getColumnClassName(index);
				String columnComment = null;
				try (
					final ResultSet columnResultSet = databaseMetaData.getColumns(null, null, table, column);
				) {
					if(columnResultSet.next()) {
						columnComment = columnResultSet.getString("REMARKS");
					}
					if(StringUtils.isEmpty(columnComment)) {
						columnComment = column;
					}
				}
				final TableColumnDto tableColumnDto = new TableColumnDto();
				tableColumnDto.setColumn(column);
				tableColumnDto.setComment(columnComment);
				tableColumnDto.setType(columnType);
				cloumns.add(tableColumnDto);
			}
		}
		return tableDto;
	}

}
