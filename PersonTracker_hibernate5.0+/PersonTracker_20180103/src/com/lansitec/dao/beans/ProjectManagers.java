package com.lansitec.dao.beans;

import java.time.LocalDate;
import com.lansitec.enumlist.Prio;

public class ProjectManagers implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String sn;
	private String userkey;
	private String tel;
	private String usermail;
	private LocalDate time;
	private String company;
	private String city;
	private String field;
	private Prio prio;
	public ProjectManagers(String username,String sn,String userkey,String tel,String usermail,LocalDate time,String company,String city,String field,Prio prio){
		this.username = username;
		this.sn = sn;
		this.userkey = userkey;
		this.tel = tel;
		this.usermail = usermail;
		this.time  = time;
		this.company = company;
		this.city = city;
		this.field = field;
		this.prio = prio;
	}
    public ProjectManagers(){
		
	}
	public Integer getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getSn() {
		return sn;
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
	public String getCompany() {
		return company;
	}
	public String getCity() {
		return city;
	}
	public String getField() {
		return field;
	}
	public Prio getPrio() {
		return prio;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	public void setCompany(String company) {
		this.company = company;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setField(String field) {
		this.field = field;
	}
	public void setPrio(Prio prio) {
		this.prio = prio;
	}
}
