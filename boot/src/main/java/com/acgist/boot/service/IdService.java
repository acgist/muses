package com.acgist.boot.service;

import java.time.LocalDateTime;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.boot.config.MusesConfig;

/**
 * ID生成
 * 
 * TODO：没有配置sn根据当前系统应用信息自动生成
 * 
 * @author acgist
 */
public class IdService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdService.class);

	/**
	 * 最大系统编号
	 */
	private static final int MAX_INDEX = 9999;

	/**
	 * 当前系统编号
	 */
	private int sn;
	/**
	 * 当前序号
	 */
	private int index;
	
	@Autowired
	private MusesConfig musesConfig;
	
	@PostConstruct
	public void init() {
		this.sn = this.musesConfig.getSn();
		final Random random = new Random();
		this.index = random.nextInt(MAX_INDEX);
		LOGGER.info("雪花ID：{}-{}", this.sn, this.index);
	}

	/**
	 * 生成十八位的ID：YYMMddHHmmss + sn + xxxx
	 * 
	 * @return ID
	 */
	public Long id() {
		synchronized (this) {
			if (++this.index > MAX_INDEX) {
				this.index = 0;
			}
		}
		final LocalDateTime time = LocalDateTime.now();
		return
			10000000000000000L * (time.getYear() % 100) +
			100000000000000L * time.getMonthValue() +
			1000000000000L * time.getDayOfMonth() +
			10000000000L * time.getHour() +
			100000000L * time.getMinute() +
			1000000L * time.getSecond() +
			10000L * this.sn +
			this.index;
	}

}
