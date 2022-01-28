package com.acgist.dao.neo4j;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.acgist.data.neo4j.BootRelationship;

@RelationshipProperties
public class PersonRelationship extends BootRelationship {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String name;
	@TargetNode
	private PersonNode target;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PersonNode getTarget() {
		return target;
	}

	public void setTarget(PersonNode target) {
		this.target = target;
	}

}
