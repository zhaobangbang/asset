package com.lansitec.app.jsondefs;

public class WarnInfomation {
	private String type;
	private String description;
	private String warn_stime;
	private String warn_etime;
	private boolean warn_on;
	public WarnInfomation(String type,String description,String warn_stime,String warn_etime,boolean warn_on){
		this.type = type;
		this.description = description;
		this.warn_stime = warn_stime;
		this.warn_etime = warn_etime;
		this.warn_on = warn_on;
	}
	public String getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public String getWarn_stime() {
		return warn_stime;
	}
	public String getWarn_etime() {
		return warn_etime;
	}
	public boolean isWarn_on() {
		return warn_on;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setWarn_stime(String warn_stime) {
		this.warn_stime = warn_stime;
	}
	public void setWarn_etime(String warn_etime) {
		this.warn_etime = warn_etime;
	}
	public void setWarn_on(boolean warn_on) {
		this.warn_on = warn_on;
	}
	@Override
	public String toString() {
		return "WarnInfomation [type=" + type + ", description=" + description + ", warn_stime=" + warn_stime
				+ ", warn_etime=" + warn_etime + ", warn_on=" + warn_on + "]";
	}
}
