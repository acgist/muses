package com.acgist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.acgist.distributed.scheduled.DistributedScheduled;

@Configuration
public class ScheduledConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledConfiguration.class);
	
	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-a", ttl = 10)
	public void scheduledGroupA() {
		LOGGER.info("scheduledGroupA");
	}

	@Scheduled(cron = "*/5 * * * * ?")
	@DistributedScheduled(name = "group-b", ttl = 10)
	public void scheduledGroupB() {
		LOGGER.info("scheduledGroupB");
	}
	
}
