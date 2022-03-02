package com.acgist.code;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;

import com.acgist.boot.StringUtils;

/**
 * 代码生成
 * 
 * @author acgist
 */
public class CodeBuilderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeBuilderTest.class);
	
	private String path;
	// 输出目录
	private String target = "/code";
	// 基本路径
	private String basePackage = "com.acgist";
	// ID字段
	private String id = "id";
	// 模块路径
	private String modulePackage = this.basePackage + ".admin.";
	// Mapper
	private String mapperPackage = "mybatis.mapper.";
	// Java目录
	private String targetJava = "/java/";
	// 资源目录
	private String targetResources = "/resources/";
	// 跳过字段
//	private String[] skipColumns = new String[] {};
	private String[] skipColumns = new String[] { "id", "create_date", "modify_date" };
	// 顶级路径：t_acgist_user_list=com.acgist.admin.data.acgist.AcgistUserList
	// 完整路径：t_acgist_user_list=com.acgist.admin.data.acgist.user.list.AcgistUserList
	private boolean absolute = false;
	// 数据配置
	private String url = "jdbc:mysql://localhost:3306/muses?useSSL=false&useUnicode=true&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
	private String user = "root";
	private String password = "";
	private String driverClass = "com.mysql.cj.jdbc.Driver";
	private FreemarkerUtils freemarkerUtils = new FreemarkerUtils();
	
	@Test
	public void testBuildAll() throws Exception {
		this.buildCode("acgist", "t_user", false);
		this.buildCode("acgist", "t_user_role", false);
	}
	
	/**
	 * 生成代码
	 * 
	 * @param author 用户
	 * @param table 表名
	 * @param removePrefix 是否去掉字段前缀
	 * 
	 * @throws Exception 异常
	 */
	public void buildCode(@RequestBody String author, String table, boolean removePrefix) throws Exception {
		this.path = new ClassPathResource("./").getFile().getParentFile().getAbsolutePath() + this.target;
		LOGGER.info("输出目录：{}", this.path);
		final Map<Object, Object> map = new HashMap<>();
		map.put("id", this.id);
		map.put("modulePackage", this.modulePackage);
		map.put("hasId", Arrays.asList(this.skipColumns).indexOf(this.id) < 0);
		final List<Column> list = this.loadTable(map, table, removePrefix);
		// 作者
		map.put("author", author);
		// 表名
		map.put("table", table);
		// 路径
		map.put("path", table.substring(table.indexOf("_") + 1).replace('_', '/'));
		// 模块
		final int index = table.indexOf('_') + 1;
		if (table.indexOf('_') == table.lastIndexOf('_')) {
			map.put("module", table.substring(index).replace('_', '.'));
		} else if (this.absolute) {
			map.put("module", table.substring(index, table.lastIndexOf("_")).replace('_', '.'));
		} else {
			map.put("module", table.substring(index, table.indexOf("_", index)).replace('_', '.'));
		}
		// 驼峰类型
		final String prefix = this.hump(table.substring(table.indexOf('_')));
		// 大写类型：SysAccount
		map.put("prefix", prefix);
		// 小写类型：sysAccount
		map.put("prefixLower", lowerFirst(prefix));
		// 字段信息
		map.put("columns", list);
		LOGGER.info("生成代码变量：{}", map);
		LOGGER.info("生成data代码：{}", table);
		this.buildPojo(map);
		LOGGER.info("生成dao代码：{}", table);
		this.buildDao(map);
		LOGGER.info("生成service代码：{}", table);
		this.buildService(map);
		LOGGER.info("生成controller代码：{}", table);
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
			target += upperFirst(names[index]);
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
	public static final String upperFirst(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	}
	
	/**
	 * 首个字母大写
	 * 
	 * @param value 原始字符
	 * 
	 * @return 目标字符
	 */
	public static final String lowerFirst(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}
	
	/**
	 * 获取数据表结构
	 * 
	 * @param map 参数
	 * @param tableName 表名
	 * @param removePrefix 是否去掉字段前缀
	 * 
	 * @return 数据表结构
	 * 
	 * @throws Exception 异常
	 */
	public List<Column> loadTable(Map<Object, Object> map, String tableName, boolean removePrefix) throws Exception {
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
			// 添加类型
			if("Date".equals(type)) {
				map.put("hasDate", true);
				map.put("hasOtherType", true);
			} else if("BigDecimal".equals(type)) {
				map.put("hasBigDecimal", true);
				map.put("hasOtherType", true);
			}
			// 字段前缀
			if(removePrefix) {
				list.add(new Column(name, type, this.hump(name.substring(2)), comment));
			} else {
				list.add(new Column(name, type, this.hump(name), comment));
			}
		}
		tableResultSet.close();
		statement.close();
		table.close();
		connection.close();
		return list;
	}
	
	/**
	 * 生成Pojo
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildPojo(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".pojo.entity";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerUtils.build("entity.ftl", map, this.path + path, map.get("prefix") + ".java");
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
		this.freemarkerUtils.build("mapper.ftl", map, this.path + path, map.get("prefix") + "Mapper.java");
		path = this.mapperPackage + map.get("module");
		path = this.targetResources + path.replace('.', '/');
		this.freemarkerUtils.build("mapper.xml.ftl", map, this.path + path, map.get("prefix") + "Mapper.xml");
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
		this.freemarkerUtils.build("service.ftl", map, this.path + path, map.get("prefix") + "Service.java");
		path = this.modulePackage + map.get("module") + ".service.impl";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerUtils.build("service.impl.ftl", map, this.path + path, map.get("prefix") + "ServiceImpl.java");
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
		this.freemarkerUtils.build("controller.ftl", map, this.path + path, map.get("prefix") + "Controller.java");
	}
	
	/**
	 * 字段
	 * 
	 * @author acgist
	 */
	public static class Column {
		
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
		
		public String getGetter() {
			return "get" + upperFirst(this.value);
		}
		
		public String getSetter() {
			return "set" + upperFirst(this.value);
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		@Override
		public String toString() {
			return this.name + "-" + this.type + "-" + this.value + "-" + this.comment;
		}
		
	}
	
}
