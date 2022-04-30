package com.acgist.cloud.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.boot.service.IdService;
import com.acgist.cloud.config.CloudConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdServiceImpl implements IdService {

	/**
	 * 最大序号
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
	private CloudConfig cloudConfig;
	
	@PostConstruct
	public void init() {
		this.sn = this.cloudConfig.getSn();
		final Random random = new Random();
		this.index = random.nextInt(MAX_INDEX);
		log.info("雪花ID：{}-{}", this.sn, this.index);
	}

	@Override
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
