package com.acgist.model.neo4j;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.acgist.boot.model.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Neo4j关系
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class BootRelationship<T extends BootNode> extends Model {

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
	/**
	 * 关系节点
	 */
	@TargetNode
	private T target;
	
}
