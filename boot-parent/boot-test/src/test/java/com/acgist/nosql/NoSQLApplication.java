package com.acgist.nosql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableNeo4jRepositories("com.acgist.nosql.**.neo4j")
@EnableElasticsearchRepositories("com.acgist.nosql.**.es")
public class NoSQLApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoSQLApplication.class, args);
	}

}
