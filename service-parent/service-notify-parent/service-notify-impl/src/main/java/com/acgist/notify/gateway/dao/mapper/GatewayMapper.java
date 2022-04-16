package com.acgist.notify.gateway.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.notify.gateway.model.entity.GatewayEntity;

@Mapper
public interface GatewayMapper extends BootMapper<GatewayEntity> {

}

