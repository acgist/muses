# OAuth2认证服务

提供OAuth2认证服务

## 术语

|术语|描述|
|:-|:-|
|JWT|JSON Web Token|
|JWS|签过名的JWT|
|JWK|JWT签名密钥|

## jwk.jks

```
keytool -genkeypair -alias jwk -keyalg RSA -keysize 2048 -keystore jwk.jks -validity 3650 -storetype jks -keypass acgist -storepass acgist
```

## 注意事项

公钥和客户端ID不能随机生成，否者重启之后Redis数据就会失效。

## 获取Token

> 参考`OAuth2Test`

### authorization_code

1. 页面访问地址`http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state`登陆获取`Code`
2. 请求地址`http://localhost:9999/oauth2/token`获取`Token`

### 自定义授权（sms|password）

`token`/`provider`/`converter`

## 免登陆授权

如果存在一些类型大屏这种不要登陆的授权方式，可以通过IP自动授权。

## 自定义过滤器

```
/**
 * 自定义过滤器
 * 
 * @author acgist
 */
public class CustomFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}
	
}

@Bean
@ConditionalOnMissingBean
public CustomFilter customFilter() {
	return new CustomFilter();
}

@Bean
public FilterRegistrationBean<CustomFilter> codeFilterRegistrationBean(CustomFilter customFilter) {
	final FilterRegistrationBean<CustomFilter> registration = new FilterRegistrationBean<CustomFilter>();
	registration.setOrder(-100);
	registration.setFilter(customFilter);
	registration.addUrlPatterns("/oauth2/token");
	return registration;
}
```