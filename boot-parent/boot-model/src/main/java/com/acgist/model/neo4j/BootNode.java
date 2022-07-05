package com.acgist.model.neo4j;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.Id;

import com.acgist.boot.model.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Neo4j节点
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class BootNode extends Model {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
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
