package com.acgist.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.acgist.boot.utils.StringUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger文档自动配置
 * 
 * 注意：OpenAPI不好配置
 * 
 * @author acgist
 */
@Slf4j
@Profile("dev")
// 如果没有手动启动MVC报错
@EnableWebMvc
@Configuration
@EnableOpenApi
@ConditionalOnClass(OpenAPI.class)
//@ConditionalOnProperty(value = "spring.profiles.active", matchIfMissing = false, havingValue = "dev")
public class SwaggerAutoConfiguration {
	
	@Value("${system.name:muses}")
	private String systemName;
	@Value("${system.version:1.0.0}")
	private String systemVersion;
	@Value("${spring.application.name:}")
	private String applicationName;
	
	@Bean
	public Docket createRestApi() {
		log.info("配置Swagger文档");
		return this.buildDocket("Muses", "/**");
	}
	
	/**
	 * @return API信息
	 */
	private ApiInfo buildApiInfo() {
		return new ApiInfoBuilder()
			.title(StringUtils.isEmpty(this.systemName) ? this.applicationName : this.systemName)
			.version(this.systemVersion)
//			.license("Apache 2.0")
//			.licenseUrl("https://www.acgist.com")
			.description(this.applicationName)
			.build();
	}
	
	/**
	 * 创建分组
	 * 
	 * @param name 名称
	 * @param path 地址
	 * 
	 * @return 分组
	 */
	private Docket buildDocket(String name, String path) {
		return new Docket(DocumentationType.OAS_30)
			.groupName(name)
			.select()
			.apis(RequestHandlerSelectors.withClassAnnotation(Tag.class))
			.paths(PathSelectors.ant(path))
			.build()
//			.forCodeGeneration(true)
//			.genericModelSubstitutes(Message.class)
			.genericModelSubstitutes(DeferredResult.class)
//			.useDefaultResponseMessages(false)
			.apiInfo(this.buildApiInfo());
	}
	
}
