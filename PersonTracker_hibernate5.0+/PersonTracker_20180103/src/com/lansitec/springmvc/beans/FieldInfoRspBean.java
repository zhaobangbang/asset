package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.FieldInfo;

public class FieldInfoRspBean {
	private int page;
	private int total;
	private int records;
	private List<FieldInfo> rows;
	public FieldInfoRspBean(int page,int total,int records,List<FieldInfo> rows){
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
	public List<FieldInfo> getRows() {
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
	public void setRows(List<FieldInfo> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "FieldInfoRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows + "]";
	}
}
