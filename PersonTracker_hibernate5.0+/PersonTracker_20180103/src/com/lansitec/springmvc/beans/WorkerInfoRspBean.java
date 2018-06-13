package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.WorkerInfo;

public class WorkerInfoRspBean {
	private int page;
	private int total;
	private int records;
	private List<WorkerInfo> rows;
	private String status;
	public WorkerInfoRspBean(){
		
	}
	public WorkerInfoRspBean(int page,int total,int records,List<WorkerInfo> rows){
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
	public List<WorkerInfo> getRows() {
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
	public void setRows(List<WorkerInfo> rows) {
		this.rows = rows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
