package com.acgist.data.config;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.StringUtils;
import com.acgist.data.TemplateQueryUtils;
import com.acgist.data.query.TemplateQuery;

/**
 * 模板查询逻辑
 * 
 * @author acgist
 * 
 * TODO：优化字符串拼接
 */
@Aspect
@Configuration
@ConditionalOnClass(JpaRepositoriesAutoConfiguration.class)
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
public class TemplateQueryAutoConfiguration {

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
		final String sourceQueryString = templateQuery.query().strip();
		final String sourceWhereString = templateQuery.where().strip();
		final Object[] args = proceedingJoinPoint.getArgs();
		final String[] argsNames = methodSignature.getParameterNames();
		final Class<?> returnType = methodSignature.getReturnType();
		final boolean fallback = templateQuery.fallback();
		final boolean voidable = Void.TYPE.equals(returnType);
		final boolean listable = List.class.isAssignableFrom(returnType);
		final boolean pageable = Page.class.isAssignableFrom(returnType);
		final boolean selectQuery = TemplateQueryUtils.selectQuery(sourceQueryString);
		final Class<?> resultType = (!voidable && !listable && !pageable) ? returnType : templateQuery.resultType();
		final int argsLength = args.length;
		final int parameterLength = pageable ? argsLength - 1 : argsLength;
		final Pageable pageableParamter = pageable ? (Pageable) args[parameterLength] : null;
		// 设置参数
		final Map<String, Object> paramterMap = TemplateQueryUtils.buildParamterMap(args, argsNames, parameterLength);
		final String whereString = TemplateQueryUtils.buildWhere(sourceWhereString, paramterMap);
		final String queryString = this.buildQuery(templateQuery, sourceQueryString, whereString, pageableParamter);
		final Map<String, Object> queryParamterMap = TemplateQueryUtils.filterParamterMap(queryString, paramterMap);
		final Query query = this.createQuery(templateQuery, queryString);
		this.buildParamter(query, pageableParamter, queryParamterMap);
		this.buildResultType(query, voidable, resultType);
		// 执行查询
		final Object result = this.execute(selectQuery, listable, pageable, query, templateQuery, whereString, pageableParamter, paramterMap);
		if(voidable) {
			return null;
		}
		if(result == null && fallback) {
			return proceedingJoinPoint.proceed(args);
		}
		return result;
	}

	/**
	 * 创建执行语句
	 * 
	 * @param templateQuery 模板语句
	 * @param queryString 原始语句
	 * @param whereString 条件语句
	 * @param pageableParamter 分页信息
	 * 
	 * @return 执行语句
	 */
	private String buildQuery(TemplateQuery templateQuery, String queryString, String whereString, Pageable pageableParamter) {
		final StringBuilder builder = new StringBuilder();
		builder.append(queryString).append(TemplateQuery.SPACE);
		if (StringUtils.isNotEmpty(whereString)) {
			builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
			builder.append(whereString).append(TemplateQuery.SPACE);
		}
		final String sorted = this.buildSortedQuery(templateQuery, pageableParamter).strip();
		if(StringUtils.isNotEmpty(sorted)) {
			builder.append(sorted).append(TemplateQuery.SPACE);
		}
		final String attach = templateQuery.attach().strip();
		if(StringUtils.isNotEmpty(attach)) {
			builder.append(attach).append(TemplateQuery.SPACE);
		}
		return builder.toString();
	}
	
	/**
	 * 创建排序语句
	 * 
	 * @param templateQuery 模板语句
	 * @param pageableParamter 分页信息
	 * 
	 * @return 排序语句
	 */
	private String buildSortedQuery(TemplateQuery templateQuery, Pageable pageableParamter) {
		final StringBuilder builder = new StringBuilder();
		final String sorted = templateQuery.sorted().strip();
		final boolean empty = StringUtils.isEmpty(sorted);
		if(!empty) {
			builder.append(sorted);
		}
		if(pageableParamter != null) {
			final Stream<Order> stream = pageableParamter.getSort().get();
			final String orderString = stream
				.map(order -> order.getProperty() + TemplateQuery.SPACE + order.getDirection())
				.collect(Collectors.joining(TemplateQuery.COMMA_SPACE));
			if(StringUtils.isEmpty(orderString)) {
				builder.append(TemplateQuery.SPACE);
			} else {
				if(empty) {
					builder.append(TemplateQuery.ORDER_BY).append(TemplateQuery.SPACE);
				} else {
					builder.append(TemplateQuery.COMMA).append(TemplateQuery.SPACE);
				}
				builder.append(orderString).append(TemplateQuery.SPACE);
			}
		} else {
			builder.append(TemplateQuery.SPACE);
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
	 * @param query 执行语句
	 * @param paramterMap 参数
	 */
	private void buildParamter(Query query, Map<String, Object> paramterMap) {
		this.buildParamter(query, null, paramterMap);
	}
	
	/**
	 * 设置参数
	 * 
	 * @param query 执行语句
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
	 * @param query 执行语句
	 * @param voidable 是否返回
	 * @param resultType 返回类型
	 */
	private void buildResultType(Query query, boolean voidable, Class<?> resultType) {
		if(!voidable) {
			query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(resultType));
		}
	}

	/**
	 * 执行语句
	 * 
	 * @param selectQuery 是否查询
	 * @param voidable 是否返回
	 * @param listable 是否列表
	 * @param pageable 是否分页
	 * @param query 执行语句
	 * @param templateQuery 模板语句
	 * @param whereString 条件语句
	 * @param pageableParamter 分页信息
	 * @param paramterMap 参数
	 * 
	 * @return 执行结果
	 */
	private Object execute(
		boolean selectQuery, boolean listable, boolean pageable,
		Query query, TemplateQuery templateQuery, String whereString,
		Pageable pageableParamter, Map<String, Object> paramterMap
	) {
		if(selectQuery) {
			Object result = null;
			final List<?> list = query.getResultList();
			if (listable) {
				result = list;
			} else if (pageable) {
				result = new PageImpl(list, pageableParamter, this.executeCount(templateQuery, whereString, paramterMap));
			} else {
				if(CollectionUtils.isEmpty(list)) {
					result = null;
				} else {
					result = list.get(0);
				}
			}
			return result;
		} else {
			return query.executeUpdate();
		}
	}
	
	/**
	 * 执行统计语句
	 * 
	 * @param templateQuery 模板语句
	 * @param whereString 条件语句
	 * @param paramterMap 参数
	 * 
	 * @return 统计数量
	 */
	private long executeCount(TemplateQuery templateQuery, String whereString, Map<String, Object> paramterMap) {
		final String countQueryString = this.buildCountQuery(templateQuery, whereString).strip();
		if(StringUtils.isEmpty(countQueryString)) {
			throw new IllegalArgumentException("没有统计查询语句");
		}
		final Map<String, Object> countParamterMap = TemplateQueryUtils.filterParamterMap(countQueryString, paramterMap);
		final Query countQuery = this.createQuery(templateQuery, countQueryString);
		this.buildParamter(countQuery, countParamterMap);
		final BigInteger count = (BigInteger) countQuery.getSingleResult();
		return count.longValue();
	}
	
}
