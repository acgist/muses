package com.acgist.dao.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import com.acgist.data.entity.BootEntity;

/**
 * JPA Repository
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
@NoRepositoryBean
public interface BootRepository<T extends BootEntity> extends JpaRepositoryImplementation<T, Long> {

}
