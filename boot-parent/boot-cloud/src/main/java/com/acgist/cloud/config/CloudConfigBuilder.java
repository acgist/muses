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
 * 系统配置Builder
 * 
 * @author acgist
 */
@Slf4j
public final class CloudConfigBuilder {
	
	/**
	 * 系统配置
	 */
	private CloudConfig cloudConfig;
	/**
	 * NacosConfigManager
	 */
	private final NacosConfigManager nacosConfigManager;
	
	private CloudConfigBuilder(NacosConfigManager nacosConfigManager) {
		this.nacosConfigManager = nacosConfigManager;
	}
	
	/**
	 * 获取系统配置Builder
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return 系统配置Builder
	 */
	public static final CloudConfigBuilder builder(NacosConfigManager nacosConfigManager) {
		final CloudConfigBuilder builder = new CloudConfigBuilder(nacosConfigManager);
		return builder.init();
	}
	
	/**
	 * 初始系统配置
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return this
	 */
	private CloudConfigBuilder init() {
		final ConfigService configService = this.nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = this.nacosConfigManager.getNacosConfigProperties();
		try {
			final String oldConfig = configService.getConfig(MusesConfig.CLOUD_CONFIG, nacosConfigProperties.getGroup(), MusesConfig.TIMEOUT);
			if(StringUtils.isEmpty(oldConfig)) {
				this.cloudConfig = new CloudConfig();
			} else {
				this.cloudConfig = JSONUtils.toJava(oldConfig, CloudConfig.class);
			}
		} catch (NacosException e) {
			log.error("初始系统配置异常", e);
		}
		return this;
	}
	
	/**
	 * 设置当前服务系统编号
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
		if(sn < 0) {
			// 负数自动生成机器序号
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
	 * 创建系统配置
	 * 
	 * @return 系统配置
	 */
	public CloudConfig build() {
		final ConfigService configService = this.nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = this.nacosConfigManager.getNacosConfigProperties();
		try {
			// 保存配置中心
			configService.publishConfig(MusesConfig.CLOUD_CONFIG, nacosConfigProperties.getGroup(), JSONUtils.toJSON(this.cloudConfig));
		} catch (NacosException e) {
			log.error("设置系统配置异常", e);
		}
		return this.cloudConfig;
	}
	
}
