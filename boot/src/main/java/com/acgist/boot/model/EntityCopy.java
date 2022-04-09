package com.acgist.boot.model;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * EntityCopy
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class EntityCopy extends ModelCopy {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date modifyDate;

}
