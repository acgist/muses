package com.acgist.data.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.acgist.common.JSONUtils;

/**
 * 数据库实体类
 * 
 * @author acgist
 */
@EntityListeners(DataEntityListener.class)
@MappedSuperclass
public abstract class DataEntity implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.acgist.data.entity.SnowflakeGenerator")
	private Long id;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modify_date")
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
		if(obj instanceof DataEntity) {
			final DataEntity entity = (DataEntity) obj;
			return Objects.equals(this.getId(), entity.getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}
	
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
