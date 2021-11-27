package com.acgist.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>工具 - HTTP</p>
 * 
 * @author acgist
 */
public final class HTTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);
	
	/**
	 * <p>是否复用：true：复用，false：不复用</p>
	 */
	private static final boolean REUSE = true;
	/**
	 * <p>超时时间</p>
	 */
	private static final int DEFAULT_TIMEOUT = 30 * 1000;
	/**
	 * <p>HTTPS请求</p>
	 */
	private static final String HTTPS_PREFIX = "https://";
	/**
	 * <p>默认编码</p>
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	/**
	 * <p>浏览器参数</p>
	 */
	private static final Map<String, String> BROWSER_HEADERS = new HashMap<>();
	/**
	 * <p>复用TCP连接</p>
	 * <p>不能关闭Client：工具自动管理，不同的域名会使用不同的TCP连接。</p>
	 */
	private static final CloseableHttpClient REUSE_CLIENT;
	/**
	 * <p>复用连接管理</p>
	 */
	private static final PoolingHttpClientConnectionManager MANAGER;
	
	static {
		BROWSER_HEADERS.put("User-Agent", "acgist");
		if (REUSE) {
			final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", createSSLConnSocketFactory()).build();
//				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
			MANAGER = new PoolingHttpClientConnectionManager(registry);
			REUSE_CLIENT = HttpClients.custom().setRedirectStrategy(DefaultRedirectStrategy.getInstance()).setConnectionManager(MANAGER).build();
		}
	}
	
	/**
	 * <p>发起get请求</p>
	 * 
	 * @param url url
	 * 
	 * @return 请求返回内容
	 */
	public static final String get(String url) {
		return get(url, null, null, DEFAULT_TIMEOUT);
	}
	
	/**
	 * <p>发起get请求</p>
	 * 
	 * @param url url
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}
	
	/**
	 * <p>发起get请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * 
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data) {
		return get(url, data, null, DEFAULT_TIMEOUT);
	}
	
	/**
	 * <p>发起get请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		String content = null;
		url = generateUrl(url, data);
		if(StringUtils.isEmpty(url)) {
			return content;
		}
		final HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig(timeout));
		final CloseableHttpClient client = generateClient(url);
		addHeaders(get, headers);
		try {
			content = invoke(client, get);
		} catch (Exception e) {
			LOGGER.error("HTTP GET请求异常，请求地址：{}，请求参数：{}", url, data, e);
		} finally {
			close(client, null);
		}
		return content;
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data) {
		return post(url, data, null, DEFAULT_TIMEOUT);
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, int timeout) {
		return post(url, data, null, timeout);
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		String content = null;
		if(StringUtils.isEmpty(url)) {
			return content;
		}
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig(timeout));
		final CloseableHttpClient client = generateClient(url); 
		addHeaders(post, headers);
		try {
			post.setEntity(new UrlEncodedFormEntity(generateParams(data), DEFAULT_CHARSET));
			content = invoke(client, post);
		} catch (Exception e) {
			LOGGER.error("HTTP POST请求异常，请求地址：{}，请求参数：{}", url, data, e);
		} finally {
			close(client, null);
		}
		return content;
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 内容
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, String data) {
		return post(url, data, null, DEFAULT_TIMEOUT);
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 内容
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, String data, int timeout) {
		return post(url, data, null, timeout);
	}
	
	/**
	 * <p>发起post请求</p>
	 * 
	 * @param url url
	 * @param data 参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * 
	 * @return 请求返回内容
	 */
	public static final String post(String url, String data, Map<String, String> headers, int timeout) {
		String content = null;
		if(StringUtils.isEmpty(url)) {
			return content;
		}
		final HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig(timeout));
		final CloseableHttpClient client = generateClient(url); 
		addHeaders(post, headers);
		try {
			post.setEntity(new StringEntity(data, DEFAULT_CHARSET));
			content = invoke(client, post);
		} catch (Exception e) {
			LOGGER.error("HTTP POST请求异常，请求地址：{}，请求参数：{}", url, data, e);
		} finally {
			close(client, null);
		}
		return content;
	}
	
	/**
	 * <p>根据参数生成url</p>
	 * 
	 * @param url 原始url
	 * @param data 参数
	 * 
	 * @return 根据参数生成的url
	 */
	private static final String generateUrl(String url, Map<String, Object> data) {
		if(StringUtils.isEmpty(url)) {
			return url;
		}
		if(MapUtils.isNotEmpty(data)) {
			final String params = generateParamsString(data);
			if(url.indexOf("?") == -1) {
				url = url + "?" + params;
			} else if(url.endsWith("?")) {
				url += params;
			} else if(url.endsWith("&")) {
				url += params;
			} else {
				url = url + "&" + params;
			}
		}
		return url;
	}
	
	/**
	 * <p>设置请求信息</p>
	 * 
	 * @param timeout 超时shijian
	 * 
	 * @return 请求配置
	 */
	private static final RequestConfig requestConfig(int timeout) {
		return RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
	}
	
	/**
	 * <p>根据URL创建合适的client</p>
	 * 
	 * @param url 请求地址
	 * 
	 * @return client
	 */
	private static final CloseableHttpClient generateClient(String url) {
		if (REUSE) {
			return REUSE_CLIENT;
		} else {
			if(StringUtils.startsWith(url, HTTPS_PREFIX)) {
				return HttpClients.custom().setRedirectStrategy(DefaultRedirectStrategy.getInstance()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
			}
			return HttpClients.custom().setRedirectStrategy(DefaultRedirectStrategy.getInstance()).build();
		}
	}

	/**
	 * <p>添加请求头</p>
	 * 
	 * @param base 请求
	 * @param headers 请求头
	 */
	private static final void addHeaders(HttpRequestBase base, Map<String, String> headers) {
		BROWSER_HEADERS.forEach((key, value) -> {
			base.addHeader(key, value);
		});
		if(headers != null) {
			headers.forEach((key, value) -> {
				base.addHeader(key, value);
			});
		}
	}

	/**
	 * <p>生成参数字符串</p>
	 * 
	 * @param data 数据
	 * 
	 * @return 参数字符串
	 */
	private static final String generateParamsString(Map<String, Object> data) {
		final List<NameValuePair> list = generateParams(data);
		try {
			return EntityUtils.toString(new UrlEncodedFormEntity(list, DEFAULT_CHARSET));
		} catch (ParseException | IOException e) {
			LOGGER.error("组装参数异常：{}", data, e);
		}
		return null;
	}
	
	/**
	 * <p>生成参数集合</p>
	 * 
	 * @param data 数据
	 * 
	 * @return 参数集合
	 */
	private static final List<NameValuePair> generateParams(Map<String, Object> data) {
		final List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(data != null) {
			data.forEach((key, value) -> {
				list.add(new BasicNameValuePair(key, String.valueOf(value))); 
			});
		}
		return list;
	}
	
	/**
	 * <p>执行请求</p>
	 * 
	 * @param client client
	 * @param request 请求
	 * 
	 * @return 返回内容
	 */
	private static final String invoke(CloseableHttpClient client, HttpUriRequest request) {
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK) {
				content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
			} else {
				content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
				LOGGER.error("HTTP返回错误状态代码：{}", statusCode);
			}
		} catch (IOException e) {
			LOGGER.error("HTTP请求异常", e);
		} catch (Exception e) {
			LOGGER.error("HTTP请求异常", e);
		} finally {
			close(null, response);
		}
		return content;
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
	 * @param client 客户端
	 * @param response 请求响应
	 */
	private static final void close(CloseableHttpClient client, CloseableHttpResponse response) {
		if(response != null) {
			try {
				response.close();
			} catch (IOException e) {
				response = null;
				LOGGER.error("关闭响应异常", e);
			}
		}
		// 复用不能关闭Client
		if(REUSE) {
			return;
		}
		if(client != null) {
			try {
				client.close();
			} catch (IOException e) {
				client = null;
				LOGGER.error("关闭连接异常", e);
			}
		}
	}
	
	/**
	 * <p>工具关闭</p>
	 */
	public static final void shutdown() {
		if(REUSE_CLIENT != null) {
			try {
				REUSE_CLIENT.close();
			} catch (IOException e) {
				LOGGER.error("关闭连接异常", e);
			}
		}
		if(MANAGER != null) {
			MANAGER.shutdown();
		}
	}
	
}

/**
 * <p>请求异常</p>
 */
class RequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequestException(String content) {
		super(content);
	}
	
}

/**
 * <p>重定向策略</p>
 * <p>307重定向只支持POST</p>
 * <p>303和302重定向支持所有协议</p>
 */
class DefaultRedirectStrategy implements RedirectStrategy {
	
	private DefaultRedirectStrategy() {
	}
	
	private static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
	
	public static final DefaultRedirectStrategy getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		final String method = request.getRequestLine().getMethod();
		final int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case HttpStatus.SC_TEMPORARY_REDIRECT:
			return HttpPost.METHOD_NAME.equalsIgnoreCase(method);
		case HttpStatus.SC_MOVED_TEMPORARILY:
		case HttpStatus.SC_SEE_OTHER:
			return true;
		default:
			return false;
		}
	}

	public URI getLocationURI(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
		final HttpClientContext clientContext = HttpClientContext.adapt(context);
		final Header locationHeader = response.getFirstHeader("location");
		if (locationHeader == null) {
			throw new ProtocolException("未返回重定向location首部");
		}
		final String location = locationHeader.getValue();
		final RequestConfig config = clientContext.getRequestConfig();
		URI uri = createLocationURI(location);
		try {
			if (!uri.isAbsolute()) {
				if (!config.isRelativeRedirectsAllowed()) {
					throw new ProtocolException("不允许重定向：" + uri);
				}
				final HttpHost target = clientContext.getTargetHost();
				final URI requestURI = new URI(request.getRequestLine().getUri());
				final URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target);
				uri = URIUtils.resolve(absoluteRequestURI, uri);
			}
		} catch (final URISyntaxException ex) {
			throw new ProtocolException(ex.getMessage(), ex);
		}
		RedirectLocations redirectLocations = (RedirectLocations) clientContext.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
		if (redirectLocations == null) {
			redirectLocations = new RedirectLocations();
			context.setAttribute(HttpClientContext.REDIRECT_LOCATIONS, redirectLocations);
		}
		if (!config.isCircularRedirectsAllowed()) {
			if (redirectLocations.contains(uri)) {
				throw new CircularRedirectException("无限循环重定向");
			}
		}
		redirectLocations.add(uri);
		return uri;
	}

	protected URI createLocationURI(final String location) throws ProtocolException {
		try {
			final URIBuilder builder = new URIBuilder(new URI(location).normalize());
			final String host = builder.getHost();
			if (host != null) {
				builder.setHost(host.toLowerCase(Locale.ROOT));
			}
			final String path = builder.getPath();
			if (TextUtils.isEmpty(path)) {
				builder.setPath("/");
			}
			return builder.build();
		} catch (final URISyntaxException ex) {
			throw new ProtocolException("非法的location：" + location, ex);
		}
	}
	
	@Override
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		final URI uri = getLocationURI(request, response, context);
		final String method = request.getRequestLine().getMethod();
		if (HttpGet.METHOD_NAME.equalsIgnoreCase(method)) { // 忽略参数
			return new HttpGet(uri);
		} else {
			final int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_TEMPORARY_REDIRECT) {
				return RequestBuilder.copy(request).setUri(uri).build();
			} else {
				return new HttpGet(uri); // 忽略参数
			}
		}
	}

}
