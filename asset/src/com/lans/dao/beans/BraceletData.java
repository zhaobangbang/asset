package com.lans.dao.beans;

public class BraceletData {
	private String deveui;
	private long bTime;
	
    public BraceletData(){
    	
    }	
	
	public BraceletData(String deveui,long bTime){
		this.deveui = deveui;
		this.bTime = bTime;
	}

	public String getDeveui() {
		return deveui;
	}

	public long getbTime() {
		return bTime;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}

	public void setbTime(long bTime) {
		this.bTime = bTime;
	}
    
	
}
