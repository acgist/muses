package com.acgist.concurrent.scheduled;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.concurrent.ConcurrentApplication;

@SpringBootTest(classes = ConcurrentApplication.class)
public class ScheduledTest {

	@Test
	public void testScheduled() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}

}
