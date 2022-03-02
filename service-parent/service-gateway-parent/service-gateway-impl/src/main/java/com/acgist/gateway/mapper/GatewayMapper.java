package com.acgist.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.gateway.pojo.entity.GatewayEntity;

@Mapper
public interface GatewayMapper extends BootMapper<GatewayEntity> {

}

