package com.acgist.data.config;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.MessageCodeException;
import com.acgist.boot.StringUtils;
import com.acgist.data.TemplateQueryUtils;
import com.acgist.data.query.TemplateQuery;
import com.acgist.data.query.TemplateQuery.Condition;

/**
 * 模板查询逻辑
 * 
 * @author acgist
 */
@Aspect
@Configuration
@ConditionalOnClass(JpaRepositoriesAutoConfiguration.class)
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
public class TemplateQueryAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateQueryAutoConfiguration.class);

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 注解切点
	 */
	@Pointcut("@annotation(com.acgist.data.query.TemplateQuery)")
	public void query() {
	}

	/**
	 * 环绕执行
	 * 
	 * @param proceedingJoinPoint 切点
	 * 
	 * @return 返回
	 * 
	 * @throws Throwable 异常
	 */
	@Around("query()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// 基本信息
		final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		final TemplateQuery templateQuery = methodSignature.getMethod().getAnnotation(TemplateQuery.class);
		final Object[] args = proceedingJoinPoint.getArgs();
		final String[] argsNames = methodSignature.getParameterNames();
		final Class<?> returnType = methodSignature.getReturnType();
		final boolean fallback = templateQuery.fallback();
		final boolean voidable = Void.TYPE.equals(returnType);
		final boolean listable = List.class.isAssignableFrom(returnType);
		final boolean pageable = Page.class.isAssignableFrom(returnType);
		final boolean selectQuery = this.selectQuery(templateQuery);
		final Class<?> resultType = (!voidable && !listable && !pageable) ? returnType : templateQuery.resultType();
		final int argsLength = args.length;
		final int parameterLength = pageable ? argsLength - 1 : argsLength;
		final Pageable pageableParamter = pageable ? (Pageable) args[parameterLength] : null;
		// 设置参数
		final Map<String, Object> paramterMap = TemplateQueryUtils.buildParamterMap(args, argsNames, parameterLength);
		final String whereString = this.buildWhere(paramterMap, templateQuery);
		final String queryString = this.buildQuery(templateQuery, whereString, pageableParamter);
		final Map<String, Object> queryParamterMap = this.filterParamterMap(queryString, paramterMap);
		final Query query = this.createQuery(templateQuery, queryString);
		this.buildParamter(query, pageableParamter, queryParamterMap);
		this.buildResultType(query, voidable, resultType);
		// 执行查询
		final Object result = this.execute(selectQuery, voidable, listable, pageable, query, templateQuery, whereString, pageableParamter, paramterMap);
		if(result == null && fallback) {
			return proceedingJoinPoint.proceed(args);
		}
		return result;
	}
	
	/**
	 * 判断查询语句
	 * 
	 * @param templateQuery 模板语句
	 * 
	 * @return 是否查询语句
	 */
	private boolean selectQuery(TemplateQuery templateQuery) {
		return StringUtils.startsWidthIgnoreCase(templateQuery.query().strip(), TemplateQuery.QUERY_SELECT);
	}
	
	/**
	 * 创建Where语句
	 * 
	 * @param paramterMap 参数
	 * @param templateQuery 模板查询
	 * 
	 * @return Where语句
	 */
	private String buildWhere(Map<String, Object> paramterMap, TemplateQuery templateQuery) {
		final String where = templateQuery.where().strip();
		if (StringUtils.isEmpty(where)) {
			return where;
		}
		final String[] lines = where.split(TemplateQuery.LINE);
		final String whereQuery = Stream.of(lines)
			.map(line -> line.strip())
			.filter(line -> StringUtils.isNotEmpty(line))
			.map(line -> this.buildWhereCondition(paramterMap, line))
			.filter(line -> StringUtils.isNotEmpty(line))
			.collect(Collectors.joining(TemplateQuery.SPACE));
		if(StringUtils.startsWidthIgnoreCase(whereQuery, TemplateQuery.QUERY_OR)) {
			return whereQuery.substring(TemplateQuery.QUERY_OR.length());
		} else if(StringUtils.startsWidthIgnoreCase(whereQuery, TemplateQuery.QUERY_AND)) {
			return whereQuery.substring(TemplateQuery.QUERY_AND.length());
		} else {
			return whereQuery;
		}
	}

	/**
	 * 创建查询语句
	 * 
	 * @param templateQuery 模板语句
	 * @param whereString 条件语句
	 * @param pageableParamter 分页信息
	 * 
	 * @return 查询语句
	 */
	private String buildQuery(TemplateQuery templateQuery, String whereString, Pageable pageableParamter) {
		final StringBuilder builder = new StringBuilder();
		builder.append(templateQuery.query().strip()).append(TemplateQuery.SPACE);
		if (StringUtils.isNotEmpty(whereString)) {
			builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
			builder.append(whereString).append(TemplateQuery.SPACE);
		}
		final String sorted = templateQuery.sorted().strip();
		if(StringUtils.isNotEmpty(sorted)) {
			builder.append(sorted);
		}
		if(pageableParamter != null) {
			final Stream<Order> stream = pageableParamter.getSort().get();
			final String orderString = stream
				.map(order -> order.getProperty() + TemplateQuery.SPACE + order.getDirection())
				.collect(Collectors.joining(TemplateQuery.COMMA + TemplateQuery.SPACE));
			if(StringUtils.isEmpty(orderString)) {
				builder.append(TemplateQuery.SPACE);
			} else {
				if(StringUtils.isEmpty(sorted)) {
					builder.append(TemplateQuery.ORDER_BY).append(TemplateQuery.SPACE);
				} else {
					builder.append(TemplateQuery.COMMA).append(TemplateQuery.SPACE);
				}
				builder.append(orderString).append(TemplateQuery.SPACE);
			}
		} else {
			builder.append(TemplateQuery.SPACE);
		}
		final String attach = templateQuery.attach().strip();
		if(StringUtils.isNotEmpty(attach)) {
			builder.append(attach).append(TemplateQuery.SPACE);
		}
		return builder.toString();
	}
	
	/**
	 * 创建统计语句
	 * 
	 * @param templateQuery 模板语句
	 * @param whereString 条件语句
	 * 
	 * @return 统计语句
	 */
	private String buildCountQuery(TemplateQuery templateQuery, String whereString) {
		final StringBuilder builder = new StringBuilder();
		builder.append(templateQuery.count().strip()).append(TemplateQuery.SPACE);
		if (StringUtils.isNotEmpty(whereString)) {
			builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
			builder.append(whereString).append(TemplateQuery.SPACE);
		}
		// 统计不要排序语句
		final String attach = templateQuery.attach().strip();
		if(StringUtils.isNotEmpty(attach)) {
			builder.append(attach).append(TemplateQuery.SPACE);
		}
		return builder.toString();
	}
	
	/**
	 * 过滤查询参数
	 * 
	 * @param queryString 查询语句
	 * @param paramterMap 参数类型
	 * 
	 * @return 查询参数
	 */
	private Map<String, Object> filterParamterMap(String queryString, Map<String, Object> paramterMap) {
		String token;
		final Map<String, Object> map = new HashMap<>();
		final StringTokenizer tokenizer = new StringTokenizer(queryString, TemplateQuery.COLON);
		// 丢弃首个
		if(tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
		}
		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			final int commaIndex = token.indexOf(TemplateQuery.COMMA);
			final int rightIndex = token.indexOf(TemplateQuery.RIGHT);
			final int spaceIndex = token.indexOf(TemplateQuery.SPACE);
			int minIndex = Integer.MAX_VALUE;
			if(commaIndex > 0) {
				minIndex = Math.min(minIndex, commaIndex);
			}
			if(rightIndex > 0) {
				minIndex = Math.min(minIndex, rightIndex);
			}
			if(spaceIndex > 0) {
				minIndex = Math.min(minIndex, spaceIndex);
			}
			if(minIndex > 0 && minIndex != Integer.MAX_VALUE) {
				final String key = token.substring(0, minIndex);
				map.put(key, paramterMap.get(key));
				continue;
			}
			map.put(token, paramterMap.get(token));
		}
		return map;
	}
	
	/**
	 * 创建查询语句
	 * 
	 * @param templateQuery 模板语句
	 * @param queryString 查询语句
	 * 
	 * @return 查询语句
	 */
	private Query createQuery(TemplateQuery templateQuery, String queryString) {
		final Query query;
		if (templateQuery.nativeQuery()) {
			query = this.entityManager.createNativeQuery(queryString);
		} else {
			query = this.entityManager.createQuery(queryString);
		}
		query.setFlushMode(FlushModeType.COMMIT);
		return query;
	}

	/**
	 * 设置参数
	 * 
	 * @param query 语句
	 * @param paramterMap 参数
	 */
	private void buildParamter(Query query, Map<String, Object> paramterMap) {
		this.buildParamter(query, null, paramterMap);
	}
	
	/**
	 * 设置参数
	 * 
	 * @param query 语句
	 * @param pageableParamter 分页信息
	 * @param paramterMap 参数
	 */
	private void buildParamter(Query query, Pageable pageableParamter, Map<String, Object> paramterMap) {
		paramterMap.forEach(query::setParameter);
		if(pageableParamter != null) {
			query.setFirstResult((int) pageableParamter.getOffset());
			query.setMaxResults(pageableParamter.getPageSize());
		}
	}
	
	/**
	 * 设置返回类型
	 * 
	 * @param query 语句
	 * @param voidable 是否返回
	 * @param resultType 返回类型
	 */
	private void buildResultType(Query query, boolean voidable, Class<?> resultType) {
		if(!voidable) {
			query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(resultType));
		}
	}

	/**
	 * 判断Where语句
	 * 
	 * @param paramterMap 参数
	 * @param line 原始Where语句
	 * 
	 * @return 目标Where语句
	 */
	private String buildWhereCondition(Map<String, Object> paramterMap, String line) {
		if (line.indexOf(TemplateQuery.IF) == 0) {
			final int left = line.indexOf(TemplateQuery.LEFT);
			final int right = line.indexOf(TemplateQuery.RIGHT);
			if(left < 0 || right < 0) {
				throw MessageCodeException.of("条件异常：", line);
			}
			final String query = line.substring(right + 1).strip();
			final String conditionQuery = line.substring(left + 1, right);
			final String[] orConditionQueries = conditionQuery.split(TemplateQuery.CONDITION_OR);
			if(orConditionQueries.length > 1) {
				boolean success = false;
				for (String orConditionQuery : orConditionQueries) {
					success = this.condition(paramterMap, orConditionQuery.strip());
					if (success) {
						break;
					}
				}
				if(success) {
					return query;
				} else {
					return null;
				}
			}
			final String[] andConditionQueries = conditionQuery.split(TemplateQuery.CONDITION_AND);
			if(andConditionQueries.length > 1) {
				boolean success = true;
				for (String andConditionQuery : andConditionQueries) {
					success = this.condition(paramterMap, andConditionQuery.strip());
					if (!success) {
						break;
					}
				}
				if(success) {
					return query;
				} else {
					return null;
				}
			}
			if (this.condition(paramterMap, conditionQuery)) {
				return query;
			} else {
				return null;
			}
		} else {
			return line;
		}
	}
	
	/**
	 * 判断条件
	 * 
	 * @param paramterMap 参数
	 * @param conditionQuery 条件语句
	 * 
	 * @return 是否匹配
	 */
	private boolean condition(Map<String, Object> paramterMap, String conditionQuery) {
		int index;
		int result;
		String left;
		String right;
		String symbol;
		Object object;
		final Condition[] values = Condition.values();
		for (Condition condition : values) {
			symbol = condition.symbol();
			index = conditionQuery.indexOf(symbol);
			if (index < 0) {
				continue;
			}
			left = conditionQuery.substring(0, index).strip();
			right = conditionQuery.substring(index + symbol.length() + 1).strip();
			object = paramterMap.get(left);
			switch (condition) {
			case EQ:
				return String.valueOf(object).equals(right);
			case NE:
				return !String.valueOf(object).equals(right);
			case LE:
				result = this.compare(object, right);
				return result == -1 || result == 0;
			case LT:
				result = this.compare(object, right);
				return result == -1;
			case GE:
				result = this.compare(object, right);
				return result == 1 || result == 0;
			case GT:
				result = this.compare(object, right);
				return result == 1;
			default:
				return false;
			}
		}
		return Boolean.TRUE.equals(paramterMap.get(conditionQuery));
	}

	/**
	 * 比较参数
	 * 
	 * @param source 原始参数
	 * @param target 目标参数
	 * 
	 * @return 比较结果
	 */
	private int compare(Object source, String target) {
		int result = 0;
		if (source instanceof Number) {
			result = Long.valueOf(source.toString()).compareTo(Long.valueOf(target));
		} else {
			result = String.valueOf(source).compareTo(target);
		}
		return result;
	}
	
	/**
	 * 执行查询
	 */
	private Object execute(
		boolean selectQuery, boolean voidable, boolean listable, boolean pageable,
		Query query, TemplateQuery templateQuery, String whereString,
		Pageable pageableParamter, Map<String, Object> paramterMap
	) {
		if(selectQuery) {
			Object result = null;
			final List<?> list = query.getResultList();
			if (listable) {
				result = list;
			} else if (pageable) {
				// 数据统计
				final String countQueryString = this.buildCountQuery(templateQuery, whereString);
				if(StringUtils.isEmpty(countQueryString.strip())) {
					throw new IllegalArgumentException("没有统计查询语句");
				}
				final Map<String, Object> countParamterMap = this.filterParamterMap(countQueryString, paramterMap);
				final Query countQuery = this.createQuery(templateQuery, countQueryString);
				this.buildParamter(countQuery, countParamterMap);
				final BigInteger count = (BigInteger) countQuery.getSingleResult();
				result = new PageImpl(list, pageableParamter, count.longValue());
			} else {
				if(CollectionUtils.isEmpty(list)) {
					result = null;
				} else {
					result = list.get(0);
				}
			}
			if(voidable) {
				return null;
			} else {
				return result;
			}
		} else {
			final int result = query.executeUpdate();
			if(voidable) {
				return null;
			} else {
				return result;
			}
		}
	}

}
