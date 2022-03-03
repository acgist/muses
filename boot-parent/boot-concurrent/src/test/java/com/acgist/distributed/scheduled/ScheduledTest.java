package com.acgist.distributed.scheduled;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.ConcurrentApplication;

@SpringBootTest(classes = ConcurrentApplication.class)
public class ScheduledTest {

	@Test
	public void testScheduled() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}

}
