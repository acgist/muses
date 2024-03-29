package com.acgist.model.query;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.model.Model;
import com.acgist.boot.utils.DateUtils;
import com.acgist.model.entity.BootEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Getter;
import lombok.Setter;

/**
 * 过滤查询
 * 
 * @author acgist
 */
@Getter
@Setter
public class FilterQuery extends Model {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 字段校验
	 */
	private static final String COLUMN_REGEX = "[a-zA-Z1-9_\\.]+";
	
	/**
	 * 缓存
	 */
	private static final Map<Class<?>, Map<String, String>> COLUMN_CACHE = new ConcurrentHashMap<>();
	
	/**
	 * 通过MyBatis注解获取数据库列名
	 * 
	 * @param <T> 类型
	 * 
	 * @param entity entity
	 * @param name Java字段名称
	 * @param joinEntities 连表实体
	 * 
	 * @return 数据库列名
	 */
	private static final <T> String column(Class<T> entity, final String name, final Class<?> ... joinEntities) {
		String column;
		// 本表
		if(entity != null) {
			column = columnDefaultValue(entity, name, null);
			if(column != null) {
				return column;
			}
		}
		// 连表
		if(ArrayUtils.isNotEmpty(joinEntities)) {
			for (Class<?> join : joinEntities) {
				column = columnDefaultValue(join, name, null);
				if(column != null) {
					return column;
				}
			}
		}
		return name;
	}
	
	/**
	 * 通过MyBatis注解获取数据库列名
	 * 
	 * @param <T> 类型
	 * 
	 * @param entity entity
	 * @param name Java字段名称
	 * @param defaultValue 字段不存在默认值
	 * 
	 * @return 数据库列名
	 */
	private static final <T> String columnDefaultValue(Class<T> entity, final String name, final String defaultValue) {
		if(entity == null) {
			return defaultValue;
		}
		final Map<String, String> map = COLUMN_CACHE.computeIfAbsent(entity, key -> new ConcurrentHashMap<>());
		return map.computeIfAbsent(name, key -> {
			final Field field = FieldUtils.getField(entity, name, true);
			if(field == null) {
				return defaultValue;
			}
			final TableField tableField = field.getAnnotation(TableField.class);
			if(tableField != null && StringUtils.isNotEmpty(tableField.value())) {
				return tableField.value();
			}
			final TableId tableId = field.getAnnotation(TableId.class);
			if(tableId != null && StringUtils.isNotEmpty(tableId.value())) {
				return tableId.value();
			}
			// 字段存在直接返回名称
			return name;
		});
	}
	
	/**
	 * 获取别名列名
	 * 
	 * @param thisAlias 优先别名
	 * @param thatAlias 次要别名
	 * @param column 列名
	 * 
	 * @return 列名
	 */
	private static final String aliasColumn(String thisAlias, String thatAlias, String column) {
		return
			StringUtils.isNotEmpty(thisAlias) ? thisAlias + "." + column :
			StringUtils.isNotEmpty(thatAlias) ? thatAlias + "." + column :
			column;
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
			// not in
			NOT_IN,
			// like
			LIKE,
			// not like
			NOT_LIKE,
			// between
			BETWEEN,
			// is null
			IS_NULL,
			// is not null
			IS_NOT_NULL,
			// 包含文本列表：模糊in查询
			INCLUDE,
			// 排除文本列表：模糊not in查询
			EXCLUDE;

			public Filter of(String name, Object value) {
				return new Filter(name, value, this);
			}
			
		}

		/**
		 * 别名
		 */
		private String alias;
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
		
		public Filter(String alias, String name, Object value, Type type) {
			this.alias = alias;
			this.name = name;
			this.value = value;
			this.type = type;
		}
		
		/**
		 * @see #filter(String, Class, QueryWrapper)
		 */
		public <T> void filter(Class<T> entity, QueryWrapper<T> wrapper, Class<?> ... joinEntities) {
			this.filter(null, entity, wrapper);
		}
		
		/**
		 * MyBatis查询条件
		 * 
		 * @param <T> 类型
		 * 
		 * @param alias 别名
		 * @param entity entity
		 * @param wrapper wrapper
		 * @param joinEntities 连表实体
		 */
		public <T> void filter(String alias, Class<T> entity, QueryWrapper<T> wrapper, Class<?> ... joinEntities) {
			final String column = aliasColumn(this.alias, alias, column(entity, this.name, joinEntities));
			FilterQuery.checkColumn(column);
			switch (this.type) {
			case EQ -> wrapper.eq(column, this.value);
			case NE -> wrapper.ne(column, this.value);
			case LT -> wrapper.lt(column, this.value);
			case GT -> wrapper.gt(column, this.value);
			case LE -> wrapper.le(column, this.value);
			case GE -> wrapper.ge(column, this.value);
			case IN -> {
				if(this.value != null && Collection.class.isAssignableFrom(this.value.getClass())) {
					wrapper.in(column, (Collection<?>) this.value);
				} else if(this.value != null && this.value.getClass().isArray()) {
					wrapper.in(column, this.value);
				} else {
					throw MessageCodeException.of("不支持的in类型：", this.value);
				}
			}
			case NOT_IN -> {
				if(this.value != null && Collection.class.isAssignableFrom(this.value.getClass())) {
					wrapper.notIn(column, (Collection<?>) this.value);
				} else if(this.value != null && this.value.getClass().isArray()) {
					wrapper.notIn(column, this.value);
				} else {
					throw MessageCodeException.of("不支持的notIn类型：", this.value);
				}
			}
			case LIKE -> wrapper.like(column, this.value);
			case NOT_LIKE -> wrapper.notLike(column, this.value);
			case BETWEEN -> {
				if(this.value != null && Collection.class.isAssignableFrom(this.value.getClass())) {
					final Collection<?> collection = (Collection<?>) this.value;
					final Iterator<?> iterator = collection.iterator();
					wrapper.between(
						column,
						iterator.hasNext() ? iterator.next() : null,
						iterator.hasNext() ? FilterQuery.between(iterator.next()) : null
					);
				} else if(this.value != null && this.value.getClass().isArray()) {
					// 注意：不支持基本类型数组
					final Object[] array = (Object[]) this.value;
					wrapper.between(
						column,
						array[0],
						FilterQuery.between(array[1])
					);
				} else {
					throw MessageCodeException.of("不支持的between类型：", this.value);
				}
			}
			case IS_NULL -> wrapper.isNull(column);
			case IS_NOT_NULL -> wrapper.isNotNull(column);
			case INCLUDE -> {
				wrapper.and(includeWrapper -> {
					if(this.value != null && Collection.class.isAssignableFrom(this.value.getClass())) {
						final Collection<?> collection = (Collection<?>) this.value;
						collection.forEach(value -> {
							Filter.Type.LIKE.of(column, value).filter(entity, includeWrapper.or());
						});
					} else if(this.value != null && this.value.getClass().isArray()) {
						final Object[] array = (Object[]) this.value;
						for(Object value : array) {
							Filter.Type.LIKE.of(column, value).filter(entity, includeWrapper.or());
						}
					} else {
						throw MessageCodeException.of("不支持的include类型：", this.value);
					}
				});
			}
			case EXCLUDE -> {
				wrapper.not(excludeWrapper -> {
					if(this.value != null && Collection.class.isAssignableFrom(this.value.getClass())) {
						final Collection<?> collection = (Collection<?>) this.value;
						collection.forEach(value -> {
							Filter.Type.LIKE.of(column, value).filter(entity, excludeWrapper.or());
						});
					} else if(this.value != null && this.value.getClass().isArray()) {
						final Object[] array = (Object[]) this.value;
						for(Object value : array) {
							Filter.Type.LIKE.of(column, value).filter(entity, excludeWrapper.or());
						}
					} else {
						throw MessageCodeException.of("不支持的exclude类型：", this.value);
					}
				});
			}
			default -> throw MessageCodeException.of("未知过滤类型：", this.type);
			}
		}

	}

	/**
	 * 优化between参数
	 * 
	 * @param object 原始参数
	 * 
	 * @return 优化参数
	 */
	private static final Object between(Object object) {
		if(object instanceof Date date) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			return calendar.getTime();
		} else if(object instanceof LocalDate localDate) {
			return localDate.plusDays(1);
		} else if(object instanceof LocalDateTime localDateTime) {
			return localDateTime.plusDays(1);
		} else if(object instanceof String value && value.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
			final Date date = DateUtils.parse(value, DateStyle.YYYY_MM_DD.getFormat());
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			return DateUtils.format(calendar.getTime(), DateStyle.YYYY_MM_DD.getFormat());
		}
		return object;
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
		 * 别名
		 */
		private String alias;
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
		
		public Sorted(String alias, String name, Type type) {
			this.alias = alias;
			this.name = name;
			this.type = type;
		}

		/**
		 * @see #order(String, QueryWrapper)
		 */
		public <T> void order(Class<T> entity, QueryWrapper<T> wrapper, Class<?> ... joinEntities) {
			this.order(null, entity, wrapper);
		}
		
		/**
		 * MyBatis排序
		 * 
		 * @param <T> 类型
		 * 
		 * @param alias 别名
		 * @param entity entity
		 * @param wrapper wrapper
		 * @param joinEntities 连表实体
		 */
		public <T> void order(String alias, Class<T> entity, QueryWrapper<T> wrapper, Class<?> ... joinEntities) {
			final String column = aliasColumn(this.alias, alias, column(entity, this.name, joinEntities));
			FilterQuery.checkColumn(column);
			switch (this.type) {
			case ASC  -> wrapper.orderByAsc(column);
			case DESC -> wrapper.orderByDesc(column);
			default   -> throw MessageCodeException.of("未知排序类型：", this.type);
			}
		}
		
	}
	
	/**
	 * 校验字段
	 * 
	 * @param column 字段
	 */
	private static final void checkColumn(String column) {
        if(column == null || !column.matches(COLUMN_REGEX)) {
            throw MessageCodeException.of(MessageCode.CODE_3405);
        }
	}

	/**
	 * 是否可以为空
	 */
	private boolean nullable = true;
	/**
	 * 查询列
	 * 
	 * 注意：只能筛选本表字段，连表字段建议直接使用SQL别名映射。
	 */
	private List<String> selectColumn = new ArrayList<>();
	/**
	 * 忽略列
	 * 
	 * 注意：只能筛选本表字段，连表字段建议直接使用SQL别名映射。
	 */
	private List<String> ignoreColumn = new ArrayList<>();
	/**
	 * 过滤条件
	 */
	private List<Filter> filter = new ArrayList<>();
	/**
	 * 排序条件
	 */
	private List<Sorted> sorted = new ArrayList<>();
	/**
	 * 每页条数
	 */
	private Integer size;
	/**
	 * 当前页码
	 */
	private Integer current;
	
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
	 * @see FilterQuery.Filter.Type#GE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery ge(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.GE));
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
	 * @see FilterQuery.Filter.Type#NOT_IN
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery notIn(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.NOT_IN));
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
	 * @see FilterQuery.Filter.Type#NOT_LIKE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery notLike(String name, String value) {
		this.filter.add(new Filter(name, value, Filter.Type.NOT_LIKE));
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
	 * @see FilterQuery.Filter.Type#INCLUDE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery include(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.INCLUDE));
		return this;
	}
	
	/**
	 * @see FilterQuery.Filter.Type#EXCLUDE
	 * 
	 * @param name 属性名称
	 * @param value 属性值
	 * 
	 * @return this
	 */
	public FilterQuery exclude(String name, Object value) {
		this.filter.add(new Filter(name, value, Filter.Type.EXCLUDE));
		return this;
	}
	
	/**
	 * 获取过滤条件
	 * 
	 * @param name 属性名称
	 * 
	 * @return 过滤条件
	 */
	public Filter get(String name) {
		return this.filter.stream()
			.filter(filter -> StringUtils.equals(filter.name, name))
			.findFirst()
			.orElse(null);
	}

	/**
	 * 删除过滤条件
	 * 
	 * @param name 属性名称
	 * 
	 * @return 过滤条件
	 */
	public Filter remove(String name) {
		final Iterator<Filter> iterator = this.filter.iterator();
		while(iterator.hasNext()) {
			final Filter filter = iterator.next();
			if(StringUtils.equals(filter.name, name)) {
				iterator.remove();
				return filter;
			}
		}
		return null;
	}

	/**
     * 批量设置别名
     * 
     * @param alias  别名
     * @param fields 字段名称
     */
    public void setAlias(String alias, String ... fields) {
        Stream.of(fields)
        .map(this::get)
        .filter(Objects::nonNull)
        .forEach(v -> v.setAlias(alias));
    }
	
	/**
	 * 获取条件Map
	 * 
	 * @return 条件Map
	 */
	public Map<String, Object> toFilterMap() {
		return this.filter.stream().collect(Collectors.toMap(Filter::getName, Filter::getValue));
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
	 * 根据ID排序
	 * 
	 * @return this
	 */
	public FilterQuery ascById() {
		return this.asc(BootEntity.PROPERTY_ID);
	}
	
	/**
	 * 根据ID倒序
	 * 
	 * @return this
	 */
	public FilterQuery descById() {
		return this.desc(BootEntity.PROPERTY_ID);
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
		this.selectColumn.clear();
		this.ignoreColumn.clear();
		return this;
	}
	
	/**
	 * @see #build(String, Class)
	 */
	public <T extends BootEntity> Wrapper<T> build(Class<T> entity) {
		return this.build(null, null, entity);
	}
	
	/**
	 * @see #build(String, Class)
	 */
	public <T extends BootEntity> Wrapper<T> build(String alias, Class<T> entity) {
		return this.build(null, alias, entity);
	}
	
	/**
	 * @see #build(String, Class)
	 */
	public <T extends BootEntity> Wrapper<T> build(QueryWrapper<T> wrapper, Class<T> entity) {
		return this.build(wrapper, null, entity);
	}
	
	/**
	 * 创建MyBatis查询条件
	 * 
	 * @param wrapper 查询器
	 * @param alias 别名
	 * @param entity 实体
	 * @param joinEntities 连表实体
	 * 
	 * @return MyBatis查询条件
	 */
	public <T extends BootEntity> Wrapper<T> build(QueryWrapper<T> wrapper, String alias, Class<T> entity, Class<?> ... joinEntities) {
		final QueryWrapper<T> queryWrapper = wrapper == null ? Wrappers.query() : wrapper;
		if(CollectionUtils.isNotEmpty(this.selectColumn)) {
			// 只用处理本表
			queryWrapper.select(field -> this.selectColumn.contains(field.getField().getName()));
		}
		if(CollectionUtils.isNotEmpty(this.ignoreColumn)) {
			// 只用处理本表
			queryWrapper.select(field -> !this.ignoreColumn.contains(field.getField().getName()));
		}
		this.filter.stream()
			.filter(filter -> this.nullable || filter.value != null)
			.forEach(filter -> filter.filter(alias, entity, queryWrapper, joinEntities));
		this.sorted.stream()
			.forEach(sorted -> sorted.order(alias, entity, queryWrapper, joinEntities));
		return queryWrapper;
	}
	
	/**
	 * 创建分页信息
	 * 
	 * @param <T> 实体类型
	 * 
	 * @return 分页信息
	 */
	public <T extends BootEntity> Page<T> buildPage() {
		return Page.of(this.current, this.size);
	}
	
}
