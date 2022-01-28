package com.acgist.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan("com.acgist.**.entity")
@MapperScan("com.acgist.mysql.**.mapper")
@EnableJpaRepositories("com.acgist.mysql.**.repository")
@SpringBootApplication(exclude = {
	Neo4jDataAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class
})
@EnableTransactionManagement
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}
