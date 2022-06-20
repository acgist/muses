package com.acgist.log.canal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.log.canal.json.CanalBooleanDeserializer;
import com.acgist.log.canal.json.CanalLocalDateTimeDeserializer;
import com.acgist.log.model.message.LogMessage;
import com.acgist.user.model.entity.UserEntity;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CanalJsonTest {

	@Test
	public void testJson() {
		final SimpleModule module = new SimpleModule("CanalModule");
		module.addDeserializer(Boolean.class, new CanalBooleanDeserializer());
		module.addDeserializer(LocalDateTime.class, new CanalLocalDateTimeDeserializer());
		JSONUtils.registerModule(module);
		final LogMessage logMessage = JSONUtils.toJava(this.json, LogMessage.class);
		logMessage.getData().forEach(value -> {
			@SuppressWarnings({ "unchecked" })
			final Map<String, Object> map = (Map<String, Object>) value;
			map.forEach((k, v) -> {
				log.info("数据：{}-{}-{}", k, v, v.getClass());
			});
//			final UserEntity user = new UserEntity();
//			BeanMap.create(user).putAll(map);
			final UserEntity user = JSONUtils.toJava(JSONUtils.toJSON(map), UserEntity.class);
			log.info("用户信息：{}", user);
		});
		assertNotNull(logMessage);
	}
	
	public String json = "{\r\n"
		+ "	\"data\": [{\r\n"
		+ "		\"id\": \"1\",\r\n"
		+ "		\"createDate\": \"2022-12-12 12:12:12\",\r\n"
		+ "		\"modifyDate\": \"2022-12-12 12:12:12.000000\",\r\n"
//		+ "		\"create_date\": \"2022-12-12 12:12:12.000000\",\r\n"
//		+ "		\"modify_date\": \"2022-12-12 12:12:12.000000\",\r\n"
		+ "		\"memo\": \"超级用户\",\r\n"
		+ "		\"name\": \"2022-06-19 08:56:54\",\r\n"
		+ "		\"enabled\": \"1\",\r\n"
		+ "		\"sorted\": \"0\",\r\n"
		+ "		\"password\": \"$2a$10$lRFeC7uj.rCiI7p9YLWMAeOqdx.1ZV6iRJzilNs6WagRa0S63wcou\"\r\n"
		+ "	}],\r\n"
		+ "	\"database\": \"zhwg\",\r\n"
		+ "	\"es\": 1655600359000,\r\n"
		+ "	\"id\": 17,\r\n"
		+ "	\"isDdl\": false,\r\n"
		+ "	\"mysqlType\": {\r\n"
		+ "		\"id\": \"bigint(20)\",\r\n"
		+ "		\"create_date\": \"datetime(6)\",\r\n"
		+ "		\"modify_date\": \"datetime(6)\",\r\n"
		+ "		\"memo\": \"varchar(128)\",\r\n"
		+ "		\"name\": \"varchar(32)\",\r\n"
		+ "		\"enabled\": \"bit(1)\",\r\n"
		+ "		\"sorted\": \"int(11)\",\r\n"
		+ "		\"password\": \"varchar(128)\"\r\n"
		+ "	},\r\n"
		+ "	\"old\": [{\r\n"
		+ "		\"create_date\": \"2022-06-19 08:58:40.000000\",\r\n"
		+ "		\"modify_date\": \"2022-06-19 08:58:40.000000\"\r\n"
		+ "	}],\r\n"
		+ "	\"pkNames\": [\"id\"],\r\n"
		+ "	\"sql\": \"\",\r\n"
		+ "	\"sqlType\": {\r\n"
		+ "		\"id\": -5,\r\n"
		+ "		\"create_date\": 93,\r\n"
		+ "		\"modify_date\": 93,\r\n"
		+ "		\"memo\": 12,\r\n"
		+ "		\"name\": 12,\r\n"
		+ "		\"enabled\": -7,\r\n"
		+ "		\"sorted\": 4,\r\n"
		+ "		\"password\": 12\r\n"
		+ "	},\r\n"
		+ "	\"table\": \"t_user\",\r\n"
		+ "	\"ts\": 1655600359868,\r\n"
		+ "	\"type\": \"UPDATE\"\r\n"
		+ "}";
	
}
