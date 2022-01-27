package com.acgist.data.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.acgist.data.entity.DataEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 过滤查询
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public class FilterQuery<T extends DataEntity> {

	/**
	 * 过滤条件
	 * 
	 * @author acgist
	 */
    public static class Filter {

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
        	
        	public Filter of(String name, Object value) {
        		return new Filter(name, value, this);
        	}
        	
        }

        /**
         * 属性名称
         */
        private String name;
        /**
         * 属性内容
         */
        private Object value;
        /**
         * 比较类型
         */
        private Type type;

        public Filter() {
        }
        
        public Filter(String name, Object value, Type type) {
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
                return builder.like(root.get(this.name), "%" + this.value + "%");
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
        
        public <T> void predicate(Class<T> entity, QueryWrapper<T> wrapper) {
        	String column = this.name;
			try {
				final Field field = entity.getDeclaredField(this.name);
				final TableField annotation = field.getAnnotation(TableField.class);
				column = annotation.value();
			} catch (NoSuchFieldException | SecurityException e) {
				throw new IllegalArgumentException("反射异常：" + this.name, e);
			}
        	switch (this.type) {
        	case EQ:
        		wrapper.eq(column, this.value);
        		break;
        	case NE:
        		wrapper.ne(column, this.value);
        		break;
        	case LT:
        		wrapper.lt(column, this.value);
        		break;
        	case GT:
        		wrapper.gt(column, this.value);
        		break;
        	case LE:
        		wrapper.le(column, this.value);
        		break;
        	case GE:
        		wrapper.ge(column, this.value);
        		break;
        	case IN:
        		wrapper.in(column, this.value);
        		break;
        	case LIKE:
        		wrapper.like(column, "%" + this.value + "%");
        		break;
        	case BETWEEN:
        		final List<?> list = (List<?>) this.value;
        		wrapper.between(column, list.get(0), list.get(1));
        		break;
        	case IS_NULL:
        		wrapper.isNull(column);
        		break;
        	case IS_NOT_NULL:
        		wrapper.isNotNull(column);
        		break;
        	default:
        		throw new IllegalArgumentException("未知过滤类型：" + this.type);
        	}
        }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

    }

    /**
     * 排序类型
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

        /**
         * 排序字段
         */
        private final String name;
        /**
         * 排序类型
         */
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
        
        public <T> void order(QueryWrapper<T> wrapper) {
        	switch (this.type) {
        	case ASC:
        		wrapper.orderByAsc(this.name);
        		break;
        	case DESC:
        		wrapper.orderByDesc(this.name);
        		break;
        	default:
        		throw new IllegalArgumentException("未知排序类型：" + this.type);
        	}
        }

    }

    /**
     * 是否可以为空
     */
    private boolean nullable = true;
    /**
     * 过滤条件
     */
    private List<Filter> filter = new ArrayList<>();
    /**
     * 排序条件
     */
    private List<Sorted> sorted = new ArrayList<>();

    public static final <T extends DataEntity> FilterQuery<T> builder() {
        return new FilterQuery<T>();
    }
    
    public FilterQuery<T> merge(List<Filter> list) {
    	if(CollectionUtils.isEmpty(list)) {
    		return this;
    	}
    	this.filter.addAll(list);
    	return this;
    }

    public FilterQuery<T> eq(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.EQ));
        return this;
    }

    public FilterQuery<T> ne(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.NE));
        return this;
    }

    public FilterQuery<T> lt(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.LT));
        return this;
    }

    public FilterQuery<T> gt(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.GT));
        return this;
    }

    public FilterQuery<T> le(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.LE));
        return this;
    }

    public FilterQuery<T> in(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.IN));
        return this;
    }

    public FilterQuery<T> like(String name, String value) {
        this.filter.add(new Filter(name, value, Filter.Type.LIKE));
        return this;
    }

    public FilterQuery<T> between(String name, Object value) {
        this.filter.add(new Filter(name, value, Filter.Type.BETWEEN));
        return this;
    }

    public FilterQuery<T> isNull(String name) {
        this.filter.add(new Filter(name, null, Filter.Type.IS_NULL));
        return this;
    }

    public FilterQuery<T> isNotNull(String name) {
        this.filter.add(new Filter(name, null, Filter.Type.IS_NOT_NULL));
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
    
    public Specification<T> build() {
        return new Specification<T>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate[] filters = FilterQuery.this.filter.stream()
                    .filter(filter -> FilterQuery.this.nullable || filter.value != null)
                    .map(filter -> filter.predicate(root, builder)).collect(Collectors.toList())
                    .toArray(Predicate[]::new);
                final Order[] orders = FilterQuery.this.sorted.stream()
                	.map(sorted -> sorted.order(root, builder))
                    .collect(Collectors.toList()).toArray(Order[]::new);
                return query.where(filters).orderBy(orders).getRestriction();
            }
        };
    }
    
    public Wrapper<T> buildWrapper(Class<T> entity) {
    	final QueryWrapper<T> wrapper = new QueryWrapper<>();
    	FilterQuery.this.filter.stream()
    		.filter(filter -> FilterQuery.this.nullable || filter.value != null)
    		.forEach(filter -> filter.predicate(entity, wrapper));
    	FilterQuery.this.sorted.stream()
    		.forEach(sorted -> sorted.order(wrapper));
    	return wrapper;
    }

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public List<Filter> getFilter() {
		return filter;
	}

	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}

	public List<Sorted> getSorted() {
		return sorted;
	}

	public void setSorted(List<Sorted> sorted) {
		this.sorted = sorted;
	}
    
}
