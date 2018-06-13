package com.lansitec.app.jsondefs;

public class SingleWarnInfoReq {
	private String devId;
	private String warn_stime;//search start time
	private String warn_etime;//search final time
	public SingleWarnInfoReq(){
		
	}
	public String getDevId() {
		return devId;
	}
	public String getWarn_stime() {
		return warn_stime;
	}
	public String getWarn_etime() {
		return warn_etime;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public void setWarn_stime(String warn_stime) {
		this.warn_stime = warn_stime;
	}
	public void setWarn_etime(String warn_etime) {
		this.warn_etime = warn_etime;
	}
}
