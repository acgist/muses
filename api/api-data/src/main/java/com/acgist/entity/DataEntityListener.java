package com.acgist.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
