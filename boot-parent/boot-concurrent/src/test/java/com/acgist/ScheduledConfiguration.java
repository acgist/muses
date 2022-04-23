package com.acgist;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.acgist.distributed.config.DistributedScheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ScheduledConfiguration {

	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-a", ttl = 10)
	public void scheduledGroupA() {
		log.info("scheduledGroupA");
	}

	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-b", ttl = 10)
	public void scheduledGroupB() {
		log.info("scheduledGroupB");
	}
	
}
