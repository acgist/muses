package com.acgist.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.acgist.boot.StringUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
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
@EnableWebMvc
@Configuration
@EnableOpenApi
@ConditionalOnClass(OpenAPI.class)
@ConditionalOnProperty(value = "spring.profiles.active", matchIfMissing = false, havingValue = "dev")
public class SwaggerAutoConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);

	@Value("${system.name:muses}")
	private String systemName;
	@Value("${system.version:1.0.0}")
	private String systemVersion;
	@Value("${spring.application.name:}")
	private String applicationName;
	
	@Bean
	@ConditionalOnMissingBean
	public Docket createRestApi() {
		LOGGER.info("配置Swagger文档");
		final ApiInfo info = new ApiInfoBuilder()
			.title(StringUtils.isEmpty(this.systemName) ? this.applicationName : this.systemName)
			.version(this.systemVersion)
			.description(this.applicationName)
			.build();
		return
			new Docket(DocumentationType.OAS_30)
			.select()
			.apis(RequestHandlerSelectors.withClassAnnotation(Tag.class))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(info);
	}
	
}
