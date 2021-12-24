package com.acgist.data.config;

import java.util.Date;
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
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.StringUtils;
import com.acgist.data.query.TemplateQuery;
import com.acgist.data.query.TemplateQuery.Condition;

/**
 * 模板查询逻辑
 * 
 * @author acgist
 * 
 * TODO：全部消息
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
        final Class<?> returnType = signature.getReturnType();
        final Class<?>[] argsTypes = signature.getParameterTypes();
        final boolean fallback = templateQuery.fallback();
        final boolean listable = List.class.isAssignableFrom(returnType);
        final boolean pageable = Pageable.class.isAssignableFrom(returnType);
        final boolean simple = !(listable || pageable);
        final boolean voidType = Void.TYPE.equals(returnType);
        final boolean selectQuery = this.selectQuery(templateQuery);
        final Class<?> resultType = simple ? returnType : templateQuery.clazz();
        final int parameterLength = pageable ? argsLength - 1 : argsLength;
        final Pageable pageableParamter = pageable ? (Pageable) args[parameterLength] : null;
        final Map<String, Object> paramterMap = this.buildParamterMap(args, argsNames, argsTypes, parameterLength);
        final String whereString = this.buildWhere(paramterMap, templateQuery);
        final String queryString = this.buildQuery(templateQuery, whereString, pageableParamter);
        final Query query = this.createQuery(templateQuery, queryString);
        this.buildParamter(query, queryString, paramterMap);
        this.buildResultType(query, voidType, resultType);
        if(selectQuery) {
            Object result = null;
            if(pageableParamter != null) {
                query.setFirstResult((int) pageableParamter.getOffset());
                query.setMaxResults(pageableParamter.getPageSize());
            }
            final List<?> list = query.getResultList();
            if(CollectionUtils.isEmpty(list) && fallback) {
                result = proceedingJoinPoint.proceed(args);
            } else if (listable) {
                result = list;
            } else if (pageable) {
                final String countQueryString = this.buildCountQuery(templateQuery, whereString);
                final Query countQuery = this.createQuery(templateQuery, countQueryString);
                this.buildParamter(countQuery, countQueryString, paramterMap);
                final Long count = (Long) countQuery.getSingleResult();
                result = new PageImpl(list, (Pageable) args[parameterLength], count);
            } else {
                result = CollectionUtils.isEmpty(list) ? null : list.get(0);
            }
            if(voidType) {
                return null;
            } else {
                return result;
            }
        } else {
            final int result = query.executeUpdate();
            if(voidType) {
                return null;
            } else {
                return result;
            }
        }
    }
    
    /**
     * 判断查询语句
     * 
     * @param templateQuery 模板语句
     * 
     * @return 是否查询语句
     */
    private boolean selectQuery(TemplateQuery templateQuery) {
        final String query = templateQuery.query().strip();
        return query.startsWith(TemplateQuery.SELECT);
    }
    
    /**
     * 创建参数
     * 
     * @param args 参数
     * @param argsNames 参数名称
     * @param argsTypes 参数类型
     * @param parameterLength 参数长度
     * 
     * @return 参数
     */
    private Map<String, Object> buildParamterMap(Object[] args, String[] argsNames, Class<?>[] argsTypes, int parameterLength) {
        Object object;
        final Map<String, Object> paramterMap = new HashMap<>();
        for (int index = 0; index < parameterLength; index++) {
            object = args[index];
            // TODO：JDK17
            if (object instanceof Map) {
                paramterMap.putAll((Map<String, Object>) object);
            } else if (object instanceof String || object instanceof Number || object instanceof Date) {
                paramterMap.put(argsNames[index], args[index]);
            } else if (object != null) {
                BeanMap.create(object).forEach((key, value) -> paramterMap.put((String) key, value));
            }
        }
        return paramterMap;
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
        final String where = templateQuery.where();
        if (StringUtils.isEmpty(where)) {
            return where;
        }
        final String[] lines = where.split(TemplateQuery.LINE);
        final String whereQuery = Stream.of(lines)
            .map(line -> line.strip())
            .map(line -> this.buildWhereCondition(paramterMap, line))
            .filter(line -> line != null)
            .collect(Collectors.joining(TemplateQuery.SPACE));
        if(whereQuery.startsWith(TemplateQuery.OR)) {
            return whereQuery.substring(TemplateQuery.OR.length());
        } else if(whereQuery.startsWith(TemplateQuery.AND)) {
            return whereQuery.substring(TemplateQuery.AND.length());
        } else {
            return whereQuery;
        }
    }

    /**
     * 创建查询语句
     * 
     * @param templateQuery 模板语句
     * @param where 条件语句
     * @param pageableParamter 分页信息
     * 
     * @return 查询语句
     */
    private String buildQuery(TemplateQuery templateQuery, String where, Pageable pageableParamter) {
        final StringBuilder builder = new StringBuilder();
        builder.append(templateQuery.query()).append(TemplateQuery.SPACE);
        if (StringUtils.isNotEmpty(where)) {
            builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
            builder.append(where).append(TemplateQuery.SPACE);
        }
        builder.append(templateQuery.sorted()).append(TemplateQuery.SPACE);
        if(pageableParamter != null) {
            final String sorted = templateQuery.sorted();
            if(StringUtils.isNotEmpty(sorted)) {
                pageableParamter.getSort().get().forEach(order -> {
                    builder.append(order.getProperty()).append(TemplateQuery.SPACE)
                    .append(order.getDirection()).append(TemplateQuery.COMMA).append(TemplateQuery.SPACE);
                });
            } else {
                builder.append("order by").append(TemplateQuery.SPACE);
                pageableParamter.getSort().get().forEach(order -> {
                    builder.append(order.getProperty()).append(TemplateQuery.SPACE)
                        .append(order.getDirection()).append(TemplateQuery.COMMA).append(TemplateQuery.SPACE);
                });
            }
        }
        builder.append(templateQuery.attach());
        return builder.toString();
    }
    
    /**
     * 创建统计语句
     * 
     * @param templateQuery 模板语句
     * @param where 条件语句
     * 
     * @return 统计语句
     */
    private String buildCountQuery(TemplateQuery templateQuery, String where) {
        final StringBuilder builder = new StringBuilder();
        builder.append(templateQuery.count()).append(TemplateQuery.SPACE);
        if (StringUtils.isNotEmpty(where)) {
            builder.append(TemplateQuery.WHERE).append(TemplateQuery.SPACE);
            builder.append(where).append(TemplateQuery.SPACE);
        }
        // 统计不要排序语句
        builder.append(templateQuery.attach());
        return builder.toString();
    }
    
    /**
     * 过滤查询参数
     * 
     * @param paramterMap 参数类型
     * @param query 查询语句
     * 
     * @return 查询参数
     */
    private Map<String, Object> filterParamterMap(Map<String, Object> paramterMap, String query) {
        final StringTokenizer tokenizer = new StringTokenizer(query);
        tokenizer.nextToken(TemplateQuery.COLON);
        String token;
        final Map<String, Object> map = new HashMap<>();
        while(tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            final int commaIndex = token.indexOf(TemplateQuery.COMMA);
            if(commaIndex > 0) {
                final String key = token.substring(0, commaIndex);
                map.put(key, paramterMap.get(key));
                continue;
            }
            final int rightIndex = token.indexOf(TemplateQuery.RIGHT);
            if(rightIndex > 0) {
                final String key = token.substring(0, rightIndex);
                map.put(key, paramterMap.get(key));
                continue;
            }
            final int spaceIndex = token.indexOf(TemplateQuery.SPACE);
            if(spaceIndex > 0) {
                final String key = token.substring(0, spaceIndex);
                map.put(key, paramterMap.get(key));
                continue;
            }
            final String key = token;
            map.put(key, paramterMap.get(key));
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
     * @param queryString 查询语句
     * @param paramterMap 参数
     */
    private void buildParamter(Query query, String queryString, Map<String, Object> paramterMap) {
        this.filterParamterMap(paramterMap, queryString).forEach(query::setParameter);
    }
    
    /**
     * 设置返回类型
     * 
     * @param query 语句
     * @param voidType 是否返回
     * @param resultType 返回类型
     */
    private void buildResultType(Query query, boolean voidType, Class<?> resultType) {
        if(!voidType) {
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
            final String query = line.substring(right + 1).strip();
            final String conditionQuery = line.substring(left + 1, right);
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
        return false;
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

}
