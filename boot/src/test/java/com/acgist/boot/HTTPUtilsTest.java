package com.acgist.boot;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtilsTest.class);
	
	@Test
	public void testCost() {
		CostUtils.costed(100, () -> {
			HTTPUtils.get("https://www.baidu.com");
			HTTPUtils.get("https://www.acgist.com");
		});
	}

	@Test
	public void testUser() {
		final String body = HTTPUtils.post("http://localhost:8888/rest/user", Map.of(), Map.of(
			"Authorization",
			"Bearer eyJraWQiOiIxYjczZjVhNS0yNmMyLTQ1MDAtYWEwYS03NzExNDRmZWRhZTMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoid2ViLWNsaWVudCIsIm5iZiI6MTYzOTgwNzg4Miwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTYzOTgxODY4MiwiaWF0IjoxNjM5ODA3ODgyfQ.MDoAQ_AYpkVkrOikgM_wZaLJvKYFOi5VWypDXhAFwO8zcJX-UqQagHaJloPydXH91MvY6SGqUDi1hn4NxSojm5ZDiYKZJVNPZR47DIMbdaZj9ykCEuHwwwv_7WFVicWP3e_ZrsK0byTzlBuVgnkpO2bmNG6ULIsBTFOD6z_SrBxnjxQ3YLEpLw3FYY9B_mVe--ZvHS_hG7JLt2Yym031FOiDw_XdqQ3U0k5ziE84R4sfOyfkQroSa-RDwgM0W4yHVV1wUMi__AcYdZz2RseCdDRSlJRdDK2jK3rkPJp_DBzcvnFdTRaklW4MNIqm1QL012Y1McQiRUW7hRqrvT28FQ"
		), 1000);
		LOGGER.info("{}", body);
	}
	
}
