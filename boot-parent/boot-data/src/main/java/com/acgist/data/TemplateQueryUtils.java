package com.acgist.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cglib.beans.BeanMap;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.StringUtils;
import com.acgist.data.query.TemplateQuery;
import com.acgist.data.query.TemplateQuery.Condition;

/**
 * 查询语句工具
 * 
 * @author acgist
 */
public final class TemplateQueryUtils {

	private TemplateQueryUtils() {
	}
	
	/**
	 * 判断是否查询语句
	 * 
	 * @param queryString 语句
	 * 
	 * @return 是否查询语句
	 */
	public static final boolean selectQuery(String queryString) {
		return StringUtils.startsWidthIgnoreCase(queryString, TemplateQuery.QUERY_SELECT);
	}
	
	/**
	 * 创建参数
	 * 
	 * @param args 参数
	 * @param argsNames 参数名称
	 * @param parameterLength 参数长度
	 * 
	 * @return 参数
	 */
	public static final Map<String, Object> buildParamterMap(Object[] args, String[] argsNames, int parameterLength) {
		// TODO：JDK17
		Object object;
		final Map<String, Object> paramterMap = new HashMap<>();
		for (int index = 0; index < parameterLength; index++) {
			object = args[index];
			if (object instanceof Map) {
				paramterMap.putAll((Map<String, Object>) object);
			} else if (object instanceof Boolean || object instanceof String || object instanceof Number || object instanceof Date) {
				paramterMap.put(argsNames[index], args[index]);
			} else if (object != null) {
				BeanMap.create(object).forEach((key, value) -> paramterMap.put((String) key, value));
			}
		}
		return paramterMap;
	}
	
	/**
	 * 过滤参数
	 * 
	 * @param queryString 语句
	 * @param paramterMap 参数
	 * 
	 * @return 必要参数
	 */
	public static final Map<String, Object> filterParamterMap(String queryString, Map<String, Object> paramterMap) {
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
	 * 创建Where语句
	 * 
	 * @param where 原始Where语句
	 * @param paramterMap 参数
	 * 
	 * @return 目标Where语句
	 */
	public static final String buildWhere(String where, Map<String, Object> paramterMap) {
		if (StringUtils.isEmpty(where)) {
			return where;
		}
		final String[] lines = where.split(TemplateQuery.LINE);
		final String whereQuery = Stream.of(lines)
			.map(line -> line.strip())
			.filter(line -> StringUtils.isNotEmpty(line))
			.map(line -> buildWhereLine(paramterMap, line))
			.filter(line -> StringUtils.isNotEmpty(line))
			.collect(Collectors.joining(TemplateQuery.SPACE));
		if(StringUtils.startsWidthIgnoreCase(whereQuery, TemplateQuery.QUERY_OR)) {
			return whereQuery.substring(TemplateQuery.QUERY_OR_LENGHT);
		} else if(StringUtils.startsWidthIgnoreCase(whereQuery, TemplateQuery.QUERY_AND)) {
			return whereQuery.substring(TemplateQuery.QUERY_AND_LENGHT);
		} else {
			return whereQuery;
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
	public static final String buildWhereLine(Map<String, Object> paramterMap, String line) {
		if (line.indexOf(TemplateQuery.IF) == 0) {
			final int left = line.indexOf(TemplateQuery.LEFT);
			final int right = line.indexOf(TemplateQuery.RIGHT);
			if(left < 0 || right < 0) {
				throw MessageCodeException.of("条件异常：", line);
			}
			final String query = line.substring(right + 1).strip();
			final String conditionQuery = line.substring(left + 1, right).strip();
			final String[] orConditionQueries = conditionQuery.split(TemplateQuery.CONDITION_OR);
			if(orConditionQueries.length > 1) {
				boolean success = false;
				for (String orConditionQuery : orConditionQueries) {
					success = condition(paramterMap, orConditionQuery);
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
					success = condition(paramterMap, andConditionQuery);
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
			if (condition(paramterMap, conditionQuery)) {
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
	public static final boolean condition(Map<String, Object> paramterMap, String conditionQuery) {
		int index;
		int result;
		String left;
		String right;
		String symbol;
		Object object;
		conditionQuery = conditionQuery.strip();
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
				result = compare(object, right);
				return result == -1 || result == 0;
			case LT:
				result = compare(object, right);
				return result == -1;
			case GE:
				result = compare(object, right);
				return result == 1 || result == 0;
			case GT:
				result = compare(object, right);
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
	public static final int compare(Object source, String target) {
		int result = 0;
		if(source == target) {
			return result;
		} else if (source instanceof Number) {
			return Long.compare(((Number) source).intValue(), Integer.valueOf(target));
		} else if(target != null) {
			result = String.valueOf(source).compareTo(target);
		}
		return result;
	}
	
}
