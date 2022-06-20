package com.acgist.log.api;

import java.util.List;

import com.acgist.log.model.query.Query;
import com.acgist.log.model.vo.LogVo;

/**
 * 日志服务
 * 
 * @author acgist
 */
public interface ILogService {

	/**
	 * 查询日志列表
	 * 
	 * @param query 查询信息
	 * 
	 * @return 日志列表
	 */
	List<LogVo> query(Query query);
	
}
