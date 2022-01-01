package com.acgist.boot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionUtilsTest {

	@Test
	public void testGetFirst() {
		assertNull(CollectionUtils.getFirst(null));
		assertNull(CollectionUtils.getFirst(List.of()));
		assertEquals("1", CollectionUtils.getFirst(List.of("1")));
		assertEquals("1", CollectionUtils.getFirst(List.of("1", "2")));
		CostUtils.costed(100000, () -> CollectionUtils.getFirst(List.of("1", "2")));
	}
	
}
