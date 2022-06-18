package com.acgist.log.dao.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.acgist.log.model.es.Log;

/**
 * 日志
 * 
 * @author acgist
 */
@Repository
public interface LogRepository extends ElasticsearchRepository<Log, Long> {

}
