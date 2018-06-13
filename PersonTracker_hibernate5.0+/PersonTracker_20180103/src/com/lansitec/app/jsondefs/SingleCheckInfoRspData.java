package com.lansitec.app.jsondefs;

public class SingleCheckInfoRspData {
	private String datetime;
	private CheckAllData all;
	public SingleCheckInfoRspData(String datetime,CheckAllData all){
		this.datetime = datetime;
		this.all = all;
	}
	public String getDatetime() {
		return datetime;
	}
	public CheckAllData getAll() {
		return all;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setAll(CheckAllData all) {
		this.all = all;
	}
}
