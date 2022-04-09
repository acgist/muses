package com.acgist.nosql.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.acgist.model.es.BootDocument;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "index_article")
public class ArticleDocument extends BootDocument {

	private static final long serialVersionUID = 1L;

	@Field(analyzer = "ik_smart", type = FieldType.Text)
	private String title;
	@Field(analyzer = "ik_smart", type = FieldType.Text)
	private String content;

}
