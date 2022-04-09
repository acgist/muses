package com.acgist.mssql;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.HikariConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置：数据库连接和数据库连接池配置一项即可
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "acgist.mssql")
public class MssqlProperties {

	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private HikariConfig datasource;

}
