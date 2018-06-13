package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.StatusRecord;

public class StatusRecordDataRspBean {
	private int page;
	private int total;
	private int records;
	private List<StatusRecord> rows;
	public StatusRecordDataRspBean(){
		
	}
	public StatusRecordDataRspBean(int page,int total,int records,List<StatusRecord> rows){
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
	public List<StatusRecord> getRows() {
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
	public void setRows(List<StatusRecord> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "StatusRecordDataRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
				+ "]";
	}
}
