package com.acgist.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Oauth2配置
 * 
 * @author acgist
 */
@ConfigurationProperties(prefix = "system.oauth2")
public class Oauth2Config {

	/**
	 * 客户端：web
	 */
	public static final String CLIENT_WEB = "web";
	/**
	 * 客户端rest
	 */
	public static final String CLIENT_REST = "rest";

	private String web;
	private String rest;
	private int code;
	private int access;
	private int refresh;
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
