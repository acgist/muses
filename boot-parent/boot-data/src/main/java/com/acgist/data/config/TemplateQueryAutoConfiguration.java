package com.acgist.data.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@ConditionalOnBean(value = EntityManager.class)
public class TemplateQueryAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateQueryAutoConfiguration.class);

    @Autowired
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
        final boolean pageable = Pageable.class.isAssignableFrom(argsTypes[length - 1]);
        final boolean listable = List.class.isAssignableFrom(signature.getReturnType());
        final String whereString = this.buildWhere(templateQuery);
        final String queryString = this.buildQuery(templateQuery, whereString);
        final String countQueryString = this.buildQuery(templateQuery, whereString);
        final Query query;
        final Query countQuery;
        if(templateQuery.nativeQuery()) {
            // TODO：resultclass参数
            query = this.entityManager.createNativeQuery(queryString);
            if(pageable) {
                countQuery = this.entityManager.createNativeQuery(countQueryString);
            } else {
                countQuery = null;
            }
        } else {
            query = this.entityManager.createQuery(queryString);
//            query.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(String.class));
            if(pageable) {
                countQuery = this.entityManager.createQuery(countQueryString);
            } else {
                countQuery = null;
            }
        }
        final List list = query.getResultList();
        if(pageable) {
            final Long count = (Long) countQuery.getSingleResult();
            return new PageImpl(list, (Pageable) args[length - 1], count);
        }
        if(listable) {
            return list;
        }
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
    
    private String buildQuery(TemplateQuery templateQuery, String where) {
        final StringBuilder builder = new StringBuilder();
        builder.append(templateQuery.select()).append(TemplateQuery.SPACE);
        builder.append(where).append(TemplateQuery.SPACE);
        builder.append(templateQuery.sorted()).append(TemplateQuery.SPACE);
        builder.append(templateQuery.attach());
        return builder.toString();
    }

    private String buildWhere(TemplateQuery templateQuery) {
        final String where = templateQuery.where();
        if(StringUtils.isEmpty(where)) {
            return where;
        }
        final String[] lines = where.split(TemplateQuery.LINE);
        return Stream.of(lines)
            .map(line -> line.strip())
            .map(this::buildIf)
            .collect(Collectors.joining(TemplateQuery.SPACE));
    }
    
    private String buildIf(String line) {
        if(line.indexOf(TemplateQuery.IF) == 0) {
            return line;
        } else {
            return line;
        }
    }

}
