package com.acgist.log.listener;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.log.model.message.LogMessage;
import com.acgist.log.service.LogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LogListener {
	
	@Autowired
	private LogService logService;
	
	@Bean
	public Consumer<String> logRecord() {
		return message -> {
			if(StringUtils.isEmpty(message)) {
				log.warn("日志格式错误：{}", message);
				return;
			}
			try {
				log.debug("记录日志：{}", message);
				this.logService.log(JSONUtils.toJava(message, LogMessage.class));
			} catch (Exception e) {
				log.error("日志记录异常：{}", message, e);
			}
		};
	}
	
}
