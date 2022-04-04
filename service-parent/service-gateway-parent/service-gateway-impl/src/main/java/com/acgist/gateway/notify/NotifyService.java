package com.acgist.gateway.notify;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acgist.gateway.model.entity.GatewayEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送通知
 * 
 * @author acgist
 *
 */
@Slf4j
@Service
public class NotifyService {
	
	private List<Notify> notifies;

	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		this.notifies = this.context.getBeansOfType(Notify.class).values().stream()
			.filter(Notify::enable)
			.sorted()
			.collect(Collectors.toList());
		this.notifies.forEach(notify -> log.info("通知类型：{}", notify.name()));
	}
	
	/**
	 * 发送通知
	 * 
	 * @param gateway 网关信息
	 */
	public void notify(GatewayEntity gateway) {
		// TODO：获取推送地址（注意验证IP地址）
		final String url = gateway.getResponse();
		this.notifies.stream()
			.filter(notify -> notify.match(url))
			.findFirst().ifPresentOrElse(notify -> {
				notify.execute(gateway);
			}, () -> {
				log.warn("没有匹配通知类型：{}", gateway);
			});
	}
	
}
