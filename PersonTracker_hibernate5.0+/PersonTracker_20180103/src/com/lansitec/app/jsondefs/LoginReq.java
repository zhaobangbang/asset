package com.lansitec.app.jsondefs;

public class LoginReq {
	private String username;
	private String password;
	
	public LoginReq(){
		
	}
    public LoginReq(String username,String password){
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
