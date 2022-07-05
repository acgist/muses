package com.acgist.boot.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.service.FreemarkerService;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerServiceImpl implements FreemarkerService {

	@Autowired
	private Configuration configuration;
	
	public FreemarkerServiceImpl() {
	}
	
	public FreemarkerServiceImpl(String outpath) {
		this.configuration = new Configuration(Configuration.VERSION_2_3_31);
		this.configuration.setDefaultEncoding(MusesConfig.CHARSET_VALUE);
		this.configuration.setClassicCompatible(true);
		this.configuration.setClassForTemplateLoading(FreemarkerServiceImpl.class, outpath);
	}

	@Override
	public boolean build(String templatePath, Map<Object, Object> data, String htmlPath, String htmlName) {
		if (StringUtils.isEmpty(htmlPath)) {
			log.warn("生成静态文件路径错误：{}", htmlPath);
			return false;
		}
		final File htmlFile = Paths.get(htmlPath, htmlName).toFile();
		if(!htmlFile.getParentFile().exists()) {
			htmlFile.getParentFile().mkdirs();
		}
		try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), MusesConfig.CHARSET))) {
			final Template template = this.configuration.getTemplate(templatePath, MusesConfig.CHARSET_VALUE);
			template.process(data, writer);
			writer.flush();
		} catch (TemplateException | IOException e) {
			log.error("处理FreeMarker模板异常：{}-{}", templatePath, data, e);
		}
		return true;
	}
	
	@Override
	public boolean delete(String htmlPath, String htmlName) {
		final File htmlFile = Paths.get(htmlPath, htmlName).toFile();
		if(htmlFile.exists()) {
			return htmlFile.delete();
		}
		return true;
	}
	
	@Override
	public void buildTemplate(String name, String content) {
		final StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate(name, content);
		this.configuration.setTemplateLoader(loader);
	}

	@Override
	public String buildTemplate(String name, Map<String, Object> data) {
		try (final Writer writer = new StringWriter()) {
			final Template template = this.configuration.getTemplate(name, MusesConfig.CHARSET_VALUE);
			template.process(data, writer);
			return writer.toString();
		} catch (TemplateException | IOException e) {
			log.error("处理FreeMarker模板异常：{}-{}", name, data, e);
		}
		return null;
	}

}
