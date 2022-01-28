package com.acgist.nosql.neo4j;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.acgist.data.neo4j.BootNode;

@Node("person")
public class PersonNode extends BootNode {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	@Property("age")
	private Short age;
	private String name;
	@Relationship(type = "friend")
	private List<PersonNode> friend;
	@Relationship(type = "relative")
	private List<PersonRelationship> relative;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PersonNode> getFriend() {
		return friend;
	}

	public void setFriend(List<PersonNode> friend) {
		this.friend = friend;
	}

	public List<PersonRelationship> getRelative() {
		return relative;
	}

	public void setRelative(List<PersonRelationship> relative) {
		this.relative = relative;
	}

}
