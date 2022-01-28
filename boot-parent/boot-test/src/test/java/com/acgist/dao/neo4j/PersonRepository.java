package com.acgist.dao.neo4j;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PersonRepository extends Neo4jRepository<PersonNode, Long> {

	@Query("match (x:person {name:$name}) return x")
	PersonNode findByName(String name);

	@Query("match r=shortestPath((start:person {name:$start})-[*]-(end:person {name:$end})) return r")
	List<?> findRelationship(String start, String end);

}
