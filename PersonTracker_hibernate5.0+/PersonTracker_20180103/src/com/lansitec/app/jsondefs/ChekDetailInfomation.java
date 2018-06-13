package com.lansitec.app.jsondefs;

public class ChekDetailInfomation {
	private String name;
	private String type;
	private String status;
	private String workingTime;
	private String getOffTime;
	public ChekDetailInfomation(){
		
	}
    public ChekDetailInfomation(String name,String type,String status,String workingTime,String getOffTime){
		this.name = name;
		this.type = type;
		this.status = status;
		this.workingTime = workingTime;
		this.getOffTime = getOffTime;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getStatus() {
		return status;
	}
	public String getWorkingTime() {
		return workingTime;
	}
	public String getGetOffTime() {
		return getOffTime;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}
	public void setGetOffTime(String getOffTime) {
		this.getOffTime = getOffTime;
	}
	@Override
	public String toString() {
		return "ChekDetailInfomation [name=" + name + ", type=" + type + ", status=" + status + ", workingTime="
				+ workingTime + ", getOffTime=" + getOffTime + "]";
	}
}
