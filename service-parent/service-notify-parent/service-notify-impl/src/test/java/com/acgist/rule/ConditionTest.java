package com.acgist.rule;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.main.NotifyApplication;
import com.acgist.notify.gateway.model.entity.GatewayEntity;
import com.acgist.notify.rule.condition.RuleCondition;
import com.acgist.notify.rule.config.ConditionMappingFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = NotifyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConditionTest {
	
	@Autowired
	private ConditionMappingFactory conditionMappingFactory;
	
	@Test
	public void testWrapper() {
//		String rule = "() && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
//		String rule = "(queryId EQ 1 || queryId IN [1, 2]) && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
		String rule = "(queryId EQ 1 || queryId IN [1, 2] || (request LIKE 测试 && request NOT_LIKE 正式)) && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
//		String rule = ("queryId EQ 1 && queryId IN [1, 2] && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
		final RuleCondition condition = RuleCondition.deserialization(rule);
		final QueryWrapper<GatewayEntity> query = Wrappers.query();
		condition.buildWrapper(GatewayEntity.class, query, this.conditionMappingFactory);
		log.info(query.getTargetSql());
		log.info(query.getCustomSqlSegment());
	}
	
	@Test
	public void testFilter() {
		final String rule = "queryId EQ 1 && queryId IN [1, 2] && queryId INCLUDE [1, 3, 4] && queryId EXCLUDE [2, 3]";
		final RuleCondition condition = RuleCondition.deserialization(rule);
		final GatewayEntity gatewayEntity = new GatewayEntity();
		gatewayEntity.setQueryId(1L);
		assertTrue(condition.filter(gatewayEntity, this.conditionMappingFactory));
		gatewayEntity.setQueryId(2L);
		assertFalse(condition.filter(gatewayEntity, this.conditionMappingFactory));
		gatewayEntity.setQueryId(null);
		assertFalse(condition.filter(gatewayEntity, this.conditionMappingFactory));
	}
	
}
