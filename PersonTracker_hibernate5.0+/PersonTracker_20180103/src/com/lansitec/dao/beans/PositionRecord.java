package com.lansitec.dao.beans;

import java.time.LocalDateTime;

public class PositionRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Integer id;
    private String deveui;
    private String mapid;
    private float x;
    private float y;
    private LocalDateTime time;
    public PositionRecord(){
    	
    }
    public PositionRecord(String deveui,String mapid,float x,float y,LocalDateTime time){
    	this.deveui = deveui;
    	this.mapid = mapid;
    	this.x = x;
    	this.y = y;
    	this.time = time;
    }
	public Integer getId() {
		return id;
	}
	public String getDeveui() {
		return deveui;
	}
	public String getMapid() {
		return mapid;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "PositionRecord [id=" + id + ", deveui=" + deveui + ", mapid=" + mapid + ", x=" + x + ", y=" + y
				+ ", time=" + time + "]";
	}
}
