package com.acgist.boot.config;

import java.lang.management.ManagementFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.StringUtils;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

/**
 * 系统配置Builder
 * 
 * @author acgist
 */
public final class MusesConfigBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MusesConfigBuilder.class);

	/**
	 * 系统配置
	 */
	private MusesConfig musesConfig;
	
	private MusesConfigBuilder() {
	}
	
	/**
	 * 获取系统配置Builder
	 * 
	 * @return 系统配置Builder
	 */
	public static final MusesConfigBuilder builder() {
		return new MusesConfigBuilder();
	}
	
	/**
	 * 初始系统配置
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return this
	 */
	public MusesConfigBuilder init(NacosConfigManager nacosConfigManager) {
		final ConfigService configService = nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
		try {
			final String oldConfig = configService.getConfig(MusesConfig.MUSES_CONFIG, nacosConfigProperties.getGroup(), MusesConfig.TIMEOUT);
			if(StringUtils.isEmpty(oldConfig)) {
				this.musesConfig = new MusesConfig();
			} else {
				this.musesConfig = JSONUtils.toJava(oldConfig, MusesConfig.class);
			}
		} catch (NacosException e) {
			LOGGER.error("初始系统配置异常", e);
		}
		return this;
	}
	
	/**
	 * 设置当前系统编号
	 * 
	 * @param sn sn
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildSn(int sn) {
		if(sn < 0) {
		} else {
			sn = this.musesConfig.getSn();
			if (++sn >= MusesConfig.MAX_SN) {
				sn = 0;
			}
		}
		this.musesConfig.setSn(sn);
		LOGGER.info("系统编号：{}", sn);
		return this;
	}
	
	/**
	 * 设置当前系统PID
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildPid() {
//		final long pid = ManagementFactory.getRuntimeMXBean().getPid();
//		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final long pid = ProcessHandle.current().pid();
		this.musesConfig.setPid((int) pid);
		LOGGER.info("系统PID：{}", pid);
		return this;
	}
	
	/**
	 * 设置当前系统端口
	 * 
	 * @return this
	 */
	public MusesConfigBuilder buildPort() {
		// TODO：port
		LOGGER.info("系统端口：{}", this.musesConfig.getPort());
		return this;
	}
	
	/**
	 * 创建保存系统配置
	 * 
	 * @param nacosConfigManager NacosConfigManager
	 * 
	 * @return
	 */
	public MusesConfig build(NacosConfigManager nacosConfigManager) {
		final ConfigService configService = nacosConfigManager.getConfigService();
		final NacosConfigProperties nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
		try {
			configService.publishConfig(MusesConfig.MUSES_CONFIG, nacosConfigProperties.getGroup(), JSONUtils.toJSON(this.musesConfig));
		} catch (NacosException e) {
			LOGGER.error("设置系统配置异常", e);
		}
		return this.musesConfig;
	}
	
}
