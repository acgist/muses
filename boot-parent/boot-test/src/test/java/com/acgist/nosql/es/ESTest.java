package com.acgist.nosql.es;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.nosql.NoSQLApplication;

/**
 * 9300：TCP
 * 9200：RESTFul
 * 
 * @author acgist
 */
@SpringBootTest(classes = NoSQLApplication.class)
public class ESTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESTest.class);

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
		this.articleRepository.findAll().forEach(v -> LOGGER.info("{}", v));
	}

	@Test
	public void testSelectByContent() {
		this.articleRepository.selectByQuery("这是一个测试").forEach(v -> LOGGER.info("{}", v));
		this.articleRepository.selectByContent("测试").forEach(v -> LOGGER.info("{}", v));
	}

}
