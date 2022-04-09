package com.acgist.model.neo4j;

import java.util.Date;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.acgist.boot.model.ModelCopy;

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
public abstract class BootRelationship<T extends BootNode> extends ModelCopy {

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
	/**
	 * 关系节点
	 */
	@TargetNode
	private T target;
	
}
