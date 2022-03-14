package com.acgist.gateway.notify;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acgist.gateway.model.entity.GatewayEntity;

/**
 * 发送通知
 * 
 * @author acgist
 *
 */
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
		this.notifies.forEach(notify -> LOGGER.info("通知类型：{}", notify.name()));
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
				LOGGER.warn("没有匹配通知类型：{}", gateway);
			});
	}
	
}
