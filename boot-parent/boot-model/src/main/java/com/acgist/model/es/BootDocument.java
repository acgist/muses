package com.acgist.model.es;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.acgist.boot.model.ModelCopy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ES文档
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class BootDocument extends ModelCopy {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@Id
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
