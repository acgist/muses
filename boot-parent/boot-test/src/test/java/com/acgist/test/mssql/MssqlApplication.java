package com.acgist.test.mssql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;

@SpringBootApplication(exclude = {
	Neo4jDataAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class
})
//默认配置：配置template不用配置factory
@MapperScan(basePackages = { "com.acgist.mysql.mapper", "com.acgist.**.dao.mapper" }, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MssqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MssqlApplication.class, args);
	}

}
