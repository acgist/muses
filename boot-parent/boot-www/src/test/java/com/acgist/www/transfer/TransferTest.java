package com.acgist.www.transfer;

import org.junit.jupiter.api.Test;

import com.acgist.boot.JSONUtils;

import lombok.Getter;
import lombok.Setter;

public class TransferTest {

	@Getter
	@Setter
	public static class User {
		
		@Transfer(group = "name")
		private String age;
		@Transfer(group = "name")
		private String name;
		
	}
	
	@Test
	public void testTransfer() {
		final User user = new User();
		user.setAge("11");
		user.setName("acgist");
		System.out.println(JSONUtils.toJSON(user));
	}
	
}
