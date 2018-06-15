package com.lans.beans;

import java.util.Date;

public class BaiduGpsPoint {
    private Integer id;
    private String username;
    private String deveui;
    private Date startdatetime;
    private Date enddatetime;
	private String baidulati;
	private String baidulong;
	
	public BaiduGpsPoint(String username, String deveui,Date startdatetime,Date enddatetime){
		this.username = username;
		this.deveui = deveui;
		this.startdatetime = startdatetime;
		this.enddatetime = enddatetime;
		
	}
	
	public BaiduGpsPoint(String baidulati, String baidulong){
		this.baidulati = baidulati;
		this.baidulong = baidulong;
		
	}
	
	
	
	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getDeveui() {
		return deveui;
	}



	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}



	public Date getStartdatetime() {
		return startdatetime;
	}



	public void setStartdatetime(Date startdatetime) {
		this.startdatetime = startdatetime;
	}



	public Date getEnddatetime() {
		return enddatetime;
	}



	public void setEnddatetime(Date enddatetime) {
		this.enddatetime = enddatetime;
	}



	public String getBaidulati() {
		return baidulati;
	}
	public void setBaidulati(String baidulati) {
		this.baidulati = baidulati;
	}
	public String getBaidulong() {
		return baidulong;
	}
	public void setBaidulong(String baidulong) {
		this.baidulong = baidulong;
	}
	

}
