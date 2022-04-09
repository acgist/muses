package com.acgist.nosql.neo4j;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import com.acgist.model.neo4j.BootRelationship;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@RelationshipProperties
public class PersonRelationship extends BootRelationship<PersonNode> {

	private static final long serialVersionUID = 1L;

	private String name;

}
