package com.acgist.data.entity;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.acgist.boot.service.IdService;

/**
 * 雪花ID
 * 
 * @author acgist
 */
public class SnowflakeGenerator implements IdentifierGenerator {

	private static IdService ID_SERVICE;

	/**
	 * 配置IdService
	 * 
	 * @param idService IdService
	 */
	public static void init(IdService idService) {
		SnowflakeGenerator.ID_SERVICE = idService;
	}
	
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		while(ID_SERVICE == null) {
			Thread.yield();
		}
		return ID_SERVICE.id();
	}

}
