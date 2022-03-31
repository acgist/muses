package com.acgist.nosql.es;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;

import com.acgist.dao.es.BootRepository;

public interface ArticleRepository extends BootRepository<ArticleDocument> {

	List<ArticleDocument> selectByContent(String content);

	@Query("{\"query_string\":{\"query\":\"?0\",\"analyzer\":\"ik_smart\",\"fields\":[\"title\",\"content\"]}}")
	List<ArticleDocument> selectByQuery(String query);

}
