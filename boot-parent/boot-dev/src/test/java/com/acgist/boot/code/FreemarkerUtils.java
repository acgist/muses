package com.acgist.boot.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.StringUtils;
import com.acgist.boot.config.MusesConfig;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 代码生成
 * 
 * @author acgist
 *
 */
public class FreemarkerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerUtils.class);

	private Configuration configuration;

	public FreemarkerUtils() {
		this.configuration = new Configuration(Configuration.VERSION_2_3_30);
		this.configuration.setDefaultEncoding(MusesConfig.CHARSET_VALUE);
		this.configuration.setClassicCompatible(true);
		this.configuration.setClassForTemplateLoading(FreemarkerUtils.class, "/com/acgist/boot/code");
	}

	/**
	 * 生成静态文件
	 * 
	 * @param templatePath 模板路径
	 * @param data 数据
	 * @param path 生成路径：/article/
	 * @param file 生成文件名称：index.html
	 * 
	 * @return 是否成功
	 * 
	 * @throws Exception 异常 
	 */
	public boolean build(String templatePath, Map<Object, Object> data, String path, String file) throws Exception {
		if (StringUtils.isEmpty(path)) {
			LOGGER.warn("生成静态文件路径错误：{}", path);
			return false;
		}
		if (!path.endsWith("/")) {
			path += "/";
		}
		final File htmlFile = new File(path + file);
		if(!htmlFile.getParentFile().exists()) {
			htmlFile.getParentFile().mkdirs();
		}
		try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), MusesConfig.CHARSET_VALUE))) {
			final Template template = this.configuration.getTemplate(templatePath, MusesConfig.CHARSET_VALUE);
			template.process(data, writer);
			writer.flush();
		} catch (TemplateException | IOException e) {
			throw e;
		}
		return true;
	}

	/**
	 * 根据文本模板生产文本
	 * 
	 * @param content 模板文本
	 * @param data 数据
	 * 
	 * @return 文本
	 * 
	 * @throws Exception 异常
	 */
	public String templateConvert(String content, Map<String, Object> data) throws Exception {
		final StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate("template", content);
		this.configuration.setTemplateLoader(loader);
		try (Writer writer = new StringWriter()) {
			final Template template = this.configuration.getTemplate("template", MusesConfig.CHARSET_VALUE);
			template.process(data, writer);
			content = writer.toString();
		} catch (TemplateException | IOException e) {
			throw e;
		}
		return content;
	}

}
