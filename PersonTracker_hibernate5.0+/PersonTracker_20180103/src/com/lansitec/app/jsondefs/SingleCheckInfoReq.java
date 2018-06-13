package com.lansitec.app.jsondefs;

public class SingleCheckInfoReq {
	private String workerSN;
	private String workingDay;
	public SingleCheckInfoReq(){
		
	}
	public String getWorkerSN() {
		return workerSN;
	}
	public String getWorkingDay() {
		return workingDay;
	}
	public void setWorkerSN(String workerSN) {
		this.workerSN = workerSN;
	}
	public void setWorkingDay(String workingDay) {
		this.workingDay = workingDay;
	}
}
