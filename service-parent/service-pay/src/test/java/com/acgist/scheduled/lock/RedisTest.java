package com.acgist.scheduled.lock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

	@Autowired
	private RedisLock redisLock;

	@Test
	public void testLock() {
		this.redisLock.tryLock("acgist", 10);
		this.redisLock.tryLock("acgist", 10);
		this.redisLock.tryLock("acgist", 10);
		this.redisLock.tryLock("acgist", 10);
		this.redisLock.unlock("acgist");
		this.redisLock.unlock("acgist");
		this.redisLock.unlock("acgist");
		this.redisLock.unlock("acgist");
		assertTrue(this.redisLock.tryLock("acgist", 1));
	}
	
	@Test
	public void testUnlock() {
		this.redisLock.unlock("acgist");
	}
	
	@Test
	public void testThreadLock() throws InterruptedException {
		final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		this.redisLock.tryLock("acgist", 100);
		final Thread thread = new Thread(() -> atomicBoolean.set(this.redisLock.tryLock("acgist", 10)));
		thread.start();
		thread.join();
		this.redisLock.unlock("acgist");
		assertFalse(atomicBoolean.get());
	}

	@Test
	public void testThread() {
		try {
			if (this.redisLock.tryLock("acgist", 100000, 1000)) {
				System.out.println("加锁成功" + Thread.currentThread().getId());
				try {
					if (this.redisLock.tryLock("acgist", 100000, 1000)) {
						System.out.println("加锁重入成功" + Thread.currentThread().getId());
					} else {
						System.err.println("加锁重入失败");
					}
				} finally {
					this.redisLock.unlock("acgist");
				}
			} else {
				System.err.println("加锁失败");
			}
		} finally {
			this.redisLock.unlock("acgist");
		}
	}

	@Test
	public void testThreads() throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(100);
		final int count = 1000;
		final long start = System.currentTimeMillis();
		final CountDownLatch latch = new CountDownLatch(count);
		for (int index = 0; index < count; index++) {
			executor.submit(() -> {
				this.testThread();
				latch.countDown();
			});
		}
		latch.await();
		System.out.println(System.currentTimeMillis() - start);
	}

}
