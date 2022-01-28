package com.acgist.dao.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.acgist.data.es.BootDocument;

/**
 * ES Repository
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
@NoRepositoryBean
public interface BootRepository<T extends BootDocument> extends ElasticsearchRepository<T, Long> {

}
