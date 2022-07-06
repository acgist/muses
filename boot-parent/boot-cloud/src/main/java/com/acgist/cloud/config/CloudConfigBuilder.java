package com.acgist.cloud.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.utils.JSONUtils;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import lombok.extern.slf4j.Slf4j;

/**
 * Cloud配置Builder
 * 
 * @author acgist
 */
@Slf4j
public final class CloudConfigBuilder {
	
	/**
	 * Cloud配置
	 */
	private CloudConfig cloudConfig;
	/**
	 * ConfigService
	 */
	private final ConfigService configService;
	/**
	 * NacosConfigProperties
	 */
	private final NacosConfigProperties nacosConfigProperties;
	
	private CloudConfigBuilder(NacosConfigManager nacosConfigManager) {
		this.configService = nacosConfigManager.getConfigService();
		this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
	}
	
	/**
	 * 获取Cloud配置Builder
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return Cloud配置Builder
	 */
	public static final CloudConfigBuilder builder(NacosConfigManager nacosConfigManager) {
		return new CloudConfigBuilder(nacosConfigManager);
	}
	
	/**
	 * 初始Cloud配置
	 * 
	 * @return this
	 */
	public CloudConfigBuilder init() {
		try {
			// 拉取配置中心配置
			final String oldConfig = this.configService.getConfig(
				MusesConfig.CLOUD_CONFIG,
				this.nacosConfigProperties.getGroup(),
				MusesConfig.TIMEOUT
			);
			if(StringUtils.isEmpty(oldConfig)) {
				this.cloudConfig = new CloudConfig();
			} else {
				this.cloudConfig = JSONUtils.toJava(oldConfig, CloudConfig.class);
			}
		} catch (NacosException e) {
			log.error("初始Cloud配置异常", e);
		}
		return this;
	}
	
	/**
	 * 设置当前服务机器编号
	 * 
	 * @param sn sn
	 * @param serviceName 服务名称
	 * 
	 * @return this
	 */
	public CloudConfigBuilder buildSn(int sn, String serviceName) {
		Map<String, Integer> sns = this.cloudConfig.getSns();
		if(sns == null) {
			sns = new HashMap<>();
			this.cloudConfig.setSns(sns);
		}
		if(sn < MusesConfig.CLOUD_MIN_SN) {
			// 负数自动生成机器编号
			sn = sns.getOrDefault(serviceName, MusesConfig.CLOUD_MIN_SN);
			if (++sn >= MusesConfig.CLOUD_MAX_SN) {
				sn = MusesConfig.CLOUD_MIN_SN;
			}
		}
		sns.put(serviceName, sn);
		this.cloudConfig.setSn(sn);
		log.info("当前服务机器编号：{}", sn);
		return this;
	}
	
	/**
	 * 设置当前服务系统PID
	 * 
	 * @return this
	 */
	public CloudConfigBuilder buildPid() {
		final long pid = ProcessHandle.current().pid();
		this.cloudConfig.setPid((int) pid);
		log.info("当前服务系统PID：{}", pid);
		return this;
	}
	
	/**
	 * 设置当前服务系统端口
	 * 
	 * @param port 端口
	 * 
	 * @return this
	 */
	public CloudConfigBuilder buildPort(int port) {
		this.cloudConfig.setPort(port);
		log.info("当前服务系统端口：{}", port);
		return this;
	}
	
	/**
	 * 创建Cloud配置
	 * 
	 * @return Cloud配置
	 */
	public CloudConfig build() {
		try {
			// 推送配置中心配置
			this.configService.publishConfig(
				MusesConfig.CLOUD_CONFIG,
				this.nacosConfigProperties.getGroup(),
				JSONUtils.toJSON(this.cloudConfig)
			);
		} catch (NacosException e) {
			log.error("创建Cloud配置异常", e);
		}
		return this.cloudConfig;
	}
	
}
