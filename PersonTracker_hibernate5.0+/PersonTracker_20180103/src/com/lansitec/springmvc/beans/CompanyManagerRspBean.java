package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.CompanyInfo;

public class CompanyManagerRspBean {
	private int page;
	private int total;
	private int records;
	private List<CompanyInfo> rows;
	public CompanyManagerRspBean(int page,int total,int records,List<CompanyInfo> rows){
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
	public List<CompanyInfo> getRows() {
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
	public void setRows(List<CompanyInfo> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "CompanyManagerRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
				+ "]";
	}
}
