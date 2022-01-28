package com.acgist.dao.mapper;

import com.acgist.data.entity.DataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * MyBatis Mapper
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public interface BootMapper<T extends DataEntity> extends BaseMapper<T> {

}
