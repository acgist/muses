package com.acgist.boot.model;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class Entity extends Model {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	private LocalDateTime createDate;
	/**
	 * 修改时间
	 */
	private LocalDateTime modifyDate;

}
