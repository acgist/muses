package com.acgist.user.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acgist.user.pojo.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	@Modifying
	@Transactional
	@Query(value = "insert into tb_order (name, create_date, modify_date) values (:#{#entity.name}, :#{#entity.createDate}, :#{#entity.modifyDate})", nativeQuery = true)
	void insert(OrderEntity entity);

}
