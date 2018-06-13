package com.lansitec.app.jsondefs;

public class SingleWarnInfoRspData {
	private String datetime;
	private WarnInfomation warnInfomation;
	public SingleWarnInfoRspData(String datetime,WarnInfomation warnInfomation){
		this.datetime = datetime;
		this.warnInfomation = warnInfomation;
	}
	public String getDatetime() {
		return datetime;
	}
	public WarnInfomation getWarnInfomation() {
		return warnInfomation;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setWarnInfomation(WarnInfomation warnInfomation) {
		this.warnInfomation = warnInfomation;
	}
}
