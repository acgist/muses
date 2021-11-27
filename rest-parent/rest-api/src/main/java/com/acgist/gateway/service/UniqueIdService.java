package com.acgist.gateway.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>Service - 编号</p>
 * 
 * @author acgist
 */
@Service
public class UniqueIdService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UniqueIdService.class);

	@Value("${server.sn:1000}")
	private String serverSn;

	private static final int MIN_INDEX = 10000;
	private static final int MAX_INDEX = 99999;
	private static final AtomicInteger INDEX = new AtomicInteger(MIN_INDEX);
	
	@PostConstruct
	public void init() {
		LOGGER.info("系统序号：{}", this.serverSn);
	}
	
	/**
	 * <p>生成系统唯一ID</p>
	 * 
	 * @return 系统唯一ID
	 */
	public String id() {
		final StringBuffer builder = new StringBuffer();
		final SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		builder.append(formater.format(new Date()));
		synchronized (INDEX) {
			builder.append(INDEX.getAndIncrement());
			if(INDEX.get() >= MAX_INDEX) {
				INDEX.set(MIN_INDEX);
			}
		}
		return builder.toString();
	}
	
}
