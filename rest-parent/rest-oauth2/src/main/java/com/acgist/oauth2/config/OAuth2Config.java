package com.acgist.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OAuth2配置
 * 
 * @author acgist
 */
@ConfigurationProperties(prefix = "system.oauth2")
public class OAuth2Config {

	/**
	 * 客户端：web
	 */
	public static final String CLIENT_WEB = "web";
	/**
	 * 客户端rest
	 */
	public static final String CLIENT_REST = "rest";

	/**
	 * Web密码
	 */
	private String web;
	/**
	 * Rest密码
	 */
	private String rest;
	/**
	 * Code刷新时间
	 */
	private int code;
	/**
	 * Access Code刷新时间
	 */
	private int access;
	/**
	 * Refresh Code刷新时间
	 */
	private int refresh;
	/**
	 * 跳转地址
	 */
	private String redirectUri;

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

}
