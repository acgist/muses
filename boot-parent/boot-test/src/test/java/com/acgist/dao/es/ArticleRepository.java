package com.acgist.dao.es;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;

public interface ArticleRepository extends BootRepository<ArticleDocument> {

	List<ArticleDocument> findByContent(String content);

	@Query("{\"query_string\":{\"query\":\"?0\",\"analyzer\":\"ik_smart\",\"fields\":[\"title\",\"content\"]}}")
	List<ArticleDocument> findByQuery(String query);

}
