package com.acgist.dao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableNeo4jRepositories("com.acgist.data.neo4j")
@EnableElasticsearchRepositories("com.acgist.data.es")
public class DaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaoApplication.class, args);
	}

}
