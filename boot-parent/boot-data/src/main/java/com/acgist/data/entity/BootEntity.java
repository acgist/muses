package com.acgist.data.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.boot.pojo.bean.PojoCopy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数据库实体类
 * 
 * 通用属性提取：@Embedded/@Embeddable
 * 
 * @author acgist
 */
@EntityListeners(BootEntityListener.class)
@MappedSuperclass
public abstract class BootEntity extends PojoCopy {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_CREATE_DATE = "createDate";
	public static final String PROPERTY_MODIFY_DATE = "modifyDate";
	
	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@TableId(value = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.acgist.data.entity.SnowflakeGenerator")
	private Long id;
	/**
	 * 创建时间
	 */
	@Column(name = "create_date")
	@TableField(value = "create_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**
	 * 修改时间
	 */
	@Column(name = "modify_date")
	@TableField(value = "modify_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		// 子类必须重写
		if(obj instanceof BootEntity) {
			final BootEntity entity = (BootEntity) obj;
			return Objects.equals(this.getId(), entity.getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}
	
}
