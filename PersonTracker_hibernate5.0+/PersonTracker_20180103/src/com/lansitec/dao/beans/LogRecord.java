package com.lansitec.dao.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.lansitec.enumlist.LogObj;
import com.lansitec.enumlist.LogType;

public class LogRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Integer id;
    private String user;
    private LogObj obj;
    private LogType type;
    private String name;
    private String content;
    private LocalDateTime time;
    
    public LogRecord(){
    	
    }
    public LogRecord(String user,LogObj obj,LogType type,String name,String content,LocalDateTime time){
    	this.user = user;
    	this.obj = obj;
    	this.type = type;
    	this.name = name;
    	this.content = content;
    	this.time = time;
    	
    }
	public Integer getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public LogObj getObj() {
		return obj;
	}
	public LogType getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getContent() {
		return content;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setObj(LogObj obj) {
		this.obj = obj;
	}
	public void setType(LogType type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
