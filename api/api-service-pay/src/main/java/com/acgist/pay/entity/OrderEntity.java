package com.acgist.pay.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.acgist.data.entity.DataEntity;

@Entity
@Table(name = "tb_order")
public class OrderEntity extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
