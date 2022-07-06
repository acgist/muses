# Rest-OAuth2

认证授权Rest服务

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

### authorization_code

1. 页面访问地址`http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state`登陆获取`Code`
2. 请求地址`http://localhost:9999/oauth2/token`获取`Token`

> 参考`OAuth2Test`

### password

现在默认已经不能使用这种方式获取`Token`了，所以这里自己实现了这个功能。
现在提供一个登陆地址`GET:/oauth2/password`，登陆成功自动跳转`/oauth2/authorize`获取`Code`。

> 需要请求支持自动跳转

## 免登陆授权

如果存在一些类型大屏这种不要登陆的授权方式，建议自己实现IP和帐号绑定逻辑，直接通过IP地址进行授权，参考`PasswordToken`、`PasswordAuthenticationFilter`和`PasswordAuthenticationProvider`。

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