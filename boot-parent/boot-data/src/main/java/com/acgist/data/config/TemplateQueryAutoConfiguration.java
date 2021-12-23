package com.acgist.data.config;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cglib.beans.BeanMap;
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
 * 参数顺序：(查询参数)*(分页参数)?(结果类型)[1]
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
     * List(paramter...)
     * Result(paramter...)
     * Page(paramter..., Pageable)
     * 
     * @param proceedingJoinPoint 切点
     * 
     * @return 返回
     * 
     * @throws Throwable 异常
     */
    @Around("query()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final TemplateQuery templateQuery = signature.getMethod().getAnnotation(TemplateQuery.class);
        final Object[] args = proceedingJoinPoint.getArgs();
        final int argsLength = args.length;
        final String[] argsNames = signature.getParameterNames();
        final Class<?>[] argsTypes = signature.getParameterTypes();
        final Class<?> returnType = signature.getReturnType();
        final boolean listable = List.class.isAssignableFrom(returnType);
        final boolean pageable = Pageable.class.isAssignableFrom(returnType);
        final boolean simple = !(listable || pageable);
        final Class<?> resultType = simple ? returnType : templateQuery.clazz();
        final int parameterLength = pageable ? argsLength - 1 : argsLength;
        final Map<String, Object> paramterMap = this.buildParamterMap(args, argsNames, argsTypes, parameterLength);
        final String whereString = this.buildWhere(paramterMap, templateQuery);
        final String queryString = this.buildQuery(templateQuery, whereString);
        final Query query = this.createQuery(templateQuery, queryString);
        this.buildParamter(query, paramterMap);
        this.buildResultType(query, resultType);
        final List<?> list = query.getResultList();
        if (listable) {
            return list;
        }
        if (pageable) {
            final String countQueryString = this.buildCountQuery(templateQuery, whereString);
            final Query countQuery = this.createQuery(templateQuery, countQueryString);
            final Long count = (Long) countQuery.getSingleResult();
            return new PageImpl(list, (Pageable) args[parameterLength], count);
        }
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    private void buildResultType(Query query, Class<?> resultType) {
        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(resultType));
    }
    
    private Map<String, Object> buildParamterMap(Object[] args, String[] argsNames, Class<?>[] argsTypes, int parameterLength) {
        Object object;
        final Map<String, Object> paramterMap = new HashMap<>();
        for (int index = 0; index < parameterLength; index++) {
            object = args[index];
            // TODO：JDK17
            if(object instanceof Map) {
                paramterMap.putAll((Map<String, Object>) object);
            } else if(object instanceof String || object instanceof Number || object instanceof Date) {
                paramterMap.put(argsNames[index], args[index]);
            } else {
                BeanMap.create(object).forEach((key, value) -> paramterMap.put((String) key, value));
            }
        }
        return paramterMap;
    }
    
    private void buildParamter(Query query, Map<String, Object> paramterMap) {
        paramterMap.forEach((key, value) -> query.setParameter(key, value));
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
        if(StringUtils.isNotEmpty(where)) {
        	builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
        	builder.append(where).append(TemplateQuery.SPACE);
        }
        builder.append(templateQuery.sorted()).append(TemplateQuery.SPACE);
        builder.append(templateQuery.attach());
        return builder.toString();
    }

    private String buildCountQuery(TemplateQuery templateQuery, String where) {
        final StringBuilder builder = new StringBuilder();
        builder.append(templateQuery.count()).append(TemplateQuery.SPACE);
        if(StringUtils.isNotEmpty(where)) {
        	builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
        	builder.append(where).append(TemplateQuery.SPACE);
        }
        builder.append(templateQuery.attach());
        return builder.toString();
    }

    private String buildWhere(Map<String, Object> paramterMap, TemplateQuery templateQuery) {
        final String where = templateQuery.where();
        if (StringUtils.isEmpty(where)) {
            return where;
        }
        final String[] lines = where.split(TemplateQuery.LINE);
        return Stream.of(lines)
            .map(line -> line.strip())
            .map(line -> this.buildIf(paramterMap, line))
            .filter(line -> line != null)
            .collect(Collectors.joining(TemplateQuery.SPACE));
    }

    private String buildIf(Map<String, Object> paramterMap, String line) {
        if(line.indexOf('$') == 0) {
            final int left = line.indexOf(TemplateQuery.LEFT);
            final int right = line.indexOf(TemplateQuery.RIGHT);
            // TODO：where判断有没条件
            String query = line.substring(right + 1).trim();
            String conditionQuery = line.substring(left + 1, right);
            if(this.condition(paramterMap, conditionQuery)) {
            	return query;
            } else {
            	return null;
            }
        } else {
        	return line;
        }
    }

	    // 注意顺序
    public enum Condition {
        
        EQ("=="),
        NE("!="),
        LE("<="),
        LT("<"),
        GE(">="),
        GT(">");
        
        private final String symbol;
        
        private Condition(String symbol) {
            this.symbol = symbol;
        }
        
    }
    
    private boolean condition(Map<String, Object> map, String conditionQuery) {
        Condition[] values = Condition.values();
        for (Condition condition : values) {
            int index = conditionQuery.indexOf(condition.symbol);
            if(index < 0) {
                continue;
            }
            String left = conditionQuery.substring(0, index).trim();
            String right = conditionQuery.substring(index + condition.symbol.length() + 1).trim();
            final Object value = map.get(left);
            int result;
            switch (condition) {
            case EQ:
                return String.valueOf(value).equals(right);
            case NE:
                return !String.valueOf(value).equals(right);
            case LE:
                result = this.compare(value, right, condition);
                return result == -1 || result == 0;
            case LT:
                result = this.compare(value, right, condition);
                return result == -1;
            case GE:
                result = this.compare(value, right, condition);
                return result == 1 || result == 0;
            case GT:
                result = this.compare(value, right, condition);
                return result == 1;
            default:
                return false;
            }
        }
        return false;
    }
    
    private int compare(Object source, String target, Condition condition) {
        int result = 0;
        if(source instanceof Number) {
            result = Long.valueOf(source.toString()).compareTo(Long.valueOf(target));
        } else {
            result = String.valueOf(source).compareTo(target);
        }
        return result;
    }

}
