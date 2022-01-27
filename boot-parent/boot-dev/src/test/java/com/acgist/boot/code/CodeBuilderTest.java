package com.acgist.boot.code;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.acgist.boot.StringUtils;

/**
 * 代码生成
 * 
 * @author acgist
 */
public class CodeBuilderTest {

	private String path;
	private String basePackage = "com.acgist";
	private boolean jpa = true;
	private boolean mybatis = true;
	private String dataPackage = this.basePackage + ".admin.data";
	private String servicePackage = this.basePackage + ".admin";
	private boolean skipId = false;
	private String[] skipColumns = new String[] { "create_date" };
	private FreemarkerUtils freemarkerUtils = new FreemarkerUtils();
	// 顶级路径：t_acgist_user_list=com.acgist.admin.data.acgist.AcgistUserList
	// 完整路径：t_acgist_user_list=com.acgist.admin.data.acgist.user.list.AcgistUserList
	private boolean absolute = false;
	// 数据配置
	private String url = "jdbc:mysql://localhost:3306/muses?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
	private String user = "root";
	private String password = "";
	
	@Test
	public void testBuildAll() throws Exception {
		this.buildCode("acgist", "t_user", false);
	}
	
	/**
	 * 生成代码
	 * 
	 * @param author 用户
	 * @param table 表名
	 * @param removePrefix 是否去掉前缀
	 * 
	 * @throws Exception 异常
	 */
	public void buildCode(String author, String table, boolean removePrefix) throws Exception {
		this.path = new ClassPathResource("./").getFile().getParentFile().getAbsolutePath() + "/code";
		final Map<Object, Object> map = new HashMap<>();
		final List<Column> list = this.loadTable(map, table, removePrefix);
		// 作者
		map.put("author", author);
		// 表名
		map.put("table", table);
		// 模块
		if(table.indexOf('_') == table.lastIndexOf('_')) {
			map.put("modulePath", table.substring(table.indexOf("_") + 1).replace('_', '.'));
		} else {
			final int index = table.indexOf('_') + 1;
			if(this.absolute) {
				map.put("modulePath", table.substring(index, table.lastIndexOf("_")).replace('_', '.'));
			} else {
				map.put("modulePath", table.substring(index, table.indexOf("_", index)).replace('_', '.'));
			}
		}
		final String prefix = this.hump(table.substring(table.indexOf('_')));
		// 大写类型：SysAccount
		map.put("prefix", prefix);
		// 小写类型：sysAccount
		map.put("prefixLower", prefix.substring(0, 1).toLowerCase() + prefix.substring(1));
		// 字段信息
		map.put("columns", list);
		this.buildData(map);
		this.buildDao(map);
		this.buildService(map);
		this.buildController(map);
		System.out.println(map);
	}
	
	public String hump(String column) {
		String[] names = column.split("_");
		if (names.length == 0) {
			return column;
		}
		String target = names[0];
		for (int index = 1; index < names.length; index++) {
			target += names[index].substring(0, 1).toUpperCase() + names[index].substring(1);
		}
		return target;
	}
	
	public List<Column> loadTable(Map<Object, Object> map, String tableName, boolean removePrefix) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
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
			if(
				"created_by".equals(name) ||
				"created_time".equals(name) ||
				"updated_by".equals(name) ||
				"updated_time".equals(name)
			) {
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
	
	public void buildData(Map<Object, Object> map) throws Exception {
		String path = "com.acgist.admin.data." + map.get("modulePath") + ".entity";
		path = "/data/" + path.replace('.', '/');
		this.freemarkerUtils.build("entity.ftl", map, this.path + path, map.get("prefix") + ".java");
	}
	
	public void buildDao(Map<Object, Object> map) throws Exception {
		String path = "com.acgist.admin." + map.get("modulePath") + ".dao.mapper";
		path = "/service/" + path.replace('.', '/');
		this.freemarkerUtils.build("mapper.ftl", map, this.path + path, map.get("prefix") + "Mapper.java");
		path = "mybatis.mapper." + map.get("modulePath");
		path = "/resources/" + path.replace('.', '/');
		this.freemarkerUtils.build("mapper.xml.ftl", map, this.path + path, map.get("prefix") + "Mapper.xml");
		path = "com.acgist.admin." + map.get("modulePath") + ".dao.repository";
		path = "/service/" + path.replace('.', '/');
		this.freemarkerUtils.build("repository.ftl", map, this.path + path, map.get("prefix") + "Repository.java");
	}
	
	public void buildService(Map<Object, Object> map) throws Exception {
		String path = "com.acgist.admin." + map.get("modulePath") + ".service";
		path = "/service/" + path.replace('.', '/');
		this.freemarkerUtils.build("service.ftl", map, this.path + path, map.get("prefix") + "Service.java");
		path = "com.acgist.admin." + map.get("modulePath") + ".service.impl";
		path = "/service/" + path.replace('.', '/');
		this.freemarkerUtils.build("service.impl.ftl", map, this.path + path, map.get("prefix") + "ServiceImpl.java");
	}

	public void buildController(Map<Object, Object> map) throws Exception {
		String path = "com.acgist.admin." + map.get("modulePath") + ".controller";
		path = "/service/" + path.replace('.', '/');
		this.freemarkerUtils.build("controller.ftl", map, this.path + path, map.get("prefix") + "Controller.java");
	}
	
	public static class Column {
		
		// 列名：t_id
		private String name;
		// 类型
		private String type;
		// 名称：id
		private String value;
		// 描述
		private String comment;
		
		public Column(String name, String type, String value, String comment) {
			this.name = name;
			this.type = type;
			this.value = value;
			this.comment = comment;
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
