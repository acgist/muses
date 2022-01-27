package com.acgist.boot.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.acgist.data.entity.DataEntity;

/**
 * BootRepository
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
@NoRepositoryBean
public interface BootRepository<T extends DataEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>  {

}
