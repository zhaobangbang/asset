package com.lansitec.dao.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.lansitec.enumlist.WarnType;

public class WarnRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Integer id;
    private String deveui;
    private WarnType type;
    private String description;
    private LocalDateTime warn_stime;
    private LocalDateTime warn_etime;
    private boolean warn_on;
    public WarnRecord(){
    	
    }
    public WarnRecord(String deveui,WarnType type,String description,LocalDateTime warn_stime,boolean warn_on){
    	this.deveui =deveui;
    	this.type = type;
    	this.description = description;
    	this.warn_stime = warn_stime;
    	this.warn_on = warn_on;
    }
	public Integer getId() {
		return id;
	}
	public String getDeveui() {
		return deveui;
	}
	public WarnType getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getWarn_stime() {
		return warn_stime;
	}
	public LocalDateTime getWarn_etime() {
		return warn_etime;
	}
	public boolean isWarn_on() {
		return warn_on;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public void setType(WarnType type) {
		this.type = type;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setWarn_stime(LocalDateTime warn_stime) {
		this.warn_stime = warn_stime;
	}
	public void setWarn_etime(LocalDateTime warn_etime) {
		this.warn_etime = warn_etime;
	}
	public void setWarn_on(boolean warn_on) {
		this.warn_on = warn_on;
	}
	@Override
	public String toString() {
		return "WarnRecord [id=" + id + ", deveui=" + deveui + ", type=" + type + ", description=" + description
				+ ", warn_stime=" + warn_stime + ", warn_etime=" + warn_etime + ", warn_on=" + warn_on + "]";
	}
}
