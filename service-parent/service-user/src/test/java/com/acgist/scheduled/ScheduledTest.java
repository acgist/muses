package com.acgist.scheduled;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduledTest {

	@Test
	public void testScheduled() throws InterruptedException {
		Thread.sleep(100000);
	}

}
