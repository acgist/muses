package com.acgist.gateway.notify;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotifyService.class);
	
	private List<Notify> notifies;

	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		this.notifies = this.context.getBeansOfType(Notify.class).values().stream()
			.filter(Notify::enable)
			.sorted()
			.collect(Collectors.toList());
		this.notify("http://www.acgist.com");
	}
	
	public void notify(String url) {
		this.notifies.stream()
			.filter(notify -> notify.match(url))
			.findFirst().ifPresentOrElse(notify -> {
				notify.execute();
			}, () -> {
				LOGGER.warn("没有匹配通知类型：{}", url);
			});
	}
	
}
