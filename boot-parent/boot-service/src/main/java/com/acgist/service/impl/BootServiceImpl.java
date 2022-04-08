package com.acgist.service.impl;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.model.entity.BootEntity;
import com.acgist.service.BootService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 基础Service实现
 * 
 * @author acgist
 *
 * @param <M> Mapper
 * @param <T> 类型
 */
public abstract class BootServiceImpl<M extends BootMapper<T>, T extends BootEntity> extends ServiceImpl<M, T> implements BootService<T> {

}
