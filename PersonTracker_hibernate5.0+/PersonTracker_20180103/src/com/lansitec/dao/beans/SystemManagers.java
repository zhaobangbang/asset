package com.lansitec.dao.beans;

import java.time.LocalDate;

public class SystemManagers implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String userkey;
	private String tel;
	private String usermail;
	private LocalDate time;
	public SystemManagers(){
		
	}
    public SystemManagers(String username,String userkey,String tel,String usermail,LocalDate time){
		this.username = username;
		this.userkey = userkey;
		this.tel = tel;
		this.usermail = usermail;
		this.time = time;
	}
	public Integer getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getUserkey() {
		return userkey;
	}
	public String getTel() {
		return tel;
	}
	public String getUsermail() {
		return usermail;
	}
	public LocalDate getTime() {
		return time;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}
	public void setTime(LocalDate time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "SystemManagers [id=" + id + ", username=" + username + ", userkey=" + userkey + ", tel=" + tel
				+ ", usermail=" + usermail + ", time=" + time + "]";
	}

}
