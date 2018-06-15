package com.lans.dao.beans;

import java.util.Date;
//Generated 2017-6-3 9:35:20 by Hibernate Tools 5.2.0.CR1

/**
* GpsData generated by hbm2java
*/
public class GpsData implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String owner;
	private String deveui;
	private float latitude;
	private float longitude;
	private float baidulati;
	private float baidulong;
	private String type;
	private Date time;
	public GpsData(){
		
	}
	public GpsData( String owner,String deveui,float latitude,float longitude,float baidulati,float baidulong,String type,Date time){
		this.owner = owner;
		this.deveui = deveui;
		this.latitude = latitude;
		this.longitude = longitude;
		this.baidulati = baidulati;
		this.baidulong = baidulong;
		this.type = type;
		this.time = time;
	}
	public GpsData( String owner,String deveui,float latitude,float longitude,String type,Date time){
		this.owner = owner;
		this.deveui = deveui;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.time = time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getBaidulati() {
		return baidulati;
	}
	public void setBaidulati(float baidulati) {
		this.baidulati = baidulati;
	}
	public float getBaidulong() {
		return baidulong;
	}
	public void setBaidulong(float baidulong) {
		this.baidulong = baidulong;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
