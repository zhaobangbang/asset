package com.lansitec.app.jsondefs;

public class WarnInfoLittleData {
	private String devId;
	private String dateTime;
	private WarnInfoAllData all;
	public WarnInfoLittleData(String devId,String dateTime,WarnInfoAllData all){
		this.devId = devId;
		this.dateTime = dateTime;
		this.all = all;
	}
	public String getDevId() {
		return devId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public WarnInfoAllData getAll() {
		return all;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public void setAll(WarnInfoAllData all) {
		this.all = all;
	}
	@Override
	public String toString() {
		return "WarnInfoLittleData [devId=" + devId + ", dateTime=" + dateTime + ", all=" + all + "]";
	}
	
}
