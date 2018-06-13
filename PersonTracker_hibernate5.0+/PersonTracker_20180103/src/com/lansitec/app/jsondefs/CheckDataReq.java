package com.lansitec.app.jsondefs;

public class CheckDataReq {
	private String username;
	private String data;
	public CheckDataReq(){
		
	}
    public CheckDataReq(String username,String data){
		this.username = username;
		this.data = data;
	}
	public String getUsername() {
		return username;
	}
	public String getData() {
		return data;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "CheckDataReq [username=" + username + ", data=" + data + "]";
	}
}
