package com.acgist.data.es;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<ArticleDocument, Long> {

	List<ArticleDocument> findByContent(String content);

	@Query("{\"query_string\":{\"query\":\"?0\",\"analyzer\":\"ik_smart\",\"fields\":[\"title\",\"content\"]}}")
	List<ArticleDocument> findByQuery(String query);

}
