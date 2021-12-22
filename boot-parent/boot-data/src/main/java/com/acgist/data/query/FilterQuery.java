package com.acgist.data.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.acgist.data.entity.DataEntity;

/**
 * 过滤查询
 * 
 * @author acgist
 */
public class FilterQuery<T extends DataEntity> {

    /**
     * 过滤条件
     * 
     * @author acgist
     */
    public static class Filter<V> {

        /**
         * 比较类型
         * 
         * @author acgist
         */
        public enum Type {
            // =
            EQ,
            // !=
            NE,
            // <
            LT,
            // >
            GT,
            // <=
            LE,
            // >=
            GE,
            // in
            IN,
            // like
            LIKE,
            // between
            BETWEEN,
            // is null
            IS_NULL,
            // is not null
            IS_NOT_NULL;
        }

        private final String name;;
        private final V value;
        private final Type type;

        public Filter(String name, V value, Type type) {
            this.name = name;
            this.value = value;
            this.type = type;
        }

        // TODO：JDK17
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public <T> Predicate predicate(Root<T> root, CriteriaBuilder builder) {
            switch (this.type) {
            case EQ:
                return builder.equal(root.get(this.name), this.value);
            case NE:
                return builder.notEqual(root.get(this.name), this.value);
            case LT:
                return builder.lessThan(root.get(this.name), (Comparable) this.value);
            case GT:
                return builder.greaterThan(root.get(this.name), (Comparable) this.value);
            case LE:
                return builder.lessThanOrEqualTo(root.get(this.name), (Comparable) this.value);
            case GE:
                return builder.greaterThanOrEqualTo(root.get(this.name), (Comparable) this.value);
            case IN:
                return root.get(this.name).in(this.value);
            case LIKE:
                return builder.like(root.get(this.name), (String) this.value);
            case BETWEEN:
                final List<?> list = (List<?>) this.value;
                return builder.between(root.get(this.name), (Comparable) list.get(0), (Comparable) list.get(1));
            case IS_NULL:
                return root.get(this.name).isNull();
            case IS_NOT_NULL:
                return root.get(this.name).isNotNull();
            default:
                throw new IllegalArgumentException("未知过滤类型：" + this.type);
            }
        }

    }

    /**
     * 排序
     * 
     * @author acgist
     */
    public static class Sorted {

        public enum Type {
            // 递增
            ASC,
            // 递减
            DESC;
        }

        private final String name;
        private final Type type;

        public Sorted(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public <T> Order order(Root<T> root, CriteriaBuilder builder) {
            switch (this.type) {
            case ASC:
                return builder.asc(root.get(this.name));
            case DESC:
                return builder.desc(root.get(this.name));
            default:
                throw new IllegalArgumentException("未知排序类型：" + this.type);
            }
        }

    }

    /**
     * 过滤条件
     */
    private List<Filter<Object>> filter = new ArrayList<>();
    /**
     * 排序条件
     */
    private List<Sorted> sorted = new ArrayList<>();
    /**
     * 是否可以为空
     */
    private boolean nullable = true;

    /**
     * 创建过滤查询
     * 
     * @param <T> 类型
     * 
     * @return 过滤查询
     */
    public static final <T extends DataEntity> FilterQuery<T> builder() {
        return new FilterQuery<T>();
    }

    public <V> FilterQuery<T> eq(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.EQ));
        return this;
    }

    public <V> FilterQuery<T> ne(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.NE));
        return this;
    }

    public <V extends Comparable<V>> FilterQuery<T> lt(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.LT));
        return this;
    }

    public <V extends Comparable<V>> FilterQuery<T> gt(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.GT));
        return this;
    }

    public <V extends Comparable<V>> FilterQuery<T> le(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.LE));
        return this;
    }

    public <I, V extends List<I>> FilterQuery<T> in(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.IN));
        return this;
    }

    /**
     * Like查询
     * 
     * @param name  名称
     * @param value 属性：需要自己设置匹配位置
     * 
     * @return 过滤查询
     */
    public FilterQuery<T> like(String name, String value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.LIKE));
        return this;
    }

    public <I extends Comparable<I>, V extends List<I>> FilterQuery<T> between(String name, V value) {
        this.filter.add(new Filter<Object>(name, value, Filter.Type.BETWEEN));
        return this;
    }

    public FilterQuery<T> isNull(String name) {
        this.filter.add(new Filter<Object>(name, null, Filter.Type.IS_NULL));
        return this;
    }

    public FilterQuery<T> isNotNull(String name) {
        this.filter.add(new Filter<Object>(name, null, Filter.Type.IS_NOT_NULL));
        return this;
    }

    public FilterQuery<T> asc(String name) {
        this.sorted.add(new Sorted(name, Sorted.Type.ASC));
        return this;
    }

    public FilterQuery<T> desc(String name) {
        this.sorted.add(new Sorted(name, Sorted.Type.DESC));
        return this;
    }

    public FilterQuery<T> nullable() {
        this.nullable = true;
        return this;
    }

    public FilterQuery<T> nullless() {
        this.nullable = false;
        return this;
    }

    /**
     * 创建查询语句
     * 
     * @return 查询语句
     */
    public Specification<T> build() {
        return new Specification<T>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate[] filters = FilterQuery.this.filter.stream()
                    .filter(filter -> FilterQuery.this.nullable || filter.value != null)
                    .map(filter -> filter.predicate(root, builder)).collect(Collectors.toList())
                    .toArray(Predicate[]::new);
                final Order[] orders = FilterQuery.this.sorted.stream().map(sorted -> sorted.order(root, builder))
                    .collect(Collectors.toList()).toArray(Order[]::new);
                return query.where(filters).orderBy(orders).getRestriction();
            }
        };
    }

}
