package com.acgist.rule;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.rule.condition.RuleCondition;
import com.acgist.rule.condition.impl.EqCondition;
import com.acgist.rule.condition.impl.ExcludeCondition;
import com.acgist.rule.condition.impl.GeCondition;
import com.acgist.rule.condition.impl.GtCondition;
import com.acgist.rule.condition.impl.InCondition;
import com.acgist.rule.condition.impl.IncludeCondition;
import com.acgist.rule.condition.impl.LeCondition;
import com.acgist.rule.condition.impl.LikeCondition;
import com.acgist.rule.condition.impl.LtCondition;
import com.acgist.rule.condition.impl.NeCondition;
import com.acgist.rule.condition.impl.NotInCondition;
import com.acgist.rule.condition.impl.NotLikeCondition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleConditionTest {

	@Test
	public void testRule() {
//		final String rule = "() && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
//		final String rule = "(queryId EQ 1 || queryId IN [1, 2]) && queryId INCLUDE [3, 4] && queryId EXCLUDE [1, 3]";
//		final String rule = "a EQ 2 && b EQ 2";
//		final String rule = "a EQ 2 && b == 2";
//		final String rule = "((a EQ 2 && b EQ 2) || c EQ 2) || (( d in [2, 0] && e EQ 2 && f EQ 2 ) && (g EQ 2)) || (h EQ 1 && i EQ 1) || ((j EQ 3 && k EQ 3))";
		final String rule = "((a EQ 2 && b EQ 2) || \r\n"
			+ "c EQ 2) || (( d in [2, 0] && \r\n"
			+ "\r\n"
			+ "e EQ 2 && f EQ 2 ) && (g EQ 2)) || \r\n"
			+ "(h EQ 1 && i EQ 1) || ((j \r\n"
			+ "EQ \r\n"
			+ "3 && k   EQ   3))";
		final RuleCondition root = RuleCondition.deserialization(rule);
		log.info(JSONUtils.toJSON(root));
		log.info(root.serialize());
//		log.info(root.serializeTransfer());
	}
	
	@Test
	public void testCondition() {
		final EqCondition eq = new EqCondition();
		assertTrue(eq.filter("1", Integer.class, 1));
		assertTrue(eq.filter("1", String.class, "1"));
		assertTrue(eq.filter("null", String.class, null));
		assertFalse(eq.filter("1", Integer.class, 12));
		assertFalse(eq.filter("1", String.class, "13"));
		assertFalse(eq.filter("null", String.class, "2"));
		final NeCondition ne = new NeCondition();
		assertFalse(ne.filter("1", Integer.class, 1));
		assertFalse(ne.filter("1", String.class, "1"));
		assertFalse(ne.filter("null", String.class, null));
		assertTrue(ne.filter("1", Integer.class, 12));
		assertTrue(ne.filter("1", String.class, "13"));
		assertTrue(ne.filter("null", String.class, "2"));
		final GeCondition ge = new GeCondition();
		assertTrue(ge.filter("1", String.class, "1"));
		assertTrue(ge.filter("11", Integer.class, 11));
		assertTrue(ge.filter("null", String.class, null));
		assertTrue(ge.filter("11", Integer.class, 12));
		assertTrue(ge.filter("1", String.class, "2"));
		assertFalse(ge.filter("12", Integer.class, 11));
		assertFalse(ge.filter("12", String.class, "11"));
		final LeCondition le = new LeCondition();
		assertTrue(le.filter("11", Integer.class, 11));
		assertTrue(le.filter("1", String.class, "1"));
		assertTrue(le.filter("null", String.class, null));
		assertFalse(le.filter("11", Integer.class, 12));
		assertFalse(le.filter("1", String.class, "2"));
		assertTrue(le.filter("12", Integer.class, 11));
		assertTrue(le.filter("12", String.class, "11"));
		final GtCondition gt = new GtCondition();
		assertFalse(gt.filter("11", Integer.class, 11));
		assertFalse(gt.filter("1", String.class, "1"));
		assertFalse(gt.filter("null", String.class, null));
		assertTrue(gt.filter("11", Integer.class, 12));
		assertTrue(gt.filter("1", String.class, "2"));
		assertFalse(gt.filter("12", Integer.class, 11));
		assertFalse(gt.filter("12", String.class, "11"));
		final LtCondition lt = new LtCondition();
		assertFalse(lt.filter("11", Integer.class, 11));
		assertFalse(lt.filter("1", String.class, "1"));
		assertFalse(lt.filter("null", String.class, null));
		assertFalse(lt.filter("11", Integer.class, 12));
		assertFalse(lt.filter("1", String.class, "2"));
		assertTrue(lt.filter("12", Integer.class, 11));
		assertTrue(lt.filter("12", String.class, "11"));
		final InCondition in = new InCondition();
		assertTrue(in.filter("[\"1\", \"2\"]", String.class, "1"));
		assertTrue(in.filter("[1, 2]", Integer.class, 1));
		assertFalse(in.filter("[\"1\", \"2\"]", String.class, "3"));
		assertFalse(in.filter("[1, 2]", Integer.class, 3));
		final NotInCondition notIn = new NotInCondition();
		assertFalse(notIn.filter("[\"1\", \"2\"]", String.class, "1"));
		assertFalse(notIn.filter("[1, 2]", Integer.class, 1));
		assertTrue(notIn.filter("[\"1\", \"2\"]", String.class, "3"));
		assertTrue(notIn.filter("[1, 2]", Integer.class, 3));
		final IncludeCondition include = new IncludeCondition();
		assertTrue(include.filter("[\"1\", \"2\"]", String.class, "123"));
		assertTrue(include.filter("[1, 2]", Integer.class, 123));
		assertFalse(include.filter("[\"1\", \"2\"]", String.class, "345"));
		assertFalse(include.filter("[1, 2]", Integer.class, 345));
		final ExcludeCondition exclude = new ExcludeCondition();
		assertFalse(exclude.filter("[\"1\", \"2\"]", String.class, "123"));
		assertFalse(exclude.filter("[1, 2]", Integer.class, 123));
		assertTrue(exclude.filter("[\"1\", \"2\"]", String.class, "345"));
		assertTrue(exclude.filter("[1, 2]", Integer.class, 345));
		final LikeCondition like = new LikeCondition();
		assertTrue(like.filter("12", String.class, "123"));
		assertTrue(like.filter("12", Integer.class, 123));
		assertFalse(like.filter("3456", String.class, "345"));
		assertFalse(like.filter("3456", Integer.class, 345));
		final NotLikeCondition notLike = new NotLikeCondition();
		assertFalse(notLike.filter("12", String.class, "123"));
		assertFalse(notLike.filter("12", Integer.class, 123));
		assertTrue(notLike.filter("3456", String.class, "345"));
		assertTrue(notLike.filter("3456", Integer.class, 345));
	}
	
}
