package com.acgist.dao.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.acgist.data.neo4j.BootNode;

/**
 * Neo4j Repository
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
@NoRepositoryBean
public interface BootRepository<T extends BootNode> extends Neo4jRepository<T, Long> {

}
