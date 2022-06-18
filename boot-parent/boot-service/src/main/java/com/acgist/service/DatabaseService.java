package com.acgist.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.acgist.model.dto.TableDto;

/**
 * 数据库
 * 
 * @author acgist
 */
public interface DatabaseService {
	
	/**
	 * 查询数据库表信息
	 * 
	 * @param tableName 数据库表名称
	 * 
	 * @return 数据库表信息
	 * 
	 * @throws SQLException SQL异常
	 */
	TableDto table(String tableName) throws SQLException;

	/**
	 * 查询数据库表信息
	 * 
	 * @param tableName 数据库表名称
	 * @param connection 数据库的连接
	 * 
	 * @return 数据库表信息
	 * 
	 * @throws SQLException SQL异常
	 */
	TableDto table(String tableName, Connection connection) throws SQLException;
	
}
