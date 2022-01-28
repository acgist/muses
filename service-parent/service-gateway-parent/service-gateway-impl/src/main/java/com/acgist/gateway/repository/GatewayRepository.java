package com.acgist.gateway.repository;

import org.springframework.stereotype.Repository;

import com.acgist.dao.repository.BootRepository;
import com.acgist.gateway.pojo.entity.GatewayEntity;

@Repository
public interface GatewayRepository extends BootRepository<GatewayEntity> {

}

