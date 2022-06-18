package com.acgist.test.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.acgist.boot.service.FreemarkerService;
import com.acgist.boot.service.impl.FreemarkerServiceImpl;
import com.acgist.model.dto.TableColumnDto;
import com.acgist.model.dto.TableDto;
import com.acgist.service.DatabaseService;
import com.acgist.service.impl.DatabaseServiceImpl;

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
	// 类型转换
	private Map<String, String> typeConverter = Map.of(
		"Timestamp", "LocalDateTime"
	);
	// 类型转换
	private Map<String, String> typeImporter = Map.of(
		"BigDecimal", "java.math.BigDecimal",
		"LocalDateTime", "java.time.LocalDateTime"
	);
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
	private FreemarkerService freemarkerService = new FreemarkerServiceImpl(this.target);
	
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
		final List<TableColumnDto> list = this.loadTable(map, table);
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
		this.buildModel(map);
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
	public List<TableColumnDto> loadTable(Map<Object, Object> map, String tableName) throws Exception {
		Class.forName(this.driverClass);
		final Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
		final DatabaseService databaseService = new DatabaseServiceImpl();
		final TableDto tableDto = databaseService.table(tableName, connection);
		map.put("name", tableDto.getComment());
		final List<TableColumnDto> list = new ArrayList<>();
		final List<String> typeImport = new ArrayList<>();
		map.put("typeImport", typeImport);
		tableDto.getColumns().forEach(column -> {
			// 去掉通用
			if(Arrays.asList(this.skipColumns).indexOf(column.getColumn()) >= 0) {
				return;
			}
			// 类型转换
			String type = column.getType();
			type = type.substring(type.lastIndexOf('.') + 1);
			type = this.typeConverter.getOrDefault(type, type);
			column.setType(type);
			// 添加类型
			final String typeValue = this.typeImporter.get(type);
			if(typeValue != null && !typeImport.contains(typeValue)) {
				typeImport.add(typeValue);
			}
			// 设置字段
			column.setField(this.hump(this.removePrefix(column.getColumn())));
			// 字段前缀
			list.add(column);
		});
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
	 * 生成Model
	 * 
	 * @param map 参数
	 * 
	 * @throws Exception 异常
	 */
	public void buildModel(Map<Object, Object> map) throws Exception {
		String path = this.modulePackage + map.get("module") + ".model.vo";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("vo.ftl", map, this.path + path, map.get("prefix") + "Vo.java");
		path = this.modulePackage + map.get("module") + ".model.dto";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("dto.ftl", map, this.path + path, map.get("prefix") + "Dto.java");
		path = this.modulePackage + map.get("module") + ".model.entity";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("entity.ftl", map, this.path + path, map.get("prefix") + "Entity.java");
		path = this.modulePackage + map.get("module") + ".model.mapstruct";
		path = this.targetJava + path.replace('.', '/');
		this.freemarkerService.build("mapstruct.ftl", map, this.path + path, map.get("prefix") + "Mapstruct.java");
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
	
}
