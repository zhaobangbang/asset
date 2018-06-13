package com.lansitec.app.jsondefs;

public class WarnInfoReq {
	private String username;
	private String data;
	public WarnInfoReq(){
		
	}
    public WarnInfoReq(String username,String data){
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
		return "WarnInfoReq [username=" + username + ", data=" + data + "]";
	}
}
