package com.acgist.boot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.config.MusesConfig;

/**
 * HTTP工具 
 * 
 * @author acgist
 */
public final class HTTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);
	
	/**
	 * 复用连接
	 * 不能关闭Client：不同域名使用不同TCP连接
	 */
	private static final CloseableHttpClient CLIENT;
	
	static {
	    final List<Header> headers = new ArrayList<>();
	    headers.add(new BasicHeader("User-Agent", "ACGIST/1.0.0 +(https://www.acgist.com)"));
	    headers.add(new BasicHeader("Content-Type", "application/json"));
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", createSSLConnSocketFactory())
//			.register("https", SSLConnectionSocketFactory.getSocketFactory())
			.build();
		final SocketConfig socketConfig = SocketConfig.custom()
//          .setSoLinger(1)
		    .setSoTimeout(MusesConfig.TIMEOUT)
		    .setTcpNoDelay(true)
		    .setSoKeepAlive(true)
		    .setSoReuseAddress(true)
		    .build();
//		final ConnectionConfig connectionConfig = ConnectionConfig.custom()
//		    .setCharset(StandardCharsets.UTF_8)
//		    .build();
		// 如果设置连接管理后面配置无效
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(256);
		connectionManager.setDefaultMaxPerRoute(8);
		connectionManager.setDefaultSocketConfig(socketConfig);
//		connectionManager.setDefaultConnectionConfig(connectionConfig);
		// 清理连接：不会定时
//		connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
//		connectionManager.closeExpiredConnections();
		final RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(MusesConfig.TIMEOUT)
			.setConnectTimeout(MusesConfig.TIMEOUT)
			.setConnectionRequestTimeout(MusesConfig.TIMEOUT)
			.build();
		CLIENT = HttpClients.custom()
//		    .setProxy(null)
//		    .setRetryHandler(null)
		    .setDefaultHeaders(headers)
//		    .setRedirectStrategy(DefaultRedirectStrategy.INSTANCE)
//		    .setKeepAliveStrategy(null)
		    .setConnectionManager(connectionManager)
		    // 需要自己清理连接
//		    .setConnectionManagerShared(true)
//		    .setConnectionReuseStrategy(null)
//		    .setDefaultConnectionConfig(null)
//		    .setDefaultCookieStore(null)
//		    .setDefaultCookieSpecRegistry(null)
//		    .setDefaultSocketConfig(socketConfig)
		    .setDefaultRequestConfig(requestConfig)
		    // 定时清理
		    .evictIdleConnections(30, TimeUnit.SECONDS)
//		    .evictExpiredConnections()
//		    .setConnectionTimeToLive(1, TimeUnit.MINUTES)
//		    .setMaxConnTotal(256)
//		    .setMaxConnPerRoute(4)
//		    .disableAuthCaching()
//		    .disableCookieManagement()
		    // 默认重试三次
		    .disableAutomaticRetries()
		    .build();
	}
	
	public static final String get(String url) {
		return get(url, null, null, MusesConfig.TIMEOUT);
	}

	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}

	public static final String get(String url, String body) {
		return get(url, body, null, MusesConfig.TIMEOUT);
	}

	public static final String get(String url, String body, Map<String, String> headers, int timeout) {
		final HttpGet get = new HttpGet(url);
		addHeaders(get, headers);
		requestConfig(get, timeout);
		if(StringUtils.isNotEmpty(body)) {
			get.setEntity(new StringEntity(body, MusesConfig.CHARSET));
		}
		return execute(get);
	}
	
	private static final class HttpGet extends HttpEntityEnclosingRequestBase {

		public final static String METHOD_NAME = "GET";

		@Override
		public String getMethod() {
			return METHOD_NAME;
		}

		HttpGet(final String uri) {
			this.setURI(URI.create(uri));
		}
		
	}
	
	public static final String post(String url, Map<String, Object> body) {
		return post(url, body, null, MusesConfig.TIMEOUT);
	}
	
	public static final String post(String url, Map<String, Object> body, int timeout) {
		return post(url, body, null, timeout);
	}
	
	public static final String post(String url, Map<String, Object> body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		addHeaders(post, headers);
		requestConfig(post, timeout);
		post.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
		if(MapUtils.isNotEmpty(body)) {
			try {
				post.setEntity(new UrlEncodedFormEntity(buildFormParams(body), MusesConfig.CHARSET));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("设置请求参数异常", e);
			}
		}
		return execute(post);
	}
	
	public static final String post(String url, String body) {
		return post(url, body, null, MusesConfig.TIMEOUT);
	}
	
	public static final String post(String url, String body, int timeout) {
		return post(url, body, null, timeout);
	}
	
	public static final String post(String url, String body, Map<String, String> headers, int timeout) {
		final HttpPost post = new HttpPost(url);
		addHeaders(post, headers);
		requestConfig(post, timeout);
		if(StringUtils.isNotEmpty(body)) {
			post.setEntity(new StringEntity(body, MusesConfig.CHARSET));
		}
		return execute(post);
	}
	
	private static final void addHeaders(HttpRequestBase request, Map<String, String> headers) {
	    if(MapUtils.isNotEmpty(headers)) {
	        headers.forEach(request::addHeader);
	    }
	}
	
	private static final void requestConfig(HttpRequestBase request, int timeout) {
	    if(timeout <= 0 || timeout == MusesConfig.TIMEOUT) {
	        return;
	    }
	    final RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .build();
	    request.setConfig(requestConfig);
	}

	/**
	 * <p>生成参数集合</p>
	 * 
	 * @param body 数据
	 * 
	 * @return 参数集合
	 */
	private static final List<NameValuePair> buildFormParams(Map<String, Object> body) {
		if(MapUtils.isNotEmpty(body)) {
			return body.entrySet().stream()
				.map(entity -> new BasicNameValuePair(entity.getKey(), String.valueOf(entity.getValue())))
				.collect(Collectors.toList());
		}
		return List.of();
	}
	
	/**
	 * <p>执行请求</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 返回内容
	 */
	private static final String execute(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		try {
			response = CLIENT.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK) {
				LOGGER.warn("HTTP返回错误状态：{}-{}-{}", statusCode, request, response);
			}
			return EntityUtils.toString(response.getEntity(), MusesConfig.CHARSET);
		} catch(Exception e) {
			LOGGER.error("发送请求异常", e);
		} finally {
			close(response);
		}
		return null;
	}
	
	/**
	 * <p>SSL工厂</p>
	 */
	private static final SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslFactory = null;
		try {
			sslContext = SSLContextBuilder.create().setProtocol("TLSv1.2").loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有证书
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOGGER.error("创建SSL工程异常", e);
		}
		sslFactory = new SSLConnectionSocketFactory(sslContext);
//		sslFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
//		sslFactory = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1.2", "TLSv1.3"}, new String[] {
//			"TLS_AES_128_GCM_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
//			"TLS_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256"
//		}, new HostnameVerifier() {
//			@Override
//			public boolean verify(String host, SSLSession session) {
//				return true;
//			}
//		});
		return sslFactory;
	}

	/**
	 * <p>关闭资源</p>
	 * 
	 * @param response 请求响应
	 */
	private static final void close(CloseableHttpResponse response) {
		if(response != null) {
			try {
			    // 归还连接
				response.close();
			} catch (IOException e) {
				LOGGER.error("关闭响应异常", e);
			}
		}
	}
	
	/**
	 * <p>工具关闭</p>
	 */
	public static final void shutdown() {
		if(CLIENT != null) {
			try {
			    // 自动清理连接管理：manager
				CLIENT.close();
			} catch (IOException e) {
				LOGGER.error("关闭连接异常", e);
			}
		}
	}
	
}