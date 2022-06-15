package com.acgist.test.nosql.neo4j;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.acgist.model.neo4j.BootNode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Node("person")
public class PersonNode extends BootNode {

	private static final long serialVersionUID = 1L;

	@Property("age")
	private Short age;
	private String name;
	@Relationship(type = "friend")
	private List<PersonNode> friend;
	@Relationship(type = "relative")
	private List<PersonRelationship> relative;

}
