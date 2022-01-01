package com.acgist.data.es;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.data.DataApplication;

/**
 * 9300：TCP
 * 9200：RESTFul
 * 
 * @author acgist
 */
@SpringBootTest(classes = DataApplication.class)
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
	public void testFindByContent() {
		this.articleRepository.findByQuery("这是一个测试").forEach(v -> LOGGER.info("{}", v));
		this.articleRepository.findByContent("测试").forEach(v -> LOGGER.info("{}", v));
	}

}
