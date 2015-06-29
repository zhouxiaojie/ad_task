package com.ocean.ad.task.model;

import java.util.Date;

public class LogEventCount {
	private Integer id;
	private String event;
	private int count;
	private int uvCount;
	private String minDate;
	private Date createTime;
	private String tableName;
	
	public LogEventCount() {
		super();
	}
		
	public LogEventCount(String event, int count, int uvCount,
			String minDate, Date createTime,String tableName) {
		super();
		this.event = event;
		this.count = count;
		this.uvCount = uvCount;
		this.minDate = minDate;
		this.createTime = createTime;
		this.tableName = tableName;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getUvCount() {
		return uvCount;
	}
	public void setUvCount(int uvCount) {
		this.uvCount = uvCount;
	}
	
	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
