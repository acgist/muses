package com.acgist.boot.config;

import java.util.HashMap;
import java.util.Map;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.boot.utils.StringUtils;
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
public final class MusesConfigBuilder {
	
	/**
	 * 系统配置
	 */
	private MusesConfig musesConfig;
	/**
	 * NacosConfigManager
	 */
	private final NacosConfigManager nacosConfigManager;
	
	private MusesConfigBuilder(NacosConfigManager nacosConfigManager) {
		this.nacosConfigManager = nacosConfigManager;
	}
	
	/**
	 * 获取系统配置Builder
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return 系统配置Builder
	 */
	public static final MusesConfigBuilder builder(NacosConfigManager nacosConfigManager) {
		final MusesConfigBuilder builder = new MusesConfigBuilder(nacosConfigManager);
		return builder.init();
	}
	
	/**
	 * 初始系统配置
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return this
	 */
	private MusesConfigBuilder init() {
		final ConfigService configService = this.nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = this.nacosConfigManager.getNacosConfigProperties();
		try {
			final String oldConfig = configService.getConfig(MusesConfig.MUSES_CONFIG, nacosConfigProperties.getGroup(), MusesConfig.TIMEOUT);
			if(StringUtils.isEmpty(oldConfig)) {
				this.musesConfig = new MusesConfig();
			} else {
				this.musesConfig = JSONUtils.toJava(oldConfig, MusesConfig.class);
			}
		} catch (NacosException e) {
			log.error("初始系统配置异常", e);
		}
		return this;
	}
	
	/**
	 * 设置当前系统编号
	 * 
	 * @param sn sn
	 * @param serviceName 服务名称
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildSn(int sn, String serviceName) {
		Map<String, Integer> sns = this.musesConfig.getSns();
		if(sns == null) {
			sns = new HashMap<>();
			this.musesConfig.setSns(sns);
		}
		if(sn < 0) {
			sn = sns.getOrDefault(serviceName, 0);
			if (++sn >= MusesConfig.MAX_SN) {
				sn = 0;
			}
		}
		sns.put(serviceName, sn);
		this.musesConfig.setSn(sn);
		log.info("系统编号：{}", sn);
		return this;
	}
	
	/**
	 * 设置当前系统PID
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildPid() {
		final long pid = ProcessHandle.current().pid();
		this.musesConfig.setPid((int) pid);
		log.info("系统PID：{}", pid);
		return this;
	}
	
	/**
	 * 设置当前系统端口
	 * 
	 * @param port 端口
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildPort(int port) {
		this.musesConfig.setPort(port);
		log.info("系统端口：{}", port);
		return this;
	}
	
	/**
	 * 创建系统配置
	 * 
	 * @return
	 */
	public MusesConfig build() {
		final ConfigService configService = this.nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = this.nacosConfigManager.getNacosConfigProperties();
		try {
			// 保存配置中心
			configService.publishConfig(MusesConfig.MUSES_CONFIG, nacosConfigProperties.getGroup(), JSONUtils.toJSON(this.musesConfig));
		} catch (NacosException e) {
			log.error("设置系统配置异常", e);
		}
		return this.musesConfig;
	}
	
}
