package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.DevInfo;

public class DevsInfoListRspBean {
	private int page;
	private int total;
	private int records;
	private List<DevInfo> rows;
	public DevsInfoListRspBean(int page,int total,int records,List<DevInfo> rows){
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
	public List<DevInfo> getRows() {
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
	public void setRows(List<DevInfo> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "DevsInfoListRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
				+ "]";
	}
}
