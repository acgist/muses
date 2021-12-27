package com.acgist.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acgist.gateway.pojo.entity.GatewayEntity;

@Repository
public interface GatewayRepository extends JpaRepository<GatewayEntity, Long> {

}

