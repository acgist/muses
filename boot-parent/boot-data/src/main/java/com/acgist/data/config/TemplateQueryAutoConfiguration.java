package com.acgist.data.config;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.transform.Transformers;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.MessageCodeException;
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
@ConditionalOnBean(DataSource.class)
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
		final String queryString = TemplateQueryUtils.buildQuery(templateQuery, sourceQueryString, whereString, pageableParamter);
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
	 * @param queryString 查询语句
	 * 
	 * @return 执行语句
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
	@SuppressWarnings("deprecation")
	private void buildResultType(Query query, boolean voidable, Class<?> resultType) {
		if(voidable) {
			return;
		}
		if(resultType == null) {
			throw MessageCodeException.of("没有设置返回类型或者集合类型（TemplateQuery#resultType）");
		}
		// TODO：过时
		query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(resultType));
	}

	/**
	 * 执行语句
	 * 
	 * @param selectQuery 是否查询
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				// TODO：泛型
				result = new PageImpl(list, pageableParamter, this.executeCount(templateQuery, whereString, paramterMap));
			} else {
				return CollectionUtils.getFirst(list);
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
		final String countQueryString = TemplateQueryUtils.buildCountQuery(templateQuery, whereString).strip();
		if(StringUtils.isEmpty(countQueryString)) {
			throw MessageCodeException.of("没有统计查询语句");
		}
		final Map<String, Object> countParamterMap = TemplateQueryUtils.filterParamterMap(countQueryString, paramterMap);
		final Query countQuery = this.createQuery(templateQuery, countQueryString);
		this.buildParamter(countQuery, countParamterMap);
		final BigInteger count = (BigInteger) countQuery.getSingleResult();
		return count.longValue();
	}
	
}
