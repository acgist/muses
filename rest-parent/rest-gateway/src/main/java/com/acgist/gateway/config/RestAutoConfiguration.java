package com.acgist.gateway.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestAutoConfiguration {

	@Value("${system.timeout:10000}")
	private int timeout;
	
	@Bean
	public RestTemplate restTemplate() {
		final RestTemplate restTemplate = new RestTemplate(this.buildFactory());
		restTemplate.getMessageConverters().set(1, this.buildMessageConverter());
		return restTemplate;
	}

	private HttpMessageConverter<String> buildMessageConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}
	
	private ClientHttpRequestFactory buildFactory() {
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(this.buildClient());
		factory.setReadTimeout(this.timeout);
		factory.setConnectTimeout(this.timeout);
		factory.setConnectionRequestTimeout(this.timeout);
		return factory;
	}
	
	private CloseableHttpClient buildClient() {
		return HttpClientBuilder.create()
			.setDefaultHeaders(this.buildHeaders())
			.setDefaultRequestConfig(this.buildRequestConfig())
			.setConnectionManager(this.buildConnectionManager())
			// 重试次数
//			.setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			// 保持连接
//			.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
			.build();
	}
	
	private List<Header> buildHeaders() {
        final List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "acgist/1.0.0 +(https://gitee.com/acgist/alibaba)"));
        headers.add(new BasicHeader("Content-type", "application/json;charset=UTF-8"));
        return headers;
	}
	
	private RequestConfig buildRequestConfig() {
		return RequestConfig.custom()
			.setSocketTimeout(this.timeout)
			.setConnectTimeout(this.timeout)
			.setConnectionRequestTimeout(this.timeout)
			.build();
	}
	
	private HttpClientConnectionManager buildConnectionManager() {
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// 最大连接数量
		connectionManager.setMaxTotal(1000);
		// 单机并发数量
		connectionManager.setDefaultMaxPerRoute(100);
		return connectionManager;
	}
	
}