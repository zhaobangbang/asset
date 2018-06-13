package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.SystemManagers;

public class SystemManagerRspBean {
	private int page;
	private int total;
	private int records;
	private List<SystemManagers> rows;
	public SystemManagerRspBean(){
		
	}
    public SystemManagerRspBean(int page,int total,int records,List<SystemManagers> rows){
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
	public List<SystemManagers> getRows() {
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
	public void setRows(List<SystemManagers> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "SystemManagerRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
				+ "]";
	}
}
