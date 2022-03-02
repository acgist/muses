package com.acgist.user.pojo.query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户查询
 * 
 * @author acgist
 */
@Getter
@Setter
public class UserQuery {

	private String name;
	private Date beginDate;
	private Date endDate;
}
