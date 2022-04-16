package com.acgist.rest.controller;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.CostUtils;
import com.acgist.boot.HTTPUtils;
import com.acgist.boot.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserControllerTest {

	@Test
	public void testUser() {
		log.info("{}", HTTPUtils.get(
			"http://localhost:19298/user",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
	}
	
	@Test
	public void testException() {
		log.info("{}", HTTPUtils.get("http://localhost:9090/404"));
		log.info("{}", HTTPUtils.get("http://localhost:9090/user"));
		log.info("{}", HTTPUtils.post("http://localhost:9090/user", ""));
		log.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		log.info("{}", HTTPUtils.get(
			"http://localhost:9090/user/404",
			"",
			Map.of(),
			1000
		));
		log.info("{}", HTTPUtils.get("http://localhost:8888/rest/404"));
		log.info("{}", HTTPUtils.get("http://localhost:8888/rest/user"));
		log.info("{}", HTTPUtils.post("http://localhost:8888/rest/user", ""));
		log.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
			1000
		));
		log.info("{}", HTTPUtils.get(
			"http://localhost:8888/rest/user/404",
			"",
			Map.of(),
			1000
		));
	}
	
	@Test
	public void testCost() {
		final long rest = CostUtils.costed(1000, () -> {
			HTTPUtils.get(
				"http://localhost:19570/user",
				"",
				Map.of(User.HEADER_CURRENT_USER, new User().currentUser().toString()),
				1000
			);
		});
		final long gateway = CostUtils.costed(1000, () -> {
			HTTPUtils.post(
				"http://localhost:8888/rest/user",
				"",
				Map.of("Authorization", "Bearer eyJraWQiOiI2NzE5MTg1NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViIiwibmJmIjoxNjUwMDMxNTY0LCJzY29wZSI6WyJhbGwiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojk5OTkiLCJleHAiOjE2NTAwMzg3NjQsImlhdCI6MTY1MDAzMTU2NH0.D7si30_ckCgUe3NBk3Zt8L7ebKpUCh8XzklvxE7ffjvZlblz8Q0NtS16_1mb-uyiYho4G30_WvJe_GQHIC9xE4qxTC3t3D93vCqcUSGxDSQ5eFMe1iy6LpnlIyMf1QWSUq6Tuu3LyKbFHxgCSZ7IBYE-13UR9wPsHRw8vWqyYQei0MNXpOpqp8k1aTi80ZF5QWV7Wae14qE_6dAXWXnTDh0D5EVLxpQGXxS9raXp97nJ_54d-oBdITlBsndfNmK-abjJBLZuBEC32h6u_ZzA2dJPQ3KiiDN189pEJ8VPk3f_wY8yfTgwICrbu-yrVGVn5nDbJZe6g2d5ih_XOY0FwQ"),
				1000
			);
		});
		log.info("Rest执行时间：{}，每秒执行数量：{}", rest, 1000000 / rest);
		log.info("Gateway执行时间：{}，每秒执行数量：{}", gateway, 1000000 / gateway);
	}
	
}
