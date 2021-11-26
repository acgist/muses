package com.acgist.data.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 新增修改监听（可以使用Auditing实现）
 * 
 * @author acgist
 */
public class DataEntityListener {

	@PrePersist
	public void prePersist(DataEntity entity) {
		entity.setCreateDate(new Date());
		entity.setModifyDate(new Date());
	}

	@PreUpdate
	public void preUpdate(DataEntity entity) {
		entity.setModifyDate(new Date());
	}

}
