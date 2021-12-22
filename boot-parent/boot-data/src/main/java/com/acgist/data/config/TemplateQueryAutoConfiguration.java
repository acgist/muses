package com.acgist.data.config;

import java.lang.reflect.ParameterizedType;
import java.util.List;
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
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.StringUtils;
import com.acgist.data.query.TemplateQuery;

/**
 * 模板查询逻辑
 * 
 * @author acgist
 */
@Aspect
@Order(Integer.MIN_VALUE)
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
		final Object[] args = proceedingJoinPoint.getArgs();
		final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		final TemplateQuery templateQuery = signature.getMethod().getAnnotation(TemplateQuery.class);
		final String[] argsNames = signature.getParameterNames();
		final Class<?>[] argsTypes = signature.getParameterTypes();
		final int length = argsNames.length;
		// 是否分页
		final Class returnType = signature.getReturnType();
		final boolean pageable = Pageable.class.isAssignableFrom(returnType);
		final boolean listable = List.class.isAssignableFrom(returnType);
		final String whereString = this.buildWhere(templateQuery);
		final String queryString = this.buildQuery(templateQuery, whereString);
		final Query query = this.createQuery(templateQuery, queryString);
		final Class resultType = this.buildResultType(pageable, listable, returnType);
		for (String name : argsNames) {
			query.setParameter(name, args[0]);
		}
//		query.unwrap(org.hibernate.query.NativeQuery.class).setResultTransformer(Transformers.aliasToBean(resultType));
		query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(resultType));
		final List list = query.getResultList();
		if (pageable) {
			final String countQueryString = this.buildQuery(templateQuery, whereString);
			final Query countQuery = this.createQuery(templateQuery, countQueryString);
			final Long count = (Long) countQuery.getSingleResult();
			return new PageImpl(list, (Pageable) args[length - 1], count);
		}
		if (listable) {
			return list;
		}
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	private Class buildResultType(boolean pageable, boolean listable, Class returnType) {
		if (pageable) {
			ParameterizedType type = (ParameterizedType) returnType.getGenericSuperclass();
			System.out.println(type.getActualTypeArguments()[0]);
		}
		if (listable) {

		}
		return returnType;
	}

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

	private String buildQuery(TemplateQuery templateQuery, String where) {
		final StringBuilder builder = new StringBuilder();
		builder.append(templateQuery.select()).append(TemplateQuery.SPACE);
		builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
		builder.append(where).append(TemplateQuery.SPACE);
		builder.append(templateQuery.sorted()).append(TemplateQuery.SPACE);
		builder.append(templateQuery.attach());
		return builder.toString();
	}

	private String buildWhere(TemplateQuery templateQuery) {
		final String where = templateQuery.where();
		if (StringUtils.isEmpty(where)) {
			return where;
		}
		final String[] lines = where.split(TemplateQuery.LINE);
		return Stream.of(lines).map(line -> line.strip()).map(this::buildIf)
			.collect(Collectors.joining(TemplateQuery.SPACE));
	}

	private String buildIf(String line) {
		if (line.indexOf(TemplateQuery.IF) == 0) {
			return line;
		} else {
			return line;
		}
	}

}
