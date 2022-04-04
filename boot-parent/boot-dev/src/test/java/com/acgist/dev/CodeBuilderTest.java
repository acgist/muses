package com.acgist.dev;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.acgist.boot.StringUtils;
import com.acgist.boot.model.PojoCopy;
import com.acgist.boot.service.impl.FreemarkerService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成
 * 
 * @author acgist
 */
@Slf4j
public class CodeBuilderTest {

	private String path;
	// 输出目录
	private String target = "/code";
	// 基本路径
	private String basePackage = "com.acgist";
	// ID字段
	private String id = "id";
	// 模块
	private String module = "admin";
	// 模块路径
	private String modulePackage = this.basePackage + "." + module + ".";
	// Mapper
	private String mapperPackage = "mybatis.mapper.";
	// Java目录
	private String targetJava = "/java/";
	// 资源目录
	private String targetResources = "/resources/";
	// 跳过字段
//	private String[] skipColumns = new String[] {};
	private String[] skipColumns = new String[] { "id", "create_date", "modify_date" };
	// 顶级路径：t_acgist_user_list=com.acgist.admin.model.acgist.AcgistUserList
	// 完整路径：t_acgist_user_list=com.acgist.admin.model.acgist.user.list.AcgistUserList
	private boolean absolute = false;
	// 数据配置
	private String url = "jdbc:mysql://localhost:3306/muses?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true";
	private String user = "root";
	private String password = "";
	private String driverClass = "com.mysql.cj.jdbc.Driver";
	private FreemarkerService freemarkerService = new FreemarkerService(this.target);
	
	@Test
	public void testBuildAll() throws Exception {
		this.buildCode("acgist", "t_user");
		this.buildCode("acgist", "t_gateway");
		this.buildCode("acgist", "t_gateway_0");
		this.buildCode("acgist", "t_gateway_1");
	}
	
	/**
	 * 生成代码
	 * 
	 * @param author 用户
	 * @param table 表名
	 * 
	 * @throws Exception 异常
	 */
	public void buildCode(String author, String table) throws Exception {
		this.path = new ClassPathResource("./").getFile().getParentFile().getAbsolutePath() + this.target;
		log.info("输出目录：{}", this.path);
		final Map<Object, Object> map = new HashMap<>();
		map.put("id", this.id);
		map.put("modulePackage", this.modulePackage);
		map.put("hasId", Arrays.asList(this.skipColumns).indexOf(this.id) < 0);
		final List<Column> list = this.loadTable(map, table);
		// 去掉前缀
		final String tableSuffix = this.removePrefix(table);
		// 作者
		map.put("author", author);
		// 表名
		map.put("table", table);
		// 路径
		map.put("path", this.module + "/" + tableSuffix.replace('_', '/').toLowerCase());
		// 模块
		final int index = tableSuffix.indexOf('_');
		if (index < 0) {
			map.put("module", tableSuffix.replace('_', '.').toLowerCase());
		} else if (this.absolute) {
			map.put("module", tableSuffix.substring(0, tableSuffix.lastIndexOf("_")).replace('_', '.').toLowerCase());
		} else {
			map.put("module", tableSuffix.substring(0, tableSuffix.indexOf("_", index)).replace('_', '.').toLowerCase());
		}
		// 驼峰类型
		final String prefix = this.hump(tableSuffix);
		// 大写类型：SysAccount
		map.put("prefix", this.upperFirstOnly(prefix));
		// 小写类型：sysAccount
		map.put("prefixLower", this.lowerFirstOnly(prefix));
		// 字段信息
		map.put("columns", list);
		log.info("生成代码变量：{}", map);
		this.buildPojo(map);
		this.buildDao(map);
		this.buildService(map);
		this.buildController(map);
	}
	
	/**
	 * 驼峰命名
	 * 
	 * @param source 原始名称
	 * 
	 * @return 驼峰名称
	 */
	public String hump(String source) {
		String[] names = source.split("_");
		if (names.length == 0) {
			return source;
		}
		String target = names[0].toLowerCase();
		for (int index = 1; index < names.length; index++) {
			target += this.upperFirst(names[index]);
		}
		return target;
	}
	
	/**
	 * 首个字母大写
	 * 
	 * @param value 原始字符
	 * 
	 * @return 目标字符
	 */
	public String upperFirst(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	}
	
	/**
	 * 首个字母大写
	 * 
	 * @param value 原始字符
	 * 
	 * @return 目标字符
	 */
	public String upperFirstOnly(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
	
	/**
	 * 首个字母大写
	 * 
	 * @param value 原始字符
	 * 
	 * @return 目标字符
	 */
	public String lowerFirstOnly(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}
	
	/**
	 * 获取数据表结构
	 * 
	 * @param map 参数
	 * @param tableName 表名
	 * 
	 * @return 数据表结构
	 * 
	 * @throws Exception 异常
	 */
	public List<Column> loadTable(Map<Object, Object> map, String tableName) throws Exception {
		Class.forName(this.driverClass);
		final Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
		final Statement statement = connection.createStatement();
		final DatabaseMetaData databaseMetaData = connection.getMetaData();
		final ResultSet table = statement.executeQuery("select * from " + tableName + " limit 1");
		// 表名描述
		final ResultSet tableResultSet = databaseMetaData.getTables(null, null, tableName, new String[] {"TABLE"});
		String tableComment = null;
		if(tableResultSet.next()) {
			tableComment = tableResultSet.getString("REMARKS");
		}
		if(StringUtils.isEmpty(tableComment)) {
			tableComment = tableName;
		}
		map.put("name", tableComment);
		final List<Column> list = new ArrayList<>();
		final ResultSetMetaData tableMetaData = table.getMetaData();
		final int columnCount = tableMetaData.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
		final String name = tableMetaData.getColumnName(index);
			// 去掉通用
			if(Arrays.asList(this.skipColumns).indexOf(name) >= 0) {
				continue;
			}
			String type = tableMetaData.getColumnClassName(index);
			type = type.substring(type.lastIndexOf('.') + 1);
			// 类型转换
			if("Timestamp".equals(type)) {
				type = "Date";
			}
			// 字段描述
			String comment = null;
			final ResultSet column = databaseMetaData.getColumns(null, null, tableName, name);
			if(column.next()) {
				comment = column.getString("REMARKS");
			}
			if(StringUtils.isEmpty(comment)) {
				comment = name;
			}
			comment = comment
				.replace("\r\n", " ")
				.replace('\r', ' ')
				.replace('\n', ' ')
				.replace("\"", "\\\"");
			// 添加类型
			if("Date".equals(type)) {
				map.put("hasDate", true);
				map.put("hasOtherType", true);
			} else if("BigDecimal".equals(type)) {
				map.put("hasBigDecimal", true);
				map.put("hasOtherType", true);
			}
			// 字段前缀
			list.add(new Column(name, type, this.hump(this.removePrefix(name)), comment));
		}
		tableResultSet.close();
		statement.close();
		table.close();
		connection.close();
		return list;
	}
	
	/**
	 * 去掉前缀
	 * 
	 * @param name 内容
	 * 
	 * @return 去掉前缀内容
	 */
	public String removePrefix(String name) {
		final int index = name.indexOf('_');
		if(index == 1) {
			return name.substring(index + 1);
		}
		return name;
	}
	
	/**
	 * 生成Pojo
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildPojo(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".model.entity";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("entity.ftl", map, this.path + path, map.get("prefix") + "Entity.java");
	}
	
	/**
	 * 生成Dao
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildDao(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".dao.mapper";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("mapper.ftl", map, this.path + path, map.get("prefix") + "Mapper.java");
		path = this.mapperPackage + map.get("module");
		path = this.targetResources + path.replace('.', '/');
		this.freemarkerService.build("mapper.xml.ftl", map, this.path + path, map.get("prefix") + "Mapper.xml");
	}
	
	/**
	 * 生成Service
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildService(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".service";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("service.ftl", map, this.path + path, map.get("prefix") + "Service.java");
		path = this.modulePackage + map.get("module") + ".service.impl";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("service.impl.ftl", map, this.path + path, map.get("prefix") + "ServiceImpl.java");
	}

	/**
	 * 生成Controller
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildController(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".controller";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("controller.ftl", map, this.path + path, map.get("prefix") + "Controller.java");
	}
	
	/**
	 * 字段
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
	public static class Column extends PojoCopy {
		
		private static final long serialVersionUID = 1L;
		
		/**
		 * 列名：t_id
		 */
		private String name;
		/**
		 * 类型
		 */
		private String type;
		/**
		 * Java字段：id
		 */
		private String value;
		/**
		 * 描述
		 */
		private String comment;
		
		public Column(String name, String type, String value, String comment) {
			this.name = name;
			this.type = type;
			this.value = value;
			this.comment = comment;
		}
		
	}
	
}
