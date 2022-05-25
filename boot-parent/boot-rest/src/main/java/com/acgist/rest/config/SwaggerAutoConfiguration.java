package com.acgist.rest.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;

import com.acgist.boot.model.User;
import com.acgist.boot.utils.StringUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuth2SchemeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger文档自动配置
 * 
 * 如果启动报空指针错误选择以下任意一种解决方式：
 * 
 * 1. 在启动类添加注解：`@EnableWebMvc`
 * 2. 添加配置：`spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER`
 * 
 * 注意：OpenAPI不好配置
 * 
 * 授权配置：https://swagger.io/docs/specification/authentication/oauth2/
 * 
 * @author acgist
 */
@Slf4j
@Profile("dev")
@Configuration
@EnableOpenApi
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(value = SwaggerConfig.class)
//@ConditionalOnProperty(value = "spring.profiles.active", matchIfMissing = false, havingValue = "dev")
public class SwaggerAutoConfiguration {
	
	@Value("${system.name:muses}")
	private String systemName;
	@Value("${system.version:1.0.0}")
	private String systemVersion;
	@Value("${spring.application.name:}")
	private String applicationName;
	
	@Autowired
	private SwaggerConfig swaggerConfig;
	
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
			.apiInfo(this.buildApiInfo())
			.securitySchemes(this.securitySchemes())
			.securityContexts(this.securityContexts());
	}

	private AuthorizationScope[] allScope() {
		final AuthorizationScope allScope = new AuthorizationScope("all", "all");
		return new AuthorizationScope[] {
			allScope
		};
	}
	
	private List<SecurityScheme> securitySchemes() {
		final List<SecurityScheme> securitySchemes = new ArrayList<>();
		// Header透传
		securitySchemes.add(new ApiKey(User.HEADER_CURRENT_USER, User.HEADER_CURRENT_USER, "header"));
		// OAuth2授权：password、implicit、authorizationCode
		securitySchemes.add(
//			new OAuth2SchemeBuilder("password")
//			new OAuth2SchemeBuilder("implicit")
			new OAuth2SchemeBuilder("authorizationCode")
			.name("OAuth2")
			.scopes(List.of(this.allScope()))
			.tokenUrl(this.swaggerConfig.getServer() + "/oauth2/token")
			.refreshUrl(this.swaggerConfig.getServer() + "/oauth2/token")
			.authorizationUrl(this.swaggerConfig.getServer() + "/oauth2/authorize")
			.build()
		);
		return securitySchemes;
	}

	private List<SecurityContext> securityContexts() {
		final List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(
			SecurityContext.builder()
			.operationSelector(context -> true)
			.securityReferences(List.of(new SecurityReference(User.HEADER_CURRENT_USER, this.allScope())))
			.build()
		);
		securityContexts.add(
			SecurityContext.builder()
			.operationSelector(context -> true)
			.securityReferences(List.of(new SecurityReference("OAuth2", this.allScope())))
			.build()
		);
		return securityContexts;
	}

}
