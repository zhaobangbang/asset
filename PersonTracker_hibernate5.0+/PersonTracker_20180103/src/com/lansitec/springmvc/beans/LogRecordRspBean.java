package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.LogRecord;

public class LogRecordRspBean {
	private int page;
	private int total;
	private int records;
	private List<LogRecord> rows;
	public LogRecordRspBean(int page,int total,int records,List<LogRecord> rows){
		this.page = page;
		this.total = total;
		this.records = records;
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public int getTotal() {
		return total;
	}
	public int getRecords() {
		return records;
	}
	public List<LogRecord> getRows() {
		return rows;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public void setRows(List<LogRecord> rows) {
		this.rows = rows;
	}
}
