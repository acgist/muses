package com.acgist.test.nosql.neo4j;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;

import com.acgist.dao.neo4j.BootRepository;

public interface PersonRepository extends BootRepository<PersonNode> {

	@Query("match (n:person {name:$name}) return n")
	PersonNode selectByName(String name);

	@Query("match r=shortestPath((start:person {name:$start})-[*]-(end:person {name:$end})) return r")
	List<?> selectRelationship(String start, String end);

}
