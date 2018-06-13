package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.ConstructInfo;

public class ConstructInfoRspBean {
	private int page;
	private int total;
	private int records;
	private List<ConstructInfo> rows;
	public ConstructInfoRspBean(int page,int total,int records,List<ConstructInfo> rows){
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
	public List<ConstructInfo> getRows() {
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
	public void setRows(List<ConstructInfo> rows) {
		this.rows = rows;
	}
}
