package com.acgist.scheduled;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.acgist.scheduled.aop.DistributedScheduled;

@Configuration
public class ScheduledConfiguration {

	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-a", ttl = 10)
	public void scheduledGroupA() {
		System.out.println("scheduledGroupA");
	}

	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-b", ttl = 10)
	public void scheduledGroupB() {
		System.out.println("scheduledGroupB");
	}
	
}
