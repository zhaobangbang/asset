package com.lansitec.dao.beans;

public class ManagersLoginInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String usermail;
	private String rkey;
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getUsermail() {
		return usermail;
	}
	public String getRkey() {
		return rkey;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}
	public void setRkey(String rkey) {
		this.rkey = rkey;
	}
	
	
}
