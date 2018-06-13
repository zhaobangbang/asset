package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.ProjectManagers;

public class ProjectManagerRspBean {
	private int page;
	private int total;
	private int records;
	private List<ProjectManagers> rows;
	public ProjectManagerRspBean(int page,int total,int records,List<ProjectManagers> rows){
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
	public List<ProjectManagers> getRows() {
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
	public void setRows(List<ProjectManagers> rows) {
		this.rows = rows;
	}
}
