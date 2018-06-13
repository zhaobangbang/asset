package com.lansitec.springmvc.beans;

import java.util.List;

import com.lansitec.dao.beans.AssetInfo;

public class AssetInfoRspBean {
	private int page;
	private int total;
	private int records;
	private List<AssetInfo> rows;
	private String status;
	public AssetInfoRspBean(){
		
	}
	public AssetInfoRspBean(int page,int total,int records,List<AssetInfo> rows){
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
	public List<AssetInfo> getRows() {
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
	public void setRows(List<AssetInfo> rows) {
		this.rows = rows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AssetInfoRspBean [page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
				+ ", status=" + status + "]";
	}
}
