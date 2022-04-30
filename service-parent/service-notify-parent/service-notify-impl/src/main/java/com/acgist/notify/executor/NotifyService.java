package com.acgist.notify.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acgist.boot.model.Model;

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

	@Autowired
	private ApplicationContext context;

	/**
	 * 通知类型
	 */
	private Collection<Notify<? extends Model, ? extends NotifyConfig>> notifies;
	
	@PostConstruct
	public void init() {
		this.notifies = new ArrayList<>();
		this.context.getBeansOfType(Notify.class).values().stream()
			.filter(Notify::enabled)
			.sorted(Comparator.comparing(notify -> Objects.requireNonNullElseGet(notify.sorted(), () -> 0)))
			.forEach(this.notifies::add);
//			// 指定类型防止泛型推断错误
//			.collect(Collectors.toCollection(ArrayList<Notify<? extends Model, ? extends NotifyConfig>>::new));
		this.notifies.forEach(notify -> log.info("通知类型：{}", notify.name()));
	}
	
	/**
	 * 发送通知
	 * 
	 * @param value 通知信息
	 * 
	 * @return 通知结果
	 */
	public boolean notify(Object value) {
		final AtomicBoolean result = new AtomicBoolean(false);
		this.notifies.stream()
			.filter(notify -> notify.match(value))
			.findFirst()
			.ifPresentOrElse(notify -> {
				result.set(notify.notify(value));
			}, () -> {
				log.warn("没有匹配通知类型：{}", value);
			});
		return result.get();
	}
	
}
