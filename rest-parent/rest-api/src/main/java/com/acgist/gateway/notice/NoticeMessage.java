package com.acgist.gateway.notice;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>异步通知信息</p>
 * 
 * @author acgist
 */
public class NoticeMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>请求标识</p>
	 */
	private String queryId;
	/**
	 * <p>通知地址</p>
	 */
	private String noticeURL;
	/**
	 * <p>通知数据</p>
	 */
	private Map<String, Object> data;

	public NoticeMessage() {
	}

	/**
	 * @param queryId 请求标识
	 * @param noticeURL 通知地址
	 * @param data 通知数据
	 */
	public NoticeMessage(String queryId, String noticeURL, Map<String, Object> data) {
		this.queryId = queryId;
		this.noticeURL = noticeURL;
		this.data = data;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getNoticeURL() {
		return noticeURL;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
