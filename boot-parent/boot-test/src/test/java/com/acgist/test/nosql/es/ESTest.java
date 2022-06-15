package com.acgist.test.nosql.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.test.nosql.NoSQLApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 9300：TCP
 * 9200：RESTFul
 * 
 * @author acgist
 */
@Slf4j
@SpringBootTest(classes = NoSQLApplication.class)
public class ESTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	public void testSave() {
		final ArticleDocument articleDocument = new ArticleDocument();
		articleDocument.setId(1L);
		articleDocument.setTitle("碧螺萧萧");
		articleDocument.setContent("这是一个测试");
		this.articleRepository.save(articleDocument);
	}

	@Test
	public void testSearch() {
		this.articleRepository.findAll().forEach(v -> log.info("{}", v));
	}

	@Test
	public void testSelectByContent() {
		this.articleRepository.selectByQuery("这是一个测试").forEach(v -> log.info("{}", v));
		this.articleRepository.selectByContent("测试").forEach(v -> log.info("{}", v));
	}

}
