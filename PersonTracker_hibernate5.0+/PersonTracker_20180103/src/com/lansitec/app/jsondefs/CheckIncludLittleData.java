package com.lansitec.app.jsondefs;

public class CheckIncludLittleData {
	private String workerSN;
	private String datetime;
	private CheckAllData all;
	public CheckIncludLittleData(){
		
	}
    public CheckIncludLittleData(String workerSN,String datetime,CheckAllData all){
		this.workerSN = workerSN;
		this.datetime = datetime;
		this.all = all;
	}
	public String getWorkerSN() {
		return workerSN;
	}
	public String getDatetime() {
		return datetime;
	}
	public CheckAllData getAll() {
		return all;
	}
	public void setWorkerSN(String workerSN) {
		this.workerSN = workerSN;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setAll(CheckAllData all) {
		this.all = all;
	}
	@Override
	public String toString() {
		return "CheckIncludLittleData [workerSN=" + workerSN + ", datetime=" + datetime + ", all=" + all + "]";
	}
}
