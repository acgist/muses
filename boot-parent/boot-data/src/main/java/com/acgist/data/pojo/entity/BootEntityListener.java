package com.acgist.data.pojo.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 新增修改监听（可以使用Auditing实现）
 * 
 * @author acgist
 */
public class BootEntityListener {

	@PrePersist
	public void prePersist(BootEntity entity) {
		entity.setCreateDate(new Date());
		entity.setModifyDate(new Date());
	}

	@PreUpdate
	public void preUpdate(BootEntity entity) {
		entity.setModifyDate(new Date());
	}

}
