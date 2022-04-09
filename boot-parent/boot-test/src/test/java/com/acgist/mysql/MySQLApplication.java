package com.acgist.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;

@SpringBootApplication(exclude = {
	Neo4jDataAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class
})
@MapperScan(basePackages = "com.acgist.mysql.mapper")
public class MySQLApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySQLApplication.class, args);
	}

}
