package com.acgist.user.pojo.query;

import java.util.Date;

/**
 * 用户查询
 * 
 * @author acgist
 */
public class UserQuery {

	private String name;
	private Date beginDate;
	private Date endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
