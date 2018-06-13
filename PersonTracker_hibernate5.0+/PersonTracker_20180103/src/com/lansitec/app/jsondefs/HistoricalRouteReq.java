package com.lansitec.app.jsondefs;

public class HistoricalRouteReq {
	private String devId;
	private String timeBefore;
	private String timeAfter;
	public String getDevId() {
		return devId;
	}
	public String getTimeBefore() {
		return timeBefore;
	}
	public String getTimeAfter() {
		return timeAfter;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public void setTimeBefore(String timeBefore) {
		this.timeBefore = timeBefore;
	}
	public void setTimeAfter(String timeAfter) {
		this.timeAfter = timeAfter;
	}

	

}
