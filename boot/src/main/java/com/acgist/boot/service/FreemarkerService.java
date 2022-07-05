package com.acgist.boot.service;

import java.util.Map;

/**
 * FreeMarker
 * 
 * @author acgist
 */
public interface FreemarkerService {

	/**
	 * 生成静态文件
	 * 
	 * @param templatePath 模板路径
	 * @param data 数据
	 * @param htmlPath 生成HTML路径：/article/
	 * @param htmlName 生成HTML文件名称：index.html
	 * 
	 * @return 是否成功
	 */
	boolean build(String templatePath, Map<Object, Object> data, String htmlPath, String htmlName);
	
	/**
	 * 删除静态文件
	 * 
	 * @param htmlPath 生成HTML路径：/article/
	 * @param htmlName 生成HTML文件名称：index.html
	 * 
	 * @return 是否删除成功
	 */
	boolean delete(String htmlPath, String htmlName);
	
	/**
	 * 创建模板
	 * 
	 * @param name 模板名称
	 * @param content 模板内容
	 */
	void buildTemplate(String name, String content);
	
	/**
	 * 根据文本模板生产文本
	 * 
	 * @param name 模板名称
	 * @param data 数据
	 * 
	 * @return 文本
	 */
	String buildTemplate(String name, Map<String, Object> data);
	
}
