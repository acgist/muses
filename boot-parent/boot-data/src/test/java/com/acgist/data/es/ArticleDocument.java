package com.acgist.data.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.acgist.boot.pojo.bean.PojoCopy;

@Document(indexName = "index_article")
public class ArticleDocument extends PojoCopy {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	@Field(analyzer = "ik_smart", type = FieldType.Text)
	private String title;
	@Field(analyzer = "ik_smart", type = FieldType.Text)
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
