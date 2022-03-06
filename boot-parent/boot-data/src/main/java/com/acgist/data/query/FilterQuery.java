package com.acgist.data.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.common.utils.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.acgist.boot.StringUtils;
import com.acgist.boot.data.MessageCodeException;
import com.acgist.data.entity.BootEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.Getter;
import lombok.Setter;

/**
 * 过滤查询
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
@Getter
@Setter
public class FilterQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterQuery.class);

	/**
	 * 通过MyBatis注解获取数据库列名
	 * 
	 * @param <T> 类型
	 * 
	 * @param entity entity
	 * @param name Java字段名称
	 * 
	 * @return 数据库列名
	 */
	private static final <T> String column(Class<T> entity, final String name) {
		final Field field = FieldUtils.findField(entity, name);
		if(field == null) {
			return name;
		}
		final TableField tableField = field.getAnnotation(TableField.class);
		if(tableField != null && StringUtils.isNotEmpty(tableField.value())) {
			return tableField.value();
		}
		final TableId tableId = field.getAnnotation(TableId.class);
		if(tableId != null && StringUtils.isNotEmpty(tableId.value())) {
			return tableId.value();
		}
		return name;
	}
	
	/**
	 * 过滤条件
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
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
		
		/**
		 * MyBatis查询条件
		 * 
		 * @param <T> 类型
		 * 
		 * @param entity entity
		 * @param wrapper wrapper
		 */
		public <T> void predicate(Class<T> entity, QueryWrapper<T> wrapper) {
			final String column = column(entity, this.name);
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
				wrapper.like(column, this.value);
				break;
			case BETWEEN:
				if(this.value instanceof List) {
					// TODO：JDK17
					final List<?> list = (List<?>) this.value;
					wrapper.between(column, list.get(0), list.get(1));
				} else {
					LOGGER.warn("不支持的between类型：{}", this.value == null ? null : this.value.getClass());
				}
				break;
			case IS_NULL:
				wrapper.isNull(column);
				break;
			case IS_NOT_NULL:
				wrapper.isNotNull(column);
				break;
			default:
				throw MessageCodeException.of("未知过滤类型：", this.type);
			}
		}

	}

	/**
	 * 排序类型
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
	public static class Sorted {

		/**
		 * 排序类型
		 * 
		 * @author acgist
		 */
		public enum Type {
			
			// 递增
			ASC,
			// 递减
			DESC;
			
			public Sorted of(String name) {
				return new Sorted(name, this);
			}
			
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

		/**
		 * MyBatis排序
		 * 
		 * @param <T> 类型
		 * 
		 * @param wrapper wrapper
		 */
		public <T> void order(QueryWrapper<T> wrapper) {
			switch (this.type) {
			case ASC:
				wrapper.orderByAsc(this.name);
				break;
			case DESC:
				wrapper.orderByDesc(this.name);
				break;
			default:
				throw MessageCodeException.of("未知排序类型：", this.type);
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

	public static final FilterQuery builder() {
		return new FilterQuery();
	}
	
	/**
	 * 设置可以为空
	 * 
	 * @return this
	 */
	public FilterQuery nullable() {
		this.nullable = true;
		return this;
	}

	/**
	 * 设置不能为空
	 * 
	 * @return this
	 */
	public FilterQuery nullless() {
		this.nullable = false;
		return this;
	}
	
	/**
	 * 合并过滤条件
	 * 
	 * @param list 过滤条件
	 * 
	 * @return this
	 */
	public FilterQuery merge(List<Filter> list) {
		if(CollectionUtils.isEmpty(list)) {
			return this;
		}
		this.filter.addAll(list);
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#EQ
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery eq(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.EQ));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#NE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery ne(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.NE));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#LT
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery lt(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.LT));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#GT
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery gt(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.GT));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#LE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery le(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.LE));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#IN
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery in(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.IN));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#LIKE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery like(String name, String value) {
		this.filter.add(new Filter(name, value, Filter.Type.LIKE));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#BETWEEN
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery between(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.BETWEEN));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#IS_NULL
	 * 
	 * @param name 属性名称
	 * 
	 * @return this
	 */
	public FilterQuery isNull(String name) {
		this.filter.add(new Filter(name, null, Filter.Type.IS_NULL));
		return this;
	}

	/**
	 * @see FilterQuery.Filter.Type#IS_NOT_NULL
	 * 
	 * @param name 属性名称
	 * 
	 * @return this
	 */
	public FilterQuery isNotNull(String name) {
		this.filter.add(new Filter(name, null, Filter.Type.IS_NOT_NULL));
		return this;
	}

	/**
	 * @see FilterQuery.Sorted.Type#ASC
	 * 
	 * @param name 属性名称
	 * 
	 * @return this
	 */
	public FilterQuery asc(String name) {
		this.sorted.add(new Sorted(name, Sorted.Type.ASC));
		return this;
	}

	/**
	 * @see FilterQuery.Sorted.Type#DESC
	 * 
	 * @param name 属性名称
	 * 
	 * @return this
	 */
	public FilterQuery desc(String name) {
		this.sorted.add(new Sorted(name, Sorted.Type.DESC));
		return this;
	}
	
	/**
	 * 重置数据
	 * 
	 * @return this
	 */
	public FilterQuery reset() {
		this.filter.clear();
		this.sorted.clear();
		this.nullable = true;
		return this;
	}
	
	/**
	 * 创建MyBatis查询条件
	 * 
	 * @param entity entity
	 * 
	 * @return MyBatis查询条件
	 */
	public <T extends BootEntity> Wrapper<T> build(Class<T> entity) {
		final QueryWrapper<T> wrapper = Wrappers.query();
		FilterQuery.this.filter.stream()
			.filter(filter -> FilterQuery.this.nullable || filter.value != null)
			.forEach(filter -> filter.predicate(entity, wrapper));
		FilterQuery.this.sorted.stream()
			.forEach(sorted -> sorted.order(wrapper));
		return wrapper;
	}
	
}
